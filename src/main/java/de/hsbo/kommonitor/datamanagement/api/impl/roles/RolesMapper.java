package de.hsbo.kommonitor.datamanagement.api.impl.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.model.privilege.PrivilegesEntity;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;


public class RolesMapper {

	public static RoleOverviewType mapToSwaggerRole(RolesEntity roleEntity) {
RoleOverviewType role = new RoleOverviewType(roleEntity.getRoleId());
		
		role.setRoleName(roleEntity.getRoleName());
		
		/*
		 * privileges
		 */
		Collection<PrivilegesEntity> privilegeEntities = roleEntity.getPrivileges();
		List<String> privileges = new ArrayList<>(privilegeEntities.size());
		for (PrivilegesEntity privilegesEntity : privilegeEntities) {
			privileges.add(privilegesEntity.getPrivilegeName());
		}
		role.setPrivileges(privileges);

		
		return role;
	}

	public static List<RoleOverviewType> mapToSwaggerRoles(List<RolesEntity> roleEntities) {
List<RoleOverviewType> roles = new ArrayList<RoleOverviewType>(roleEntities.size());
		
		for (RolesEntity roleEntity : roleEntities) {
			roles.add(mapToSwaggerRole(roleEntity));
		}
		return roles;

	}

}
