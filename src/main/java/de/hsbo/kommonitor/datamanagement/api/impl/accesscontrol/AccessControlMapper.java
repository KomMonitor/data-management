package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

public class AccessControlMapper {

    public static RoleOverviewType mapToSwaggerRole(RolesEntity roleEntity) {
        RoleOverviewType role = new RoleOverviewType();
        role.setRoleId(roleEntity.getRoleId());
        role.setPermissionLevel(roleEntity.getPermissionLevel());
        return role;
    }

    public static OrganizationalUnitOverviewType mapToSwaggerOrganizationalUnit(OrganizationalUnitEntity ouEntity) {
        OrganizationalUnitOverviewType ou = new OrganizationalUnitOverviewType();
        ou.setOrganizationalUnitId(ouEntity.getOrganizationalUnitId());
        ou.setName(ouEntity.getName());
        ou.setContact(ouEntity.getContact());
        ou.setDescription(ouEntity.getDescription());
        ou.setRoles(mapToSwaggerRoles(ouEntity.getRoles()));
        return ou;
    }

    public static List<RoleOverviewType> mapToSwaggerRoles(List<RolesEntity> roleEntities) {
        List<RoleOverviewType> roles = new ArrayList<RoleOverviewType>(roleEntities.size());

        for (RolesEntity roleEntity : roleEntities) {
            roles.add(mapToSwaggerRole(roleEntity));
        }
        return roles;
    }

    public static List<OrganizationalUnitOverviewType> mapToSwaggerOrganizationalUnits(
        List<OrganizationalUnitEntity> ouEntities
    ) {
        List<OrganizationalUnitOverviewType> ous = new ArrayList<>(ouEntities.size());

        for (OrganizationalUnitEntity entity : ouEntities) {
            ous.add(mapToSwaggerOrganizationalUnit(entity));
        }
        return ous;
    }

}
