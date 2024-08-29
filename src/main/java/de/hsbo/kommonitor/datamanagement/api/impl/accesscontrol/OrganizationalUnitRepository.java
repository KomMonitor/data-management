package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationalUnitRepository extends JpaRepository<OrganizationalUnitEntity, Long> {

    OrganizationalUnitEntity findByOrganizationalUnitId(String id);

    OrganizationalUnitEntity findByKeycloakId(UUID id);

    OrganizationalUnitEntity findByKeycloakIdAndMandantOrganizationalUnitId(UUID keycloakId, String mandantId);

    OrganizationalUnitEntity findByName(String name);

    boolean existsByOrganizationalUnitId(String id);

    boolean existsByName(String name);

    void deleteByOrganizationalUnitId(String id);

}
