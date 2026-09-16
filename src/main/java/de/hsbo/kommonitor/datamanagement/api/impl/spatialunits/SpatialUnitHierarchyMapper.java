package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMemberType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyOverviewType;

/**
 * Maps {@link SpatialUnitHierarchyEntity} instances to their Swagger/OpenAPI representation.
 */
public class SpatialUnitHierarchyMapper {

    public static SpatialUnitHierarchyOverviewType mapToSwaggerHierarchy(SpatialUnitHierarchyEntity entity) {
        return mapToSwaggerHierarchy(entity, entity.getMemberships());
    }

    /**
     * Maps a hierarchy using an explicitly provided membership list rather than the entity's lazy collection. Used
     * right after a hierarchy's memberships were changed within the same transaction, where the entity's in-memory
     * collection may not yet reflect the persisted rows.
     */
    public static SpatialUnitHierarchyOverviewType mapToSwaggerHierarchy(SpatialUnitHierarchyEntity entity,
            List<SpatialUnitHierarchyMembershipEntity> memberships) {
        SpatialUnitHierarchyOverviewType overview = new SpatialUnitHierarchyOverviewType();
        overview.setHierarchyId(entity.getId());
        overview.setName(entity.getName());
        if (entity.getMandant() != null) {
            overview.setMandantId(entity.getMandant().getOrganizationalUnitId());
        }
        overview.setIsPublic(entity.isPublic());
        overview.setMembers(mapToMembers(memberships));
        return overview;
    }

    public static List<SpatialUnitHierarchyOverviewType> mapToSwaggerHierarchies(List<SpatialUnitHierarchyEntity> entities) {
        List<SpatialUnitHierarchyOverviewType> result = new ArrayList<>(entities.size());
        for (SpatialUnitHierarchyEntity entity : entities) {
            result.add(mapToSwaggerHierarchy(entity));
        }
        return result;
    }

    private static List<SpatialUnitHierarchyMemberType> mapToMembers(List<SpatialUnitHierarchyMembershipEntity> memberships) {
        if (memberships == null) {
            return new ArrayList<>();
        }
        return memberships.stream()
                .sorted(Comparator.comparing(SpatialUnitHierarchyMembershipEntity::getHierarchyLevel,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(m -> {
                    SpatialUnitHierarchyMemberType member = new SpatialUnitHierarchyMemberType();
                    member.setSpatialUnitId(m.getSpatialUnit().getDatasetId());
                    member.setSpatialUnitLevel(m.getSpatialUnit().getDatasetName());
                    member.setHierarchyLevel(m.getHierarchyLevel());
                    if (m.getNextUpperSpatialUnit() != null) {
                        member.setNextUpperSpatialUnitId(m.getNextUpperSpatialUnit().getDatasetId());
                    }
                    if (m.getNextLowerSpatialUnit() != null) {
                        member.setNextLowerSpatialUnitId(m.getNextLowerSpatialUnit().getDatasetId());
                    }
                    return member;
                })
                .collect(Collectors.toList());
    }
}
