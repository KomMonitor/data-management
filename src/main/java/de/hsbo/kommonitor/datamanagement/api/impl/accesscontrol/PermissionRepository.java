package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    PermissionEntity findByPermissionId(String id);
    
    PermissionEntity findByName(String name);
    
    PermissionEntity findByOrganizationalUnitAndPermissionLevel(OrganizationalUnitEntity ou, PermissionLevelType level);

    boolean existsByRoleId(String roleId);

    boolean existsByOrganizationalUnitAndPermissionLevel(OrganizationalUnitEntity ou, PermissionLevelType level);

    void deleteByRoleId(String roleId);
}
