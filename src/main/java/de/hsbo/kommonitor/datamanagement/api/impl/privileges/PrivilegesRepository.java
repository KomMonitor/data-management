package de.hsbo.kommonitor.datamanagement.api.impl.privileges;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.privilege.PrivilegesEntity;


public interface PrivilegesRepository extends JpaRepository<PrivilegesEntity, Long> {
	PrivilegesEntity findByPrivilegeId(String privilegeId);
    
	PrivilegesEntity findByPrivilegeName(String privilegeName);
    
    boolean existsByPrivilegeId(String privilegeId);
    
    boolean existsByPrivilegeName(String privilegeName);
    
    void deleteByPrivilegeId(String privilegeId);

}
