package de.hsbo.kommonitor.datamanagement.api.impl.spatialunits;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpatialUnitHierarchyRepository extends JpaRepository<SpatialUnitHierarchyEntity, String> {

    List<SpatialUnitHierarchyEntity> findByMandant_OrganizationalUnitId(String mandantId);

    List<SpatialUnitHierarchyEntity> findByIsPublicTrue();

    boolean existsByNameAndMandant_OrganizationalUnitId(String name, String mandantId);
}
