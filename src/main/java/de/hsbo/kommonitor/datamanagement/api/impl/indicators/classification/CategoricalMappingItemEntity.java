package de.hsbo.kommonitor.datamanagement.api.impl.indicators.classification;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "categorical_mapping_item")
public class CategoricalMappingItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid", nullable = false)
    private MetadataIndicatorsEntity indicator;

    @Column(name = "categorical_value", nullable = false)
    private String categoricalValue;

    @Column(name = "color", length = 9, nullable = false)
    private String color;

    @Column(name = "label")
    private String label;

    public CategoricalMappingItemEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetadataIndicatorsEntity getIndicator() {
        return indicator;
    }

    public void setIndicator(MetadataIndicatorsEntity indicator) {
        this.indicator = indicator;
    }

    public String getCategoricalValue() {
        return categoricalValue;
    }

    public void setCategoricalValue(String categoricalValue) {
        this.categoricalValue = categoricalValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
