package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.*;
import java.util.stream.Collectors;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ValidationException;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.msg.MessageResolver;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.model.*;

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

    private static final String MSG_INVALID_HIERARCHY_NEIGHBOURS_ERROR = "invalid-hierarchy-neighbours-error";

    @Autowired
    private SpatialUnitHierarchyRepository hierarchyRepository;

    @Autowired
    private SpatialUnitHierarchyMembershipRepository membershipRepository;

    @Autowired
    private SpatialUnitsMetadataRepository spatialUnitsMetadataRepo;

    @Autowired
    private OrganizationalUnitManager orgaManager;

    @Autowired
    private MessageResolver messageResolver;

    /**
     * Creates a new spatial unit hierarchy owned by a mandant.
     *
     * @param input definition of the hierarchy to create
     * @return representation of the created spatial unit hierarchy
     * @throws ResourceNotFoundException if the referenced organizational unit does not exist or is not a mandant
     */
    public SpatialUnitHierarchyOverviewType addHierarchy(SpatialUnitHierarchyInputType input) throws ResourceNotFoundException {
        OrganizationalUnitEntity mandant = orgaManager.getOrganizationalUnitEntity(input.getMandantId());
        if (!mandant.isMandant()) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                    "Organizational unit '" + mandant.getOrganizationalUnitId() + "' is not a mandant and cannot own a spatial unit hierarchy.");
        }

        SpatialUnitHierarchyEntity entity = new SpatialUnitHierarchyEntity();
        entity.setName(input.getName());
        entity.setMandant(mandant);
        entity.setPublic(Boolean.TRUE.equals(input.getIsPublic()));

        entity = hierarchyRepository.save(entity);
        logger.info("Created spatial unit hierarchy '{}' for mandant '{}'.", entity.getId(), mandant.getOrganizationalUnitId());
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(entity);
    }

    /**
     * Retrieves the spatial unit hierarchies the current user is allowed to access. Without an authentication provider
     * only public hierarchies are returned, global admins receive all hierarchies and any other user receives the
     * hierarchies of the mandants they belong to.
     *
     * @param provider authentication information of the current user, or {@code null} to retrieve only public hierarchies
     * @return list of accessible spatial unit hierarchies
     */
    public List<SpatialUnitHierarchyOverviewType> getAllHierarchies(AuthInfoProvider provider) {
        logger.info("Retrieving all hierarchies from db");

        List<SpatialUnitHierarchyEntity> hierarchyEntities;

        if (provider == null) {
            hierarchyEntities = hierarchyRepository.findByIsPublicTrue();
        } else if (provider.hasGlobalAdminPermissions()) {
            logger.debug("User is global admin - retrieving all hierarchies from DB");
            hierarchyEntities = hierarchyRepository.findAll();
        }
        else {
            hierarchyEntities = hierarchyRepository.findAll().stream()
                    .filter(s -> orgaManager.belongsToMandant(s.getMandant(), provider))
                    .collect(Collectors.toList());
        }

        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchies(hierarchyEntities);
    }

    /**
     * Retrieves all spatial unit hierarchies owned by the given mandant.
     *
     * @param mandantId ID of the mandant (organizational unit) that owns the hierarchies
     * @return list of the mandant's spatial unit hierarchies
     */
    public List<SpatialUnitHierarchyOverviewType> getHierarchiesForMandant(String mandantId) {
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchies(
                hierarchyRepository.findByMandant_OrganizationalUnitId(mandantId));
    }

    /**
     * Retrieves a single spatial unit hierarchy including its ordered members.
     *
     * @param hierarchyId ID of the spatial unit hierarchy
     * @return representation of the spatial unit hierarchy
     * @throws ResourceNotFoundException if no hierarchy exists with the given id
     */
    public SpatialUnitHierarchyOverviewType getHierarchy(String hierarchyId) throws ResourceNotFoundException {
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(getHierarchyEntity(hierarchyId));
    }

    /**
     * Updates the metadata (name, owning mandant and public flag) of an existing spatial unit hierarchy.
     *
     * @param hierarchyId ID of the spatial unit hierarchy to update
     * @param input the new hierarchy metadata
     * @return representation of the updated spatial unit hierarchy
     * @throws ResourceNotFoundException if the hierarchy does not exist or the referenced organizational unit is not a mandant
     */
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
        if (input.getIsPublic() != null) {
            entity.setPublic(input.getIsPublic());
        }
        entity = hierarchyRepository.save(entity);
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(entity);
    }

    /**
     * Retrieves all publicly accessible spatial unit hierarchies.
     *
     * @return list of public spatial unit hierarchies
     */
    public List<SpatialUnitHierarchyOverviewType> getPublicHierarchies() {
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchies(hierarchyRepository.findByIsPublicTrue());
    }

    /**
     * Retrieves a single publicly accessible spatial unit hierarchy including its ordered members.
     *
     * @param hierarchyId ID of the spatial unit hierarchy
     * @return representation of the public spatial unit hierarchy
     * @throws ResourceNotFoundException if no hierarchy exists with the given id or the hierarchy is not public
     */
    public SpatialUnitHierarchyOverviewType getPublicHierarchy(String hierarchyId) throws ResourceNotFoundException {
        SpatialUnitHierarchyEntity entity = getHierarchyEntity(hierarchyId);
        if (!entity.isPublic()) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "No public spatial unit hierarchy exists with id " + hierarchyId);
        }
        return SpatialUnitHierarchyMapper.mapToSwaggerHierarchy(entity);
    }

    /**
     * Deletes a spatial unit hierarchy. The spatial units that were members of the hierarchy are not deleted.
     *
     * @param hierarchyId ID of the spatial unit hierarchy to delete
     * @throws ResourceNotFoundException if no hierarchy exists with the given id
     */
    public void deleteHierarchy(String hierarchyId) throws ResourceNotFoundException {
        SpatialUnitHierarchyEntity entity = getHierarchyEntity(hierarchyId);
        hierarchyRepository.delete(entity);
        logger.info("Deleted spatial unit hierarchy '{}'.", hierarchyId);
    }

    /**
     * Replaces the full ordered list of members of a hierarchy. Covers reordering, removing and adding members within
     * the hierarchy in a single operation. The members are ordered by the requested hierarchyLevel and then normalized
     * so that level and neighbouring spatial units are coherent.
     *
     * @param hierarchyId ID of a spatial unit hierarchy
     * @param members list of members of a hierarchy
     * @return representation of the ordered spatial unit hierarchy
     * @throws ResourceNotFoundException if the requested spatial unit or one of the hierarchies does not exist or
     * if the spatial unit and a hierarchy do not belong to the same mandant.
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
     * Replaces the full set of hierarchy memberships of an existing spatial unit using integer levels. Covers placing
     * the spatial unit into further hierarchies, changing its level within a hierarchy and removing it from a hierarchy
     * in a single operation. A {@code null} list clears all memberships. Affected hierarchies are renormalized so
     * that level and neighbouring spatial units stay coherent.
     *
     * @param spatialUnitId ID of the spatial unit for which the hierarchy memberships should be updated
     * @param memberships List of hierarchy membership definitions
     * @throws ResourceNotFoundException if the requested spatial unit or one of the hierarchies does not exist or
     * if the spatial unit and a hierarchy do not belong to the same mandant.
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
     * Places a newly registered spatial unit into hierarchies by defining, for each hierarchy, its neighboring
     * spatial units (next upper / next lower). The integer level is derived from that placement so both representations
     * stay coherent. Replaces any existing memberships of the spatial unit. A {@code null} list clears all memberships.
     *
     * @param spatialUnitId ID of the spatial unit that is registered
     * @param memberships definition of hierarchy memberships
     * @throws ResourceNotFoundException if spatial unit and hierarchy do not belong to the same mandant
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
                validateNeighbourMembership(hierarchy, membership.getNextUpperSpatialUnitId(), spatialUnitId);
                validateNeighbourMembership(hierarchy, membership.getNextLowerSpatialUnitId(), spatialUnitId);

                List<SpatialUnitHierarchyMembershipEntity> ordered = loadOrderedMembers(hierarchy.getId());
                int index = getInsertIndexByNeighbours(ordered, hierarchy, membership.getNextUpperSpatialUnitId(),
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
     * Removes a spatial unit from all hierarchies it belongs to and renormalizes those hierarchies afterwards, so that
     * the membership levels and neighbouring spatial units of the remaining members stay coherent. Intended to be
     * called when a spatial unit is deleted.
     *
     * @param spatialUnitId ID of the spatial unit that is being deleted
     */
    public void removeSpatialUnitFromAllHierarchies(String spatialUnitId) {
        Set<String> affectedHierarchyIds = collectHierarchyIds(spatialUnitId);
        if (affectedHierarchyIds.isEmpty()) {
            return;
        }

        membershipRepository.deleteBySpatialUnit_DatasetId(spatialUnitId);
        membershipRepository.flush();

        for (String hierarchyId : affectedHierarchyIds) {
            recomputeOrdering(loadOrderedMembers(hierarchyId));
        }
        logger.info("Removed spatial unit '{}' from {} hierarchies and renormalized their membership ordering.",
                spatialUnitId, affectedHierarchyIds.size());
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
     * Normalizes an ordered list of memberships (index 0 = top level) so that both representations of the ordering are
     * coherent: hierarchyLevel is set to the position and the nextUpper/nextLower spatial units are set to the adjacent
     * members (null at the top/bottom ends). Persists all memberships.
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

    /**
     * Determines the insertion index for a newly registered spatial unit within an ordered
     * hierarchy from its requested neighbours:
     * <ul>
     *   <li>if neither neighbour is given, the unit is appended at the last (bottom) position;</li>
     *   <li>if only one neighbour is given, the unit is placed directly adjacent to it;</li>
     *   <li>if both are given, they must currently be direct neighbours (next lower directly
     *       below next upper) and the unit is placed between them - otherwise a
     *       {@link ValidationException} is thrown.</li>
     * </ul>
     */
    private int getInsertIndexByNeighbours(List<SpatialUnitHierarchyMembershipEntity> ordered,
                                           SpatialUnitHierarchyEntity hierarchy, String nextUpperId, String nextLowerId) {
        boolean hasUpper = nextUpperId != null && !nextUpperId.isBlank();
        boolean hasLower = nextLowerId != null && !nextLowerId.isBlank();

        if (!hasUpper && !hasLower) {
            return ordered.size();
        }

        Integer upperIndex = hasUpper ? indexOfMember(ordered, nextUpperId) : null;
        Integer lowerIndex = hasLower ? indexOfMember(ordered, nextLowerId) : null;

        if (hasUpper && hasLower) {
            // both neighbours must currently be adjacent (next lower directly below next upper)
            if (upperIndex == null || lowerIndex == null || lowerIndex != upperIndex + 1) {
                String message = String.format(
                        messageResolver.getMessage(MSG_INVALID_HIERARCHY_NEIGHBOURS_ERROR),
                        nextUpperId, nextLowerId, hierarchy.getId());
                throw new ValidationException("hierarchies", message);
            }
            return upperIndex + 1;
        }

        if (hasUpper) {
            return upperIndex + 1;
        }
        return lowerIndex;
    }

    private Integer indexOfMember(List<SpatialUnitHierarchyMembershipEntity> ordered, String spatialUnitId) {
        for (int i = 0; i < ordered.size(); i++) {
            if (ordered.get(i).getSpatialUnit().getDatasetId().equals(spatialUnitId)) {
                return i;
            }
        }
        return null;
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
                                             String spatialUnitId) throws ResourceNotFoundException {
        if (neighbourId == null || neighbourId.isBlank()) {
            return;
        }
        if (neighbourId.equals(spatialUnitId)) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                    "The neighbouring spatial unit of a hierarchy membership must not be the spatial unit itself.");
        }
        if (!membershipRepository.existsByHierarchy_IdAndSpatialUnit_DatasetId(hierarchy.getId(), neighbourId)) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST.value(),
                    "The neighbouring spatial unit '" + neighbourId + "' is not a member of hierarchy '"
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
