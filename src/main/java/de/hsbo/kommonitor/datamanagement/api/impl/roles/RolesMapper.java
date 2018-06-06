package de.hsbo.kommonitor.datamanagement.api.impl.roles;

import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;


public class RolesMapper {

	public static RoleOverviewType mapToSwaggerRole(RolesEntity roleEntity) {
RoleOverviewType role = new RoleOverviewType(roleEntity.getRoleId());
		
		role.setRoleName(roleEntity.getRoleName());

		
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
