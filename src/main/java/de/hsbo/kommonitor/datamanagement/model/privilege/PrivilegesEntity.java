package de.hsbo.kommonitor.datamanagement.model.privilege;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Entity(name = "Privileges")
public class PrivilegesEntity {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String privilegeId = null;

	private String privilegeName;
	 
    @ManyToMany(mappedBy = "privileges")
    private Collection<RolesEntity> roles;
    
    public PrivilegesEntity(){}

	public String getPrivilegeId() {
		return privilegeId;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public Collection<RolesEntity> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RolesEntity> roles) {
		this.roles = roles;
	};
}
