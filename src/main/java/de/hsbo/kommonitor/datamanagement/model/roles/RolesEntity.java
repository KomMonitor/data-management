package de.hsbo.kommonitor.datamanagement.model.roles;

import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Roles")
public class RolesEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String roleId = null;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "organizationalUnit")
    private OrganizationalUnitEntity organizationalUnit;

    // We default to no permissions just in case
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PermissionLevelType permissionLevel = PermissionLevelType.NONE;

    /*
     * default constructor is required by hibernate / jpa
     */
    public RolesEntity() {

    }

    public OrganizationalUnitEntity getOrganizationalUnit() {
        return organizationalUnit;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setOrganizationalUnit(OrganizationalUnitEntity unit) {
        this.organizationalUnit = unit;
    }

    public PermissionLevelType getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevelType permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

}
