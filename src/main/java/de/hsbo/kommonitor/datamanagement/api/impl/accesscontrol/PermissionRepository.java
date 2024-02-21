package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    PermissionEntity findByPermissionId(String id);

    Optional<PermissionEntity> findByOrganizationalUnitAndPermissionLevelAndPermissionType(OrganizationalUnitEntity ou,
                                                                                           PermissionLevelType level,
                                                                                           String type);

}
