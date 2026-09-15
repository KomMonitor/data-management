package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyInputType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMemberInputType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMembershipInputType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyMembershipPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.SpatialUnitHierarchyOverviewType;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Manages mandant-owned spatial unit hierarchies and the ordered membership of spatial units within them.
 */
@Transactional
@Component
public class SpatialUnitHierarchyManager {

    private static final Logger logger = LoggerFactory.getLogger(SpatialUnitHierarchyManager.class);

    @Autowired
    private SpatialUnitHierarchyRepository hierarchyRepository;

    @Autowired
    private SpatialUnitHierarchyMembershipRepository membershipRepository;

    @Autowired
    private SpatialUnitsMetadataRepository spatialUnitsMetadataRepo;

    @Autowired
    private OrganizationalUnitManager orgaManager;

    public SpatialUnitHierarchyOverviewType addHierarchy(SpatialUnitHierarchyInputType input) throws ResourceNotFoundException {
        OrganizationalUnitEntity mandant = orgaManager.getOrganizationalUnitEntity(input.getMandantId());
        if (!mandant.isMandant()) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                    "Organizational unit '" + mandant.getOrganizationalUnitId() + "' is not a mandant and cannot own a spatial unit hierarchy.");
        }

        SpatialUnitHierarchyEntity entity = new SpatialUnitHierarchyEntity();
        entity.setName(input.getName());
        entity.setMandant(mandant);

        entity = hierarchyRepository.save(entity);
        logger.info("Created spatial unit hierarchy '{}' for mandant '{}'.", entity.getId(), mandant.getOrganizationalUnitId());
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(entity);
    }

    public List<SpatialUnitHierarchyOverviewType> getAllHierarchies() {
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchies(hierarchyRepository.findAll());
    }

    public List<SpatialUnitHierarchyOverviewType> getHierarchiesForMandant(String mandantId) {
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchies(
                hierarchyRepository.findByMandant_OrganizationalUnitId(mandantId));
    }

    public SpatialUnitHierarchyOverviewType getHierarchy(String hierarchyId) throws ResourceNotFoundException {
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(getHierarchyEntity(hierarchyId));
    }

    public SpatialUnitHierarchyOverviewType updateHierarchy(String hierarchyId, SpatialUnitHierarchyInputType input) throws ResourceNotFoundException {
        SpatialUnitHierarchyEntity entity = getHierarchyEntity(hierarchyId);
        entity.setName(input.getName());
        if (input.getMandantId() != null) {
            OrganizationalUnitEntity mandant = orgaManager.getOrganizationalUnitEntity(input.getMandantId());
            if (!mandant.isMandant()) {
                throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                        "Organizational unit '" + mandant.getOrganizationalUnitId() + "' is not a mandant and cannot own a spatial unit hierarchy.");
            }
            entity.setMandant(mandant);
        }
        entity = hierarchyRepository.save(entity);
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(entity);
    }

    public void deleteHierarchy(String hierarchyId) throws ResourceNotFoundException {
        SpatialUnitHierarchyEntity entity = getHierarchyEntity(hierarchyId);
        hierarchyRepository.delete(entity);
        logger.info("Deleted spatial unit hierarchy '{}'.", hierarchyId);
    }

    /**
     * Replaces the full ordered list of members of a hierarchy. Covers reordering,
     * removing and adding members within the hierarchy in a single operation. The members
     * are ordered by the requested hierarchyLevel and then normalized so that level and
     * neighbouring spatial units are coherent.
     */
    public SpatialUnitHierarchyOverviewType updateHierarchyMembers(String hierarchyId, List<SpatialUnitHierarchyMemberInputType> members)
            throws ResourceNotFoundException {
        SpatialUnitHierarchyEntity hierarchy = getHierarchyEntity(hierarchyId);

        membershipRepository.deleteByHierarchy_Id(hierarchyId);
        membershipRepository.flush();

        List<SpatialUnitHierarchyMembershipEntity> ordered = new ArrayList<>();
        if (members != null) {
            List<SpatialUnitHierarchyMemberInputType> sortedMembers = new ArrayList<>(members);
            sortedMembers.sort(Comparator.comparing(SpatialUnitHierarchyMemberInputType::getHierarchyLevel,
                    Comparator.nullsLast(Comparator.naturalOrder())));
            for (SpatialUnitHierarchyMemberInputType member : sortedMembers) {
                MetadataSpatialUnitsEntity spatialUnit = getSpatialUnitEntity(member.getSpatialUnitId());
                validateSameMandant(spatialUnit, hierarchy);
                ordered.add(newMembership(hierarchy, spatialUnit));
            }
        }
        recomputeOrdering(ordered);

        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(getHierarchyEntity(hierarchyId));
    }

    /**
     * Replaces the full set of hierarchy memberships of an existing spatial unit using
     * integer levels. Covers placing the spatial unit into further hierarchies, changing
     * its level within a hierarchy and removing it from a hierarchy in a single operation.
     * A {@code null} list clears all memberships. Affected hierarchies are renormalized so
     * that level and neighbouring spatial units stay coherent.
     */
    public void updateSpatialUnitMemberships(String spatialUnitId, List<SpatialUnitHierarchyMembershipInputType> memberships)
            throws ResourceNotFoundException {
        MetadataSpatialUnitsEntity spatialUnit = getSpatialUnitEntity(spatialUnitId);

        Set<String> affectedHierarchyIds = collectHierarchyIds(spatialUnitId);
        membershipRepository.deleteBySpatialUnit_DatasetId(spatialUnitId);
        membershipRepository.flush();

        Set<String> targetHierarchyIds = new HashSet<>();
        if (memberships != null) {
            for (SpatialUnitHierarchyMembershipInputType membership : memberships) {
                SpatialUnitHierarchyEntity hierarchy = getHierarchyEntity(membership.getHierarchyId());
                validateSameMandant(spatialUnit, hierarchy);

                List<SpatialUnitHierarchyMembershipEntity> ordered = loadOrderedMembers(hierarchy.getId());
                int index = insertIndexByLevel(ordered, membership.getHierarchyLevel());
                ordered.add(index, newMembership(hierarchy, spatialUnit));
                recomputeOrdering(ordered);

                affectedHierarchyIds.add(hierarchy.getId());
                targetHierarchyIds.add(hierarchy.getId());
            }
        }
        renormalizeRemovedFrom(affectedHierarchyIds, targetHierarchyIds);
    }

    /**
     * Places a newly registered spatial unit into hierarchies by defining, for each
     * hierarchy, its neighbouring spatial units (next upper / next lower). The integer
     * level is derived from that placement so both representations stay coherent. Replaces
     * any existing memberships of the spatial unit. A {@code null} list clears all memberships.
     */
    public void createMembershipsFromRegistration(String spatialUnitId, List<SpatialUnitHierarchyMembershipPOSTInputType> memberships)
            throws ResourceNotFoundException {
        MetadataSpatialUnitsEntity spatialUnit = getSpatialUnitEntity(spatialUnitId);

        Set<String> affectedHierarchyIds = collectHierarchyIds(spatialUnitId);
        membershipRepository.deleteBySpatialUnit_DatasetId(spatialUnitId);
        membershipRepository.flush();

        Set<String> targetHierarchyIds = new HashSet<>();
        if (memberships != null) {
            for (SpatialUnitHierarchyMembershipPOSTInputType membership : memberships) {
                SpatialUnitHierarchyEntity hierarchy = getHierarchyEntity(membership.getHierarchyId());
                validateSameMandant(spatialUnit, hierarchy);
                validateNeighbourMembership(hierarchy, membership.getNextUpperSpatialUnitId(), spatialUnitId, "next upper");
                validateNeighbourMembership(hierarchy, membership.getNextLowerSpatialUnitId(), spatialUnitId, "next lower");

                List<SpatialUnitHierarchyMembershipEntity> ordered = loadOrderedMembers(hierarchy.getId());
                int index = insertIndexByNeighbours(ordered, membership.getNextUpperSpatialUnitId(),
                        membership.getNextLowerSpatialUnitId());
                ordered.add(index, newMembership(hierarchy, spatialUnit));
                recomputeOrdering(ordered);

                affectedHierarchyIds.add(hierarchy.getId());
                targetHierarchyIds.add(hierarchy.getId());
            }
        }
        renormalizeRemovedFrom(affectedHierarchyIds, targetHierarchyIds);
    }

    /**
     * Renormalizes the ordering of hierarchies the spatial unit was removed from (i.e.
     * affected but no longer a target), closing the gap left behind.
     */
    private void renormalizeRemovedFrom(Set<String> affectedHierarchyIds, Set<String> targetHierarchyIds) {
        for (String hierarchyId : affectedHierarchyIds) {
            if (!targetHierarchyIds.contains(hierarchyId)) {
                recomputeOrdering(loadOrderedMembers(hierarchyId));
            }
        }
    }

    /**
     * Normalizes an ordered list of memberships (index 0 = top level) so that both
     * representations of the ordering are coherent: hierarchyLevel is set to the position
     * and the nextUpper/nextLower spatial units are set to the adjacent members (null at
     * the top/bottom ends). Persists all memberships.
     */
    private void recomputeOrdering(List<SpatialUnitHierarchyMembershipEntity> ordered) {
        for (int i = 0; i < ordered.size(); i++) {
            SpatialUnitHierarchyMembershipEntity membership = ordered.get(i);
            membership.setHierarchyLevel(i);
            membership.setNextUpperSpatialUnit(i > 0 ? ordered.get(i - 1).getSpatialUnit() : null);
            membership.setNextLowerSpatialUnit(i < ordered.size() - 1 ? ordered.get(i + 1).getSpatialUnit() : null);
        }
        membershipRepository.saveAll(ordered);
        membershipRepository.flush();
    }

    private List<SpatialUnitHierarchyMembershipEntity> loadOrderedMembers(String hierarchyId) {
        List<SpatialUnitHierarchyMembershipEntity> members = new ArrayList<>(membershipRepository.findByHierarchy_Id(hierarchyId));
        members.sort(Comparator.comparing(SpatialUnitHierarchyMembershipEntity::getHierarchyLevel,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return members;
    }

    private int insertIndexByLevel(List<SpatialUnitHierarchyMembershipEntity> ordered, Integer targetLevel) {
        if (targetLevel == null || targetLevel > ordered.size()) {
            return ordered.size();
        }
        return Math.max(targetLevel, 0);
    }

    private int insertIndexByNeighbours(List<SpatialUnitHierarchyMembershipEntity> ordered, String nextUpperId, String nextLowerId) {
        if (nextUpperId != null && !nextUpperId.isBlank()) {
            for (int i = 0; i < ordered.size(); i++) {
                if (ordered.get(i).getSpatialUnit().getDatasetId().equals(nextUpperId)) {
                    return i + 1;
                }
            }
        }
        if (nextLowerId != null && !nextLowerId.isBlank()) {
            for (int i = 0; i < ordered.size(); i++) {
                if (ordered.get(i).getSpatialUnit().getDatasetId().equals(nextLowerId)) {
                    return i;
                }
            }
        }
        return ordered.size();
    }

    private Set<String> collectHierarchyIds(String spatialUnitId) {
        Set<String> ids = new HashSet<>();
        for (SpatialUnitHierarchyMembershipEntity membership : membershipRepository.findBySpatialUnit_DatasetId(spatialUnitId)) {
            ids.add(membership.getHierarchy().getId());
        }
        return ids;
    }

    private SpatialUnitHierarchyMembershipEntity newMembership(SpatialUnitHierarchyEntity hierarchy,
                                                              MetadataSpatialUnitsEntity spatialUnit) {
        SpatialUnitHierarchyMembershipEntity entity = new SpatialUnitHierarchyMembershipEntity();
        entity.setHierarchy(hierarchy);
        entity.setSpatialUnit(spatialUnit);
        return entity;
    }

    /**
     * Validates that a named neighbouring spatial unit (next upper or next lower) is a
     * member of the given hierarchy and is not the spatial unit itself. A {@code null}
     * or blank neighbour is allowed and denotes the top/bottom of the hierarchy.
     */
    private void validateNeighbourMembership(SpatialUnitHierarchyEntity hierarchy, String neighbourId,
                                             String spatialUnitId, String position) throws ResourceNotFoundException {
        if (neighbourId == null || neighbourId.isBlank()) {
            return;
        }
        if (neighbourId.equals(spatialUnitId)) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                    "The " + position + " spatial unit of a hierarchy membership must not be the spatial unit itself.");
        }
        if (!membershipRepository.existsByHierarchy_IdAndSpatialUnit_DatasetId(hierarchy.getId(), neighbourId)) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                    "The " + position + " spatial unit '" + neighbourId + "' is not a member of hierarchy '"
                            + hierarchy.getId() + "'. Neighbouring spatial units must already be members of the same hierarchy.");
        }
    }

    private void validateSameMandant(MetadataSpatialUnitsEntity spatialUnit, SpatialUnitHierarchyEntity hierarchy)
            throws ResourceNotFoundException {
        String spatialUnitMandantId = spatialUnit.getMandant() != null
                ? spatialUnit.getMandant().getOrganizationalUnitId() : null;
        String hierarchyMandantId = hierarchy.getMandant() != null
                ? hierarchy.getMandant().getOrganizationalUnitId() : null;

        if (spatialUnitMandantId == null || !spatialUnitMandantId.equals(hierarchyMandantId)) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                    "Spatial unit '" + spatialUnit.getDatasetId() + "' and hierarchy '" + hierarchy.getId()
                            + "' do not belong to the same mandant. A spatial unit may only be placed into hierarchies of its own mandant.");
        }
    }

    private SpatialUnitHierarchyEntity getHierarchyEntity(String hierarchyId) throws ResourceNotFoundException {
        return hierarchyRepository.findById(hierarchyId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                        "No spatial unit hierarchy exists with id " + hierarchyId));
    }

    private MetadataSpatialUnitsEntity getSpatialUnitEntity(String spatialUnitId) throws ResourceNotFoundException {
        MetadataSpatialUnitsEntity spatialUnit = spatialUnitsMetadataRepo.findByDatasetId(spatialUnitId);
        if (spatialUnit == null) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "No spatial unit exists with id " + spatialUnitId);
        }
        return spatialUnit;
    }
}
