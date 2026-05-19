package de.hsbo.kommonitor.datamanagement.api.impl.indicators.classification;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "qualitativeclassificationmappingitemtype")
public class QualitativeClassificationMappingItemEntity extends AbstractClassificationMappingItemType {

    @Column(name = "spatialunitid", nullable = false)
    private String spatialUnitId;

    @OneToMany(mappedBy = "parentMapping", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CategoricalMappingItemEntity> categoricalData;

    public String getSpatialUnitId() {
        return spatialUnitId;
    }

    public void setSpatialUnitId(String spatialUnitId) {
        this.spatialUnitId = spatialUnitId;
    }

    public Collection<CategoricalMappingItemEntity> getCategoricalData() {
        return categoricalData;
    }

    public void setCategoricalData(Collection<CategoricalMappingItemEntity> categoricalData) {
        this.categoricalData = categoricalData;
    }

    public void addCategoricalMappingItem(CategoricalMappingItemEntity item) {
        if (this.categoricalData == null) {
            this.categoricalData = new ArrayList<>();
        }
        this.categoricalData.add(item);
        item.setParentMapping(this);
    }
}
