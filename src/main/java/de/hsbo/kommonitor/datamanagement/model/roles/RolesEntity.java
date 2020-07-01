package de.hsbo.kommonitor.datamanagement.model.roles;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Roles")
public class RolesEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String roleId = null;

	@Column(
			unique = true,
			nullable = false
	)
	private String roleName = null;


	/*
	 * default constructor is required by hibernate / jpa
	 */
	public RolesEntity() {

	}

	public String getRoleName() {
		return roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
