package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationalUnitRepository extends JpaRepository<OrganizationalUnitEntity, Long> {

    OrganizationalUnitEntity findByOrganizationalUnitId(String id);

    OrganizationalUnitEntity findByName(String name);

    boolean existsByOrganizationalUnitId(String id);

    boolean existsByName(String name);

    void deleteByOrganizationalUnitId(String id);

}
