package de.hsbo.kommonitor.datamanagement.api.impl.roles;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;


public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
	RolesEntity findByRoleId(String roleId);
    
	RolesEntity findByRoleName(String roleName);
    
    boolean existsByRoleId(String roleId);
    
    boolean existsByRoleName(String roleName);
    
    void deleteByRoleId(String roleId);



}
