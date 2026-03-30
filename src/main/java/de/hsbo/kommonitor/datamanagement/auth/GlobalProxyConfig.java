package de.hsbo.kommonitor.datamanagement.auth;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

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
    
    @Bean
    public JwtDecoder jwtDecoder() {
        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null) {
            log.info("Configuring JwtDecoder to use Proxy {}:{}", proxyHost, proxyPort);
            
            HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                    .proxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)));

            if (proxyUser != null && !proxyUser.isEmpty()) {
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

            return NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                    .restOperations(restTemplate)
                    .build();
        }

        log.info("No proxy configured for JwtDecoder. Using direct connection to {}", jwkSetUri);
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @PostConstruct
    public void initGlobalProxy() {
        if (proxyHost != null && proxyPort != null) {
            log.info("Initializing global JVM proxy settings: {}:{}", proxyHost, proxyPort);
        	
            System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", String.valueOf(proxyPort));
            System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|host.docker.internal");

            if (proxyUser != null && proxyPassword != null) {
                log.info("Setting global Authenticator for proxy user: {}", proxyUser);
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType() == RequestorType.PROXY) {
                            log.debug("Global Authenticator providing credentials for proxy request");
                            return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                        }
                        return null;
                    }
                });
            }
        } else {
            log.debug("Global proxy settings skipped - properties not set.");
        }
    }
}