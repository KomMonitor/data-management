package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewSpatialUnitElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrganizationalUnitMapper {

    public static OrganizationalUnitOverviewType mapToSwaggerOrganizationalUnit(OrganizationalUnitEntity ouEntity) {
        OrganizationalUnitOverviewType ou = new OrganizationalUnitOverviewType();
        ou.setOrganizationalUnitId(ouEntity.getOrganizationalUnitId());
        ou.setName(ouEntity.getName());
        ou.setContact(ouEntity.getContact());
        ou.setDescription(ouEntity.getDescription());
        ou.setPermissions(mapToSwaggerPermissions(ouEntity.getPermissions()));
        ou.setKeycloakId(ouEntity.getKeycloakId().toString());
        ou.setMandant(ouEntity.isMandant());
        if (ouEntity.getMandant() != null) {
            ou.setMandantId(ouEntity.getMandant().getOrganizationalUnitId());
        }
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

    /**
     * Maps the header information (id, name, contact, description) of an organizational unit onto a
     * permission overview. The grouped per-dataset permissions themselves are assembled in
     * {@link OrganizationalUnitManager}, because that requires access to the dataset repositories.
     */
    public static OrganizationalUnitPermissionOverviewType mapToSwapperOUPermissionOverviewType(
            OrganizationalUnitEntity ouEntity) {
        OrganizationalUnitPermissionOverviewType ou = new OrganizationalUnitPermissionOverviewType();
        ou.setOrganizationalUnitId(ouEntity.getOrganizationalUnitId());
        ou.setName(ouEntity.getName());
        ou.setContact(ouEntity.getContact());
        ou.setDescription(ouEntity.getDescription());
        return ou;
    }

    public static OrganizationalUnitPermissionOverviewElementType mapToPermissionElement(
            PermissionEntity permission, String datasetId) {
        OrganizationalUnitPermissionOverviewElementType element =
                new OrganizationalUnitPermissionOverviewElementType();
        element.setId(UUID.fromString(datasetId));
        element.setPermissionLevel(mapPermissionLevel(permission.getPermissionLevel()));
        element.setRoleId(UUID.fromString(permission.getPermissionId()));
        return element;
    }

    public static OrganizationalUnitPermissionOverviewSpatialUnitElementType mapToSpatialUnitPermissionElement(
            PermissionEntity permission, String indicatorId, String spatialUnitId) {
        OrganizationalUnitPermissionOverviewSpatialUnitElementType element =
                new OrganizationalUnitPermissionOverviewSpatialUnitElementType();
        element.setId(UUID.fromString(indicatorId));
        element.setPermissionLevel(mapPermissionLevel(permission.getPermissionLevel()));
        element.setRoleId(UUID.fromString(permission.getPermissionId()));
        element.setSpatialUnitId(UUID.fromString(spatialUnitId));
        return element;
    }

    private static String mapPermissionLevel(PermissionLevelType permissionLevel) {
        return permissionLevel != null ? permissionLevel.getValue() : null;
    }

}
