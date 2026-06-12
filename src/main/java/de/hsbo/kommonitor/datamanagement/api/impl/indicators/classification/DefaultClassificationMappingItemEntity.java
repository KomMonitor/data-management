package de.hsbo.kommonitor.datamanagement.api.impl.indicators.classification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "DefaultClassificationMappingItemType")
public class DefaultClassificationMappingItemEntity extends AbstractClassificationMappingItemType{


    private String spatialUnitId = null;

    private List<Float> breaks = new ArrayList<>();

    public String getSpatialUnitId() {
        return spatialUnitId;
    }

    public void setSpatialUnitId(String spatialUnitId) {
        this.spatialUnitId = spatialUnitId;
    }

    public List<Float> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Float> breaks) {
        this.breaks = breaks;
    }

}
