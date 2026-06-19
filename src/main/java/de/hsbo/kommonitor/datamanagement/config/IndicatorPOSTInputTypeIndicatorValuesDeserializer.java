package de.hsbo.kommonitor.datamanagement.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeCategoricalValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeNumericalValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorValueTypeEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndicatorPOSTInputTypeIndicatorValuesDeserializer extends StdDeserializer<IndicatorPOSTInputTypeIndicatorValues> {

    public IndicatorPOSTInputTypeIndicatorValuesDeserializer() {
        super(IndicatorPOSTInputTypeIndicatorValues.class);
    }

    @Override
    public IndicatorPOSTInputTypeIndicatorValues deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        IndicatorPOSTInputTypeIndicatorValues result = new IndicatorPOSTInputTypeIndicatorValues();

        if (node.has("spatialReferenceKey") && !node.get("spatialReferenceKey").isNull()) {
            result.setSpatialReferenceKey(node.get("spatialReferenceKey").asText());
        }

        IndicatorValueTypeEnum valueType = IndicatorValueTypeEnum.NUMERIC;
        if (node.has("valueType") && !node.get("valueType").isNull()) {
            valueType = mapper.treeToValue(node.get("valueType"), IndicatorValueTypeEnum.class);
        }
        result.setValueType(valueType);

        if (node.has("valueMapping") && node.get("valueMapping").isArray()) {
            List<IndicatorPOSTInputTypeValueMapping> list = new ArrayList<>();
            for (JsonNode element : node.get("valueMapping")) {
                if (valueType == IndicatorValueTypeEnum.CATEGORICAL) {
                    list.add(mapper.treeToValue(element, IndicatorPOSTInputTypeCategoricalValueMapping.class));
                } else {
                    list.add(mapper.treeToValue(element, IndicatorPOSTInputTypeNumericalValueMapping.class));
                }
            }
            result.setValueMapping(list);
        }

        return result;
    }
}
