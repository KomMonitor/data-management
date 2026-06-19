package de.hsbo.kommonitor.datamanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeCategoricalValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeNumericalValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorValueTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class IndicatorPOSTInputTypeIndicatorValuesDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.addMixIn(
                IndicatorPOSTInputTypeIndicatorValues.class,
                JacksonConfig.IndicatorPOSTInputTypeIndicatorValuesMixin.class
        );
    }

    @Test
    public void testNumericDeserialization() throws IOException {
        String json = "{\n" +
                "  \"spatialReferenceKey\": \"ref-123\",\n" +
                "  \"valueType\": \"NUMERIC\",\n" +
                "  \"valueMapping\": [\n" +
                "    {\n" +
                "      \"timestamp\": \"2026-06-19\",\n" +
                "      \"indicatorValue\": 42.5\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        IndicatorPOSTInputTypeIndicatorValues values = objectMapper.readValue(json, IndicatorPOSTInputTypeIndicatorValues.class);

        Assertions.assertEquals("ref-123", values.getSpatialReferenceKey());
        Assertions.assertEquals(IndicatorValueTypeEnum.NUMERIC, values.getValueType());
        
        List<IndicatorPOSTInputTypeValueMapping> mapping = values.getValueMapping();
        Assertions.assertEquals(1, mapping.size());
        
        IndicatorPOSTInputTypeValueMapping item = mapping.get(0);
        Assertions.assertTrue(item instanceof IndicatorPOSTInputTypeNumericalValueMapping);
        IndicatorPOSTInputTypeNumericalValueMapping numericalItem = (IndicatorPOSTInputTypeNumericalValueMapping) item;
        Assertions.assertEquals(42.5f, numericalItem.getIndicatorValue());
    }

    @Test
    public void testCategoricalDeserialization() throws IOException {
        String json = "{\n" +
                "  \"spatialReferenceKey\": \"ref-456\",\n" +
                "  \"valueType\": \"CATEGORICAL\",\n" +
                "  \"valueMapping\": [\n" +
                "    {\n" +
                "      \"timestamp\": \"2026-06-19\",\n" +
                "      \"indicatorValue\": \"high-risk\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        IndicatorPOSTInputTypeIndicatorValues values = objectMapper.readValue(json, IndicatorPOSTInputTypeIndicatorValues.class);

        Assertions.assertEquals("ref-456", values.getSpatialReferenceKey());
        Assertions.assertEquals(IndicatorValueTypeEnum.CATEGORICAL, values.getValueType());
        
        List<IndicatorPOSTInputTypeValueMapping> mapping = values.getValueMapping();
        Assertions.assertEquals(1, mapping.size());
        
        IndicatorPOSTInputTypeValueMapping item = mapping.get(0);
        Assertions.assertTrue(item instanceof IndicatorPOSTInputTypeCategoricalValueMapping);
        IndicatorPOSTInputTypeCategoricalValueMapping categoricalItem = (IndicatorPOSTInputTypeCategoricalValueMapping) item;
        Assertions.assertEquals("high-risk", categoricalItem.getIndicatorValue());
    }
}
