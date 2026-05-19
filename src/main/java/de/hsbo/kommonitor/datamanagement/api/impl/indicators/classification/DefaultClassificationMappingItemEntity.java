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

    private List<String> labels = null;

    private List<String> individualColors;

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

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }


    public List<String> getIndividualColors() {
        return individualColors;
    }

    public void setIndividualColors(List<String> individualColors) {
        this.individualColors = individualColors;
    }
}
