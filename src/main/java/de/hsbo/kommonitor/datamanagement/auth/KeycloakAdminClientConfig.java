package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminClientConfig {

    private static final String CLIENT_ID = "admin-cli";

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.cli-secret}")
    private String cliSecret;


    @Bean
    Keycloak configureKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(keycloakRealm)
                .clientId(CLIENT_ID)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret(cliSecret)
                .build();
    }

}
