package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Roles")
public class RolesEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String roleId = null;
    
    @Column(nullable = true)
    private String roleName = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizationalUnit")
    private OrganizationalUnitEntity organizationalUnit;

    // We default to no permissions just in case
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = true)
//    private PermissionLevelType permissionLevel = PermissionLevelType.NONE();
    private PermissionLevelType permissionLevel = null;

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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
