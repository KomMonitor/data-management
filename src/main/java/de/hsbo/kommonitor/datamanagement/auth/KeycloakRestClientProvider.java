package de.hsbo.kommonitor.datamanagement.auth;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

public class KeycloakRestClientProvider extends ResteasyJackson2Provider {

    public KeycloakRestClientProvider() {
        super();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        setMapper(mapper);
    }
}
