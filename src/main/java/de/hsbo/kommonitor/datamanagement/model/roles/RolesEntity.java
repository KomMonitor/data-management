package de.hsbo.kommonitor.datamanagement.model.roles;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import de.hsbo.kommonitor.datamanagement.model.privilege.PrivilegesEntity;
import de.hsbo.kommonitor.datamanagement.model.users.UsersEntity;

@Entity(name = "Roles")
public class RolesEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String roleId = null;

	private String roleName = null;
	
	@ManyToMany(mappedBy = "userRoles")
    private Collection<UsersEntity> users;
	
	@ManyToMany
    @JoinTable(
        name = "roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "roleid"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "privilegeid"))
    private Collection<PrivilegesEntity> privileges;

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

	public Collection<UsersEntity> getUsers() {
		return users;
	}

	public void setUsers(Collection<UsersEntity> users) {
		this.users = users;
	}

	public Collection<PrivilegesEntity> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Collection<PrivilegesEntity> privileges) {
		this.privileges = privileges;
	}

}
