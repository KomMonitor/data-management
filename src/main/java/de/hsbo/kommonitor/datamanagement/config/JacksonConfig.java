package de.hsbo.kommonitor.datamanagement.config;

import de.hsbo.kommonitor.datamanagement.model.AbstractClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.AbstractClassificationMappingTypeTemplate;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJackson() {
        return builder -> builder.mixIn(
                AbstractClassificationMappingType.class,
                AbstractClassificationMappingTypeTemplate.class
        );
    }
}
