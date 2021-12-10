package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationalUnitRepository extends JpaRepository<OrganizationalUnitEntity, Long> {

    OrganizationalUnitEntity findByOrganizationalUnitId(String id);

    boolean existsByOrganizationalUnitId(String id);

    boolean existsByName(String name);

    void deleteByOrganizationalUnitId(String id);

}
