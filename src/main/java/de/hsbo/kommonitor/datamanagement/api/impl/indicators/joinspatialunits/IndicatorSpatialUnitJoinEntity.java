package de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import jakarta.persistence.*;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity(name = "IndicatorSpatialUnits")
public class IndicatorSpatialUnitJoinEntity implements Serializable, RestrictedEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String entryId;

    private String indicatorMetadataId = null;
    private String indicatorName = null;

    private String spatialUnitId = null;
    private String spatialUnitName = null;
    private String indicatorValueTableName = null;
    private String wmsUrl = null;
    private String wfsUrl = null;
    private String defaultStyleName = null;
    @Transient
    private List<PermissionLevelType> userPermissions;

    public IndicatorSpatialUnitJoinEntity() {
    }

    public String getIndicatorMetadataId() {
        return indicatorMetadataId;
    }

    public void setIndicatorMetadataId(String indicatorMetadataId) {
        this.indicatorMetadataId = indicatorMetadataId;
    }

    public String getSpatialUnitId() {
        return spatialUnitId;
    }

    public void setSpatialUnitId(String spatialUnitId) {
        this.spatialUnitId = spatialUnitId;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getIndicatorViewTableName() {
        return indicatorValueTableName;
    }

    public void setIndicatorValueTableName(String indicatorValueTableName) {
        this.indicatorValueTableName = indicatorValueTableName;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getSpatialUnitName() {
        return spatialUnitName;
    }

    public void setSpatialUnitName(String spatialUnitName) {
        this.spatialUnitName = spatialUnitName;
    }

    public String getWmsUrl() {
        return wmsUrl;
    }

    public void setWmsUrl(String wmsUrl) {
        this.wmsUrl = wmsUrl;
    }

    public String getWfsUrl() {
        return wfsUrl;
    }

    public void setWfsUrl(String wfsUrl) {
        this.wfsUrl = wfsUrl;
    }

    public String getDefaultStyleName() {
        return defaultStyleName;
    }

    public void setDefaultStyleName(String defaultStyleName) {
        this.defaultStyleName = defaultStyleName;
    }

    public List<PermissionLevelType> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(List<PermissionLevelType> userPermissions) {
        this.userPermissions = userPermissions;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "spatialunitid", updatable = false, insertable = false)
    private MetadataSpatialUnitsEntity metadataSpatialUnitsEntity;

    public MetadataSpatialUnitsEntity getMetadataSpatialUnitsEntity() {
        return metadataSpatialUnitsEntity;
    }

    public void setMetadataSpatialUnitsEntity(MetadataSpatialUnitsEntity metadataSpatialUnitsEntity) {
        this.metadataSpatialUnitsEntity = metadataSpatialUnitsEntity;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "indicatormetadataid", updatable = false, insertable = false)
    private MetadataIndicatorsEntity metadataIndicatorsEntity;

    public MetadataIndicatorsEntity getMetadataIndicatorsEntity() {
        return metadataIndicatorsEntity;
    }

    public void setMetadataIndicatorsEntity(MetadataIndicatorsEntity metadataIndicatorsEntity) {
        this.metadataIndicatorsEntity = metadataIndicatorsEntity;
    }

    @ManyToMany()
    @JoinTable(name = "indicatorSpatialUnits_roles",
            joinColumns = @JoinColumn(name = "indicatorspatialunit_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Collection<PermissionEntity> permissions;

    @ManyToOne
    private OrganizationalUnitEntity owner;

    @Column
    private boolean isPublic;

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
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

//    @ManyToMany()
//    @JoinTable(name = "metadataIndicators_roles",
//            joinColumns = @JoinColumn(name = "metadataindicators_id", referencedColumnName = "indicatorMetadataId"),
//            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "roleid"))
//    private Collection<RolesEntity> indicatorRoles;
//
//    public HashSet<RolesEntity> getIndicatorRoles() {
//        return new HashSet<RolesEntity>(indicatorRoles);
//    }
//
//    public void setIndicatorRoles(Collection<RolesEntity> indicatorRoles) {
//        this.indicatorRoles = new HashSet<RolesEntity>(indicatorRoles);
//    }
//
//    @Transient
//    @ManyToMany()
//    @JoinTable(name = "metadataSpatialUnits_roles",
//            joinColumns = @JoinColumn(name = "metadataspatialunits_id", referencedColumnName = "spatialUnitId"),
//            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "roleid"))
//    private Collection<RolesEntity> spatialUnitRoles;
//
//    public HashSet<RolesEntity> getSpatialUnitRoles() {
//        return new HashSet<RolesEntity>(spatialUnitRoles);
//    }
//
//    public void setSpatialUnitRoles(Collection<RolesEntity> spatialUnitRoles) {
//        this.spatialUnitRoles = new HashSet<RolesEntity>(spatialUnitRoles);
//    }

}
