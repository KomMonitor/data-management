package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitJoinEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.AbstractMetadata;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.auth.AuthHelperService;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitOverviewType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewSpatialUnitElementType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitPermissionOverviewTypePermissions;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.PermissionOverviewType;
import de.hsbo.kommonitor.datamanagement.model.PermissionResourceType;
import de.hsbo.kommonitor.datamanagement.model.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.data.domain.Example;

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
            OrganizationalUnitEntity ouEntity,
            ResourceType resourceType) {
        var ou = new OrganizationalUnitPermissionOverviewType();
        ou.setOrganizationalUnitId(ouEntity.getOrganizationalUnitId());
        ou.setName(ouEntity.getName());
        ou.setContact(ouEntity.getContact());
        ou.setDescription(ouEntity.getDescription());
        var permissions = new OrganizationalUnitPermissionOverviewTypePermissions();
        ou.setPermissions(permissions);

        //TODO(auti); create mapping
        var authHelperService = AuthHelperService.GetInstance();
        var examplePermission = new PermissionEntity();
        examplePermission.setOrganizationalUnit(ouEntity);
        var examplePermissions = List.of(examplePermission);

        if (resourceType == null || resourceType == ResourceType.GEORESOURCES) {
            var example = new MetadataGeoresourcesEntity();
            example.setPermissions(examplePermissions);
            authHelperService.getGeoresourceRepository()
                .findBy(Example.of(example), q -> q.all())
                .forEach(re ->
                    getPermissionLevel(re, ouEntity)
                    .map(pl -> createElement(re, pl))
                    .ifPresent(permissions::addGeoresourcesItem));
        }

        if (resourceType == null || resourceType == ResourceType.INDICATORS) {
            var example2 = new MetadataIndicatorsEntity();
            example2.setPermissions(examplePermissions);
            authHelperService.getIndicatorRepository()
                .findBy(Example.of(example2), q -> q.all())
                .forEach(re ->
                    getPermissionLevel(re, ouEntity)
                    .map(pl -> createElement(re, pl))
                    .ifPresent(permissions::addIndicatorsItem));
        }

        if (resourceType == null || resourceType == ResourceType.SPATIALUNITS) {
            var example3 = new MetadataSpatialUnitsEntity();
            example3.setPermissions(examplePermissions);
            authHelperService.getSpatialunitsRepository()
                .findBy(Example.of(example3), q -> q.all())
                .forEach(re ->
                    getPermissionLevel(re, ouEntity)
                    .map(pl -> createElement(re, pl))
                    .ifPresent(permissions::addSpatialunitsItem));
        }

        if (resourceType == null || resourceType == ResourceType.INDICATORSPATIALUNITS) {
            var example4 = new IndicatorSpatialUnitJoinEntity();
            example4.setPermissions(examplePermissions);
            authHelperService.getIndicatorSpatialunitsRepository()
                .findBy(Example.of(example4), q -> q.all())
                .forEach(re ->
                    getPermissionLevel(re, ouEntity)
                    .map(pl -> {
                        var element = new OrganizationalUnitPermissionOverviewSpatialUnitElementType();
                        element.setId(UUID.fromString(re.getIndicatorMetadataId()));
                        element.setSpatialUnitId(UUID.fromString(re.getSpatialUnitId()));
                        element.setPermissionLevel(pl.getValue());
                        element.setRoleId(null); //TODO keine Ahnung wo die her kommen soll...
                        return element;
                    }).ifPresent(permissions::addIndicatorspatialunitsItem));
        }

        return ou;
    }

    private static OrganizationalUnitPermissionOverviewElementType createElement(AbstractMetadata re, PermissionLevelType pl)  {
        var element = new OrganizationalUnitPermissionOverviewElementType();
        element.setId(UUID.fromString(re.getDatasetId()));
        element.setPermissionLevel(pl.getValue());
        element.setRoleId(null); //TODO keine Ahnung wo die her kommen soll...
        return element;
    }

    private static Optional<PermissionLevelType> getPermissionLevel(
            RestrictedEntity re, OrganizationalUnitEntity ou) {
        return re.getPermissions().stream()
                .filter(p -> p.getOrganizationalUnit().equals(ou) &&
                             p.getPermissionType().equals(PermissionResourceType.RESOURCES))
                .map(PermissionEntity::getPermissionLevel)
                .min(Comparator.comparing(PermissionLevelType::ordinal));
    }

}
