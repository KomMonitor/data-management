package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Permissions")
public class PermissionEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String permissionId = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizationalUnit")
    private OrganizationalUnitEntity organizationalUnit;

    // We default to no permissions just in case
    @Enumerated(EnumType.ORDINAL)
    @Column()
    private PermissionLevelType permissionLevel = null;

    private String name;

    private String permissionType;

    /*
     * default constructor is required by hibernate / jpa
     */

    public PermissionEntity() {

    }
    public OrganizationalUnitEntity getOrganizationalUnit() {
        return organizationalUnit;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setOrganizationalUnit(OrganizationalUnitEntity unit) {
        this.organizationalUnit = unit;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public PermissionLevelType getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevelType permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
