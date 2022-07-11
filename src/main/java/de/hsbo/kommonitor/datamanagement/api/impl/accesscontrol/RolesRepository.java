package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    RolesEntity findByRoleId(String roleId);    
    
    RolesEntity findByRoleName(String roleName);
    
    RolesEntity findByOrganizationalUnitAndPermissionLevel(OrganizationalUnitEntity ou, PermissionLevelType level);

    boolean existsByRoleId(String roleId);

    boolean existsByOrganizationalUnitAndPermissionLevel(OrganizationalUnitEntity ou, PermissionLevelType level);

    void deleteByRoleId(String roleId);
}
