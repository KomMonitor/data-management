package de.hsbo.kommonitor.datamanagement.api.impl.indicators.classification;

import jakarta.persistence.*;

@Entity
@Table(name = "categorical_mapping_item")
public class CategoricalMappingItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mappingid", referencedColumnName = "mappingid", nullable = false)
    private QualitativeClassificationMappingItemEntity parentMapping;

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

    public QualitativeClassificationMappingItemEntity getParentMapping() {
        return parentMapping;
    }

    public void setParentMapping(QualitativeClassificationMappingItemEntity parentMapping) {
        this.parentMapping = parentMapping;
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
