package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitHierarchyMembershipEntity;
import jakarta.persistence.*;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;

@Entity(name = "MetadataSpatialUnits")
public class MetadataSpatialUnitsEntity extends AbstractMetadata implements RestrictedEntity {

    private int sridEpsg;
    private boolean isOutlineLayer = false;
    private String outlineColor = null;
    private Integer outlineWidth = null;
    private String outlineDashArrayString = null;

    @ManyToMany()
    @JoinTable(name = "metadataSpatialUnits_permissions",
            joinColumns = @JoinColumn(name = "metadataspatialunits_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Collection<PermissionEntity> permissions;

    @ManyToOne
    private OrganizationalUnitEntity owner;

    /**
     * The mandant this spatial unit belongs to. Spatial unit names are unique only
     * within a mandant, and a spatial unit may only be placed into hierarchies owned
     * by this mandant.
     */
    @ManyToOne
    @JoinColumn(name = "mandant_organizationalunitid")
    private OrganizationalUnitEntity mandant;

    @OneToMany(mappedBy = "spatialUnit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SpatialUnitHierarchyMembershipEntity> hierarchyMemberships = new ArrayList<>();

    @Column
    private Boolean isPublic;

    public HashSet<PermissionEntity> getPermissions() {
        return new HashSet<>(permissions);
    }

    public void setPermissions(Collection<PermissionEntity> permissions) {
        this.permissions = new HashSet<>(permissions);
    }

    public OrganizationalUnitEntity getOwner() {
        return owner;
    }

    public void setOwner(OrganizationalUnitEntity owner) {
        this.owner = owner;
    }

    @Override
    public Boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getSridEpsg() {
        return sridEpsg;
    }

    public void setSridEpsg(int sridEpsg) {
        this.sridEpsg = sridEpsg;
    }

    public OrganizationalUnitEntity getMandant() {
        return mandant;
    }

    public void setMandant(OrganizationalUnitEntity mandant) {
        this.mandant = mandant;
    }

    public List<SpatialUnitHierarchyMembershipEntity> getHierarchyMemberships() {
        return hierarchyMemberships;
    }

    public void setHierarchyMemberships(List<SpatialUnitHierarchyMembershipEntity> hierarchyMemberships) {
        this.hierarchyMemberships = hierarchyMemberships;
    }

    public boolean isOutlineLayer() {
        return isOutlineLayer;
    }

    public void setOutlineLayer(boolean isOutlineLayer) {
        this.isOutlineLayer = isOutlineLayer;
    }

    public String getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(String outlineColor) {
        this.outlineColor = outlineColor;
    }

    public String getOutlineDashArrayString() {
        return outlineDashArrayString;
    }

    public void setOutlineDashArrayString(String outlineDashArrayString) {
        this.outlineDashArrayString = outlineDashArrayString;
    }

    public Integer getOutlineWidth() {
        return outlineWidth;
    }

    public void setOutlineWidth(Integer outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    public HashSet<PeriodOfValidityEntity_spatialUnits> getSpatialUnitsPeriodsOfValidity() throws IOException, SQLException {
        AvailablePeriodsOfValidityType availablePeriodsOfValidity = SpatialFeatureDatabaseHandler.getAvailablePeriodsOfValidity(this.getDbTableName());

        HashSet<PeriodOfValidityEntity_spatialUnits> hashSet = new HashSet<PeriodOfValidityEntity_spatialUnits>();

        for (PeriodOfValidityType periodOfValidityType : availablePeriodsOfValidity) {
            PeriodOfValidityEntity_spatialUnits periodEntity = new PeriodOfValidityEntity_spatialUnits(periodOfValidityType);
            hashSet.add(periodEntity);
        }

        return hashSet;
    }

}
