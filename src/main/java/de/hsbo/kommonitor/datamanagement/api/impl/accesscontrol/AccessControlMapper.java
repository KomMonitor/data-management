package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewTypePermissions;
import de.hsbo.kommonitor.datamanagement.model.PermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccessControlMapper {

    public static OrganizationalUnitOverviewType mapToSwaggerOrganizationalUnit(OrganizationalUnitEntity ouEntity) {
        OrganizationalUnitOverviewType ou = new OrganizationalUnitOverviewType();
        ou.setOrganizationalUnitId(ouEntity.getOrganizationalUnitId());
        ou.setName(ouEntity.getName());
        ou.setContact(ouEntity.getContact());
        ou.setDescription(ouEntity.getDescription());
        ou.setPermissions(mapToSwaggerPermissions(ouEntity.getPermissions()));
        ou.setKeycloakId(ouEntity.getKeycloakId().toString());
        ou.setMandant(ouEntity.isMandant());
        if (ouEntity.getParent() != null) {
            ou.setParentId(ouEntity.getParent().getOrganizationalUnitId());
        }
        if (ouEntity.getChildren() != null) {
            ou.setChildren(ouEntity.getChildren().stream().map(OrganizationalUnitEntity::getOrganizationalUnitId)
                    .collect(Collectors.toList()));
        }
        ou.setUserAdminRoles(ouEntity.getUserAdminRoles());
        return ou;
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

    public static PermissionOverviewType mapToSwaggerPermission(PermissionEntity roleEntity) {
        PermissionOverviewType role = new PermissionOverviewType();
        role.setPermissionId(roleEntity.getPermissionId());
        role.setPermissioneType(PermissionResourceType.fromValue(roleEntity.getPermissionType()));
        role.setPermissionLevel(roleEntity.getPermissionLevel());
        return role;
    }

    public static List<PermissionOverviewType> mapToSwaggerPermissions(List<PermissionEntity> roleEntities) {
        List<PermissionOverviewType> roles = new ArrayList<PermissionOverviewType>(roleEntities.size());

        for (PermissionEntity roleEntity : roleEntities) {
            roles.add(mapToSwaggerPermission(roleEntity));
        }
        return roles;
    }

    public static OrganizationalUnitPermissionOverviewType mapToSwapperOUPermissionOverviewType(
            OrganizationalUnitEntity ouEntity) {
        OrganizationalUnitPermissionOverviewType ou = new OrganizationalUnitPermissionOverviewType();
        ou.setOrganizationalUnitId(ouEntity.getOrganizationalUnitId());
        ou.setName(ouEntity.getName());
        ou.setContact(ouEntity.getContact());
        ou.setDescription(ouEntity.getDescription());

        OrganizationalUnitPermissionOverviewTypePermissions permissions =
                new OrganizationalUnitPermissionOverviewTypePermissions();

        //TODO(auti); create mapping

        ou.setPermissions(permissions);

        return ou;
    }

}
