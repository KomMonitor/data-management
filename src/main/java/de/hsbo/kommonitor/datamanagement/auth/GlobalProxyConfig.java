package de.hsbo.kommonitor.datamanagement.auth;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.client.ClientRequestFilter;

import java.util.Base64;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalProxyConfig {

    private static final Logger log = LoggerFactory.getLogger(GlobalProxyConfig.class);

    @Value("${proxy.host:#{null}}")
    private String proxyHost;

    @Value("${proxy.port:#{null}}")
    private Integer proxyPort;

    @Value("${proxy.user:#{null}}")
    private String proxyUser;

    @Value("${proxy.password:#{null}}")
    private String proxyPassword;
    
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:#{null}}")
    private String jwkSetUri;
    
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:#{null}}")
    private String issuerUri;
    
    private Set<Pattern> nonProxyPatterns;
    
    /**
     * Hilfsmethode zur Prüfung der Basis-Proxy-Konfiguration
     */
    private boolean isProxyConfigured() {
        return proxyHost != null && !proxyHost.trim().isEmpty() && proxyPort != null && proxyPort > 0;
    }
    
    /**
     * Hilfsmethode zur Prüfung, ob Proxy-Credentials vorhanden sind
     */
    private boolean hasProxyCredentials() {
        return proxyUser != null && !proxyUser.trim().isEmpty();
    }
    
    /**
     * Validiert die Proxy-Parameter und gibt hilfreiche Warnungen in den Logs aus.
     */
    private void validateProxyConfig() {
        boolean hasHost = proxyHost != null && !proxyHost.trim().isEmpty();
        boolean hasPort = proxyPort != null && proxyPort > 0;
        boolean hasUser = proxyUser != null && !proxyUser.trim().isEmpty();
        boolean hasPass = proxyPassword != null && !proxyPassword.trim().isEmpty();

        // Fall 1: Host da, aber Port fehlt (oder umgekehrt)
        if (hasHost != hasPort) {
            log.warn("INCOMPLETE PROXY CONFIGURATION: Both 'proxy.host' and 'proxy.port' must be set. " +
                     "Current state: host={}, port={}. Proxy will be DISABLED.", proxyHost, proxyPort);
        }

        // Fall 2: User da, aber Passwort fehlt (oder umgekehrt)
        if (hasUser != hasPass) {
            log.warn("INCOMPLETE PROXY CREDENTIALS: Both 'proxy.user' and 'proxy.password' should be set for authenticated proxies. " +
                     "Current state: user={}, password={}. Proxy authentication might fail.", 
                     proxyUser, (hasPass ? "********" : "MISSING"));
        }
    }
    
    @PostConstruct
    public void init() {
    	
    	// 1. Prüfung auf Unvollständigkeit (Wichtig für Admins!)
        validateProxyConfig();
    	
        // Erst die Hosts mergen und System Properties setzen
        setupSystemProperties();
        // Dann sofort die Patterns daraus generieren
        setupNonProxyPatterns();
    }
    
    private void setupSystemProperties() {
        if (isProxyConfigured()) {
            log.info("Initializing global JVM proxy settings: {}:{}", proxyHost, proxyPort);
            
            String existingHosts = System.getProperty("http.nonProxyHosts");
            Set<String> hosts = new HashSet<>(Arrays.asList(
                "localhost", "127.0.0.1", "host.docker.internal"
            ));

            if (existingHosts != null && !existingHosts.isEmpty()) {
                Arrays.stream(existingHosts.split("\\|"))
                      .map(String::trim)
                      .filter(s -> !s.isEmpty())
                      .forEach(hosts::add);
            }

            String combinedHosts = String.join("|", hosts);
            System.setProperty("http.nonProxyHosts", combinedHosts);
            System.setProperty("https.nonProxyHosts", combinedHosts);
        	
            System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", String.valueOf(proxyPort));
            
            if (hasProxyCredentials()) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        // Explizite Prüfung: Nur für Proxies!
                        if (getRequestorType() == RequestorType.PROXY) {
                            return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                        }
                        return null; 
                    }
                });
            }
        }
    }

    private void setupNonProxyPatterns() {
    	if (isProxyConfigured()) {
    		String systemNonProxy = System.getProperty("http.nonProxyHosts", "");
            this.nonProxyPatterns = Arrays.stream(systemNonProxy.split("\\|"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(host -> host.replace(".", "\\.").replace("*", ".*"))
                    .map(regex -> Pattern.compile("^" + regex + "$", Pattern.CASE_INSENSITIVE))
                    .collect(Collectors.toSet());
                    
            log.info("Initialized Proxy Bypass for hosts: {}", systemNonProxy);
    	}
        
    }
    
    private boolean isInternal(String host) {
        if (host == null || host.isEmpty()) return false;
        
        // Prüft, ob der Host gegen eines der Patterns matcht
        return nonProxyPatterns.stream().anyMatch(pattern -> pattern.matcher(host).matches());
    }
    
//    @Bean
//    @Primary
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        // 1. HttpClient konfigurieren
//        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
//                .connectTimeout(Duration.ofSeconds(10));
//
//        // Nur einen ProxySelector einbauen, wenn die Basis-Parameter da sind
//        if (isProxyConfigured()) {
//            log.info("RestTemplate: Proxy configuration detected ({}:{}).", proxyHost, proxyPort);
//            httpClientBuilder.proxy(new ProxySelector() {
//                @Override
//                public List<Proxy> select(URI uri) {
//                    // Wenn intern, dann Proxy umgehen
//                    if (isInternal(uri.getHost())) {
//                        log.debug("Internal request to {} - bypassing proxy.", uri.getHost());
//                        return List.of(Proxy.NO_PROXY);
//                    }
//                    // Wenn extern, konfigurierten Proxy nutzen
//                    return List.of(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
//                }
//
//                @Override
//                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
//                    log.error("Connection to proxy {}:{} failed for URI: {}", proxyHost, proxyPort, uri, ioe);
//                }
//            });
//        } else {
//            log.info("RestTemplate: No proxy configured (direct connection).");
//        }
//
//        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClientBuilder.build());
//
//        return builder
//                .requestFactory(() -> requestFactory)
//                .additionalInterceptors((request, body, execution) -> {
//                    String host = request.getURI().getHost();
//
//                    // A) Bearer Token Propagation (Immer, falls Auth vorhanden)
//                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                    if (auth instanceof JwtAuthenticationToken jwt) {
//                        request.getHeaders().setBearerAuth(jwt.getToken().getTokenValue());
//                        log.debug("Propagated Bearer Token to host: {}", host);
//                    }
//
//                    // B) Manuelle Proxy-Auth (Nur wenn extern + Proxy konfiguriert + Credentials vorhanden)
//                    if (isProxyConfigured() && !isInternal(host) && hasProxyCredentials()) {
//                        String authStr = proxyUser + ":" + (proxyPassword != null ? proxyPassword : "");
//                        String encodedAuth = java.util.Base64.getEncoder().encodeToString(authStr.getBytes());
//                        request.getHeaders().set("Proxy-Authorization", "Basic " + encodedAuth);
//                        log.debug("Added Proxy-Authorization header for external host: {}", host);
//                    }
//
//                    return execution.execute(request, body);
//                })
//                .build();
//    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
    	NimbusJwtDecoder jwtDecoder = null;
		
    	// 1. Basis-Decoder mit der JWK-Set-URI erstellen
        if (isProxyConfigured()) {
            log.info("Configuring JwtDecoder to use Proxy {}:{}", proxyHost, proxyPort);
            
            HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                    .proxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)));

            if (hasProxyCredentials()) {
                log.info("Proxy authentication enabled for user: {}", proxyUser);
                httpClientBuilder.authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                    }
                });
            }

            JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClientBuilder.build());
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            
            // WICHTIG: Verbessertes Error Handling für den Zertifikats-Abruf
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
                public void handleError(java.io.IOException e) {
                    log.error("Failed to fetch JWKS from {} via Proxy. Check proxy credentials and connectivity.", jwkSetUri, e);
                }
            });

            jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                    .restOperations(restTemplate)
                    .build();
        }
        else {
        	log.info("No proxy configured for JwtDecoder. Using direct connection to {}", jwkSetUri);
            jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        }
        
     // 2. Optionale Validatoren vorbereiten
        OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();

        if (issuerUri != null && !issuerUri.isEmpty()) {
            log.info("Adding optional Issuer validation for URI: {}", issuerUri);
            
            // Erstellt einen kombinierten Validator (Timestamp + Issuer)
            OAuth2TokenValidator<Jwt> issuerValidator = new JwtIssuerValidator(issuerUri);
            OAuth2TokenValidator<Jwt> combinedValidator = new DelegatingOAuth2TokenValidator<>(defaultValidator, issuerValidator);
            
            jwtDecoder.setJwtValidator(combinedValidator);
        } else {
            log.warn("No issuer-uri provided. JWT issuer validation is DISABLED!");
            jwtDecoder.setJwtValidator(defaultValidator);
        }
        
        return jwtDecoder; 
    }
    
    /**
     * CLEAN CODE: Single Source of Truth for underlying network behavior.
     * Generates a native Java HttpClient with global proxy and bypass rules.
     */
    private HttpClient buildSharedHttpClient(String loggingContext) {
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10));

        if (isProxyConfigured()) {
            log.info("{}: Proxy configuration detected ({}:{}).", loggingContext, proxyHost, proxyPort);
            httpClientBuilder.proxy(new ProxySelector() {
                @Override
                public List<Proxy> select(URI uri) {
                    if (isInternal(uri.getHost())) {
                        log.debug("Internal request to {} - bypassing proxy.", uri.getHost());
                        return List.of(Proxy.NO_PROXY);
                    }
                    return List.of(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    log.error("Connection to proxy {}:{} failed for URI: {}", proxyHost, proxyPort, uri, ioe);
                }
            });
        } else {
            log.info("{}: No proxy configured (direct connection).", loggingContext);
        }

        return httpClientBuilder.build();
    }
    
    @Bean
    @Primary
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Reuse the core HttpClient definition
        HttpClient httpClient = buildSharedHttpClient("RestTemplate");
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);

        return builder
                .requestFactory(() -> requestFactory)
                .additionalInterceptors((request, body, execution) -> {
                    String host = request.getURI().getHost();

                    // A) Bearer Token Propagation
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth instanceof JwtAuthenticationToken jwt) {
                        request.getHeaders().setBearerAuth(jwt.getToken().getTokenValue());
                        log.debug("Propagated Bearer Token to host: {}", host);
                    }

                    // B) Proxy-Auth Header
                    if (isProxyConfigured() && !isInternal(host) && hasProxyCredentials()) {
                        request.getHeaders().set("Proxy-Authorization", buildProxyAuthHeader());
                        log.debug("Added Proxy-Authorization header for external host: {}", host);
                    }

                    return execution.execute(request, body);
                })
                .build();
    }
    
    @Bean
    public ResteasyClient resteasyClient() {
        // 1. Reuse identical core HttpClient routing configurations
        HttpClient httpClient = buildSharedHttpClient("ResteasyClient");

        // 2. Initialize RESTEasy builder
        ResteasyClientBuilderImpl builder = new ResteasyClientBuilderImpl();
        
        // 3. Register custom Keycloak provider with its specific priority
        builder.register(new KeycloakRestClientProvider(), 1000);

        // 4. Register the Proxy-Auth Header Filter (equivalent to RestTemplate Interceptor)
        builder.register((ClientRequestFilter) requestContext -> {
            String host = requestContext.getUri().getHost();

            if (isProxyConfigured() && !isInternal(host) && hasProxyCredentials()) {
                requestContext.getHeaders().add("Proxy-Authorization", buildProxyAuthHeader());
                log.debug("RESTEasy: Added Proxy-Authorization header for external host: {}", host);
            }
        });

        return builder.build();
    }
    
    private String buildProxyAuthHeader() {
        String authStr = proxyUser + ":" + (proxyPassword != null ? proxyPassword : "");
        return "Basic " + Base64.getEncoder().encodeToString(authStr.getBytes());
    }

}