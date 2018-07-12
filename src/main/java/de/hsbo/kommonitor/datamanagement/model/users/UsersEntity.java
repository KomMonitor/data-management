package de.hsbo.kommonitor.datamanagement.model.users;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Entity(name = "Users")
public class UsersEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String userId = null;

	private String userName = null;

	private String password = null;
	
	@ManyToMany
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "userid"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "roleid")) 
    private Collection<RolesEntity> userRoles;

	/*
	 * default constructor is required by hibernate / jpa
	 */
	public UsersEntity() {

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}
	
	public Collection<RolesEntity> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Collection<RolesEntity> userRoles) {
		this.userRoles = userRoles;
	}

}
