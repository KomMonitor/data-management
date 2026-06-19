package de.hsbo.kommonitor.datamanagement.config;

import de.hsbo.kommonitor.datamanagement.model.AbstractClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.AbstractClassificationMappingTypeTemplate;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeIndicatorValues;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.annotation.JsonDeserialize;

@Configuration
public class JacksonConfig {
    @Bean
    public JsonMapperBuilderCustomizer customizeJackson() {
        return builder -> builder
                .addMixIn(
                        AbstractClassificationMappingType.class,
                        AbstractClassificationMappingTypeTemplate.class
                )
                .addMixIn(
                        IndicatorPOSTInputTypeIndicatorValues.class,
                        IndicatorPOSTInputTypeIndicatorValuesMixin.class
                );
    }

    @JsonDeserialize(using = IndicatorPOSTInputTypeIndicatorValuesDeserializer.class)
    public interface IndicatorPOSTInputTypeIndicatorValuesMixin {
    }
}

