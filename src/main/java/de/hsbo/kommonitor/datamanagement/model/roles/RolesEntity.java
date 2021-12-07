package de.hsbo.kommonitor.datamanagement.model.roles;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Roles")
public class RolesEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String roleId = null;

	@Column(unique = true, nullable = false)
	private String organizationalUnit = null;

    // We default to no permissions just in case
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PermissionLevelType permissionLevel = PermissionLevelType.NONE;

	/*
	 * default constructor is required by hibernate / jpa
	 */
	public RolesEntity() {

	}

	public String getOrganizationalUnit() {
		return organizationalUnit;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setOrganizationalUnit(String roleName) {
		this.organizationalUnit = roleName;
	}

    public PermissionLevelType getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevelType permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

}
