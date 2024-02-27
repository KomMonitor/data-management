package de.hsbo.kommonitor.datamanagement.auth;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminClientConfig {

    @Bean
    Keycloak configureKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://keycloak:8080/")
                .realm("kommonitor")
                .clientId("admin-cli")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret("secret")
                .build();
    }

}
