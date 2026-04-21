package de.hsbo.kommonitor.datamanagement.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "classificationType",
        visible = true,
        defaultImpl = DefaultClassificationMappingType.class
)
public interface AbstractClassificationMappingTypeTemplate {
}
