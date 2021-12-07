package de.hsbo.kommonitor.datamanagement.api.impl.roles;

import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    RolesEntity findByRoleId(String roleId);

    boolean existsByRoleId(String roleId);

    RolesEntity findByOrganizationalUnitAndPermissionLevel(String ou, PermissionLevelType level);

    boolean existsByOrganizationalUnitAndPermissionLevel(String ou, PermissionLevelType level);

    void deleteByRoleId(String roleId);

}
