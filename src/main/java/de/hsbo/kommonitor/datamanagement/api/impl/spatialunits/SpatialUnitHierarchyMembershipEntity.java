package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.UuidGenerator;

/**
 * Membership of a spatial unit within a {@link SpatialUnitHierarchyEntity}.  A spatial unit may be a member of several
 * hierarchies, all owned by the same mandant.
 */
@Entity(name = "SpatialUnitHierarchyMemberships")
@Table(name = "spatialunithierarchy_memberships")
public class SpatialUnitHierarchyMembershipEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private String id = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hierarchy_id")
    private SpatialUnitHierarchyEntity hierarchy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "spatialunit_datasetid", referencedColumnName = "datasetid")
    private MetadataSpatialUnitsEntity spatialUnit;

    private Integer hierarchyLevel;

    @ManyToOne
    @JoinColumn(name = "nextlower_spatialunit_datasetid", referencedColumnName = "datasetid")
    private MetadataSpatialUnitsEntity nextLowerSpatialUnit;

    @ManyToOne
    @JoinColumn(name = "nextupper_spatialunit_datasetid", referencedColumnName = "datasetid")
    private MetadataSpatialUnitsEntity nextUpperSpatialUnit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpatialUnitHierarchyEntity getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(SpatialUnitHierarchyEntity hierarchy) {
        this.hierarchy = hierarchy;
    }

    public MetadataSpatialUnitsEntity getSpatialUnit() {
        return spatialUnit;
    }

    public void setSpatialUnit(MetadataSpatialUnitsEntity spatialUnit) {
        this.spatialUnit = spatialUnit;
    }

    public Integer getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public MetadataSpatialUnitsEntity getNextLowerSpatialUnit() {
        return nextLowerSpatialUnit;
    }

    public void setNextLowerSpatialUnit(MetadataSpatialUnitsEntity nextLowerSpatialUnit) {
        this.nextLowerSpatialUnit = nextLowerSpatialUnit;
    }

    public MetadataSpatialUnitsEntity getNextUpperSpatialUnit() {
        return nextUpperSpatialUnit;
    }

    public void setNextUpperSpatialUnit(MetadataSpatialUnitsEntity nextUpperSpatialUnit) {
        this.nextUpperSpatialUnit = nextUpperSpatialUnit;
    }
}
