package de.hsbo.kommonitor.datamanagement.api.impl.indicators.classification;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractClassificationMappingItemType {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private String mappingId;

    // Getters and Setters
    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }
}