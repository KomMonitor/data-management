package de.hsbo.kommonitor.datamanagement.auth;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

@Configuration
public class KeycloakAdminClientConfig {

	private static final String CLIENT_ID = "admin-cli";

	@Value("${keycloak.auth-server-url}")
	private String keycloakUrl;

	@Value("${keycloak.realm}")
	private String keycloakRealm;

	@Value("${keycloak.cli-secret}")
	private String cliSecret;

	@Value("${proxy.host:#{null}}")
	protected String proxyHost;

	@Value("${proxy.port:#{null}}")
	protected Integer proxyPort;

	@Value("${proxy.user:#{null}}")
	protected String proxyUser;

	@Value("${proxy.password:#{null}}")
	protected String proxyPassword;
	
	

	public String getProxyHost() {
		return proxyHost;
	}



	public Integer getProxyPort() {
		return proxyPort;
	}



	public String getProxyUser() {
		return proxyUser;
	}



	public String getProxyPassword() {
		return proxyPassword;
	}



	@Bean
	Keycloak configureKeycloak() {
		
		// non prxy default client
		Client restEasyClient = ClientBuilder.newBuilder().register(new KeycloakRestClientProvider(), 1000).build();

		// configure proxy if set
		if (proxyHost != null && proxyPort != null) {
			// 1. Proxy-Zugangsdaten definieren
			CredentialsProvider credsProvider = new BasicCredentialsProvider();

			if (proxyUser != null && proxyPassword != null) {
				
				credsProvider.setCredentials(new AuthScope(getProxyHost(), getProxyPort()),
						new UsernamePasswordCredentials(getProxyUser(), getProxyPassword()));
			}
			
			// 2. HttpClient mit Proxy und Authentifizierung bauen			
			restEasyClient = ((ResteasyClientBuilder) ClientBuilder.newBuilder())
				    .httpEngine(new ApacheHttpClient43Engine(HttpClientBuilder.create()
				        .setProxy(new HttpHost(getProxyHost(), getProxyPort()))
				        .setDefaultCredentialsProvider(credsProvider)
				        .build())).register(new KeycloakRestClientProvider(), 1000)
				    .build();

		}

		
		return KeycloakBuilder.builder().serverUrl(keycloakUrl).realm(keycloakRealm).clientId(CLIENT_ID)
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS).clientSecret(cliSecret)
				.resteasyClient(restEasyClient)
				.build();
	}

}
