package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpatialUnitHierarchyMembershipRepository extends JpaRepository<SpatialUnitHierarchyMembershipEntity, String> {

    List<SpatialUnitHierarchyMembershipEntity> findByHierarchy_Id(String hierarchyId);

    List<SpatialUnitHierarchyMembershipEntity> findBySpatialUnit_DatasetId(String spatialUnitId);

    boolean existsByHierarchy_IdAndSpatialUnit_DatasetId(String hierarchyId, String spatialUnitId);

    void deleteBySpatialUnit_DatasetId(String spatialUnitId);

    void deleteByHierarchy_Id(String hierarchyId);
}
