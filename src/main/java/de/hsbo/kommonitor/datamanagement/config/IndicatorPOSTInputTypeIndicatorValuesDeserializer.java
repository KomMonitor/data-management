package de.hsbo.kommonitor.datamanagement.config;

import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeCategoricalValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeNumericalValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorValueTypeEnum;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;
import java.util.ArrayList;
import java.util.List;

public class IndicatorPOSTInputTypeIndicatorValuesDeserializer extends ValueDeserializer<IndicatorPOSTInputTypeIndicatorValues> {

    public IndicatorPOSTInputTypeIndicatorValuesDeserializer() {
        super();
    }


    @Override
    public IndicatorPOSTInputTypeIndicatorValues deserialize(JsonParser jp, DeserializationContext ctxt) throws JacksonException{
        JsonNode node = ctxt.readTree(jp);

        IndicatorPOSTInputTypeIndicatorValues result = new IndicatorPOSTInputTypeIndicatorValues();

        if (node.has("spatialReferenceKey") && !node.get("spatialReferenceKey").isNull()) {
            result.setSpatialReferenceKey(node.get("spatialReferenceKey").asText());
        }

        IndicatorValueTypeEnum valueType = IndicatorValueTypeEnum.NUMERIC;
        if (node.has("valueType") && !node.get("valueType").isNull()) {
            valueType = ctxt.readTreeAsValue(node.get("valueType"), IndicatorValueTypeEnum.class);
        }
        result.setValueType(valueType);

        if (node.has("valueMapping") && node.get("valueMapping").isArray()) {
            List<IndicatorPOSTInputTypeValueMapping> list = new ArrayList<>();
            for (JsonNode element : node.get("valueMapping")) {
                if (valueType == IndicatorValueTypeEnum.CATEGORICAL) {
                    list.add(ctxt.readTreeAsValue(element, IndicatorPOSTInputTypeCategoricalValueMapping.class));
                } else {
                    list.add(ctxt.readTreeAsValue(element, IndicatorPOSTInputTypeNumericalValueMapping.class));
                }
            }
            result.setValueMapping(list);
        }

        return result;
    }
}
