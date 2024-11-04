package de.hsbo.kommonitor.datamanagement.auth;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

/**
 * This class is needed to avoid failing response parameter parsing. Keycloak Admin CLI and Keycloak REST Server
 * may differ in schema and default Keycloak REST client has a strict policy when dealing with undefined response
 * parameters. Therefore, a custom REST client provider is required, which supports deserialization of unknown
 * properties.
 */
public class KeycloakRestClientProvider extends ResteasyJackson2Provider {

    public KeycloakRestClientProvider() {
        super();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        setMapper(mapper);
    }
}
