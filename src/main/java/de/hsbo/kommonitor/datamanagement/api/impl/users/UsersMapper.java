package de.hsbo.kommonitor.datamanagement.api.impl.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.roles.RolesMapper;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;
import de.hsbo.kommonitor.datamanagement.model.users.UserOverviewType;
import de.hsbo.kommonitor.datamanagement.model.users.UsersEntity;

public class UsersMapper {

	public static UserOverviewType mapToSwaggerUser(UsersEntity userEntity) {
		UserOverviewType user = new UserOverviewType(userEntity.getUserId());

		user.setUserName(userEntity.getUserName());
		user.setUserId(userEntity.getUserId());

		// Roles
		Collection<RolesEntity> userRoles = userEntity.getUserRoles();
		List<RoleOverviewType> roles = RolesMapper.mapToSwaggerRoles(new ArrayList<>(userRoles));
		user.setRoles(roles);

		return user;
	}

	public static List<UserOverviewType> mapToSwaggerUsers(List<UsersEntity> userEntities) {
		List<UserOverviewType> users = new ArrayList<UserOverviewType>(userEntities.size());

		for (UsersEntity userEntity : userEntities) {
			users.add(mapToSwaggerUser(userEntity));
		}
		return users;

	}

}
