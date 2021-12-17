package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import java.util.List;

import javax.transaction.Transactional;

import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.model.organizations.OrganizationalUnitInputType;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.model.roles.RoleOverviewType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Transactional
@Repository
@Component
public class RolesManager {

    private static final Logger logger = LoggerFactory.getLogger(RolesManager.class);

    @Autowired
    RolesRepository rolesRepo;

    public RoleOverviewType addRole(OrganizationalUnitEntity organizationalUnit,
                                    PermissionLevelType permissionLevel) throws Exception {
        logger.info("Trying to persist role with roleName '{}' and permissionLevel '{}'",
                    organizationalUnit,
                    permissionLevel);

        if (rolesRepo.existsByOrganizationalUnitAndPermissionLevel(
            organizationalUnit, permissionLevel)) {
            logger.error("The role with organizationalUnit '{}' and permissionLevel '{}'already exists. " +
                             "Thus aborting add role request.",
                         organizationalUnit,
                         permissionLevel);
            throw new Exception("role already exists. Aborting add role request.");
        }

        /*
         * ID will be autogenerated by JPA / Hibernate
         */
        RolesEntity role = new RolesEntity();
        role.setOrganizationalUnit(organizationalUnit);
        role.setPermissionLevel(permissionLevel);
        rolesRepo.saveAndFlush(role);
        return getRoleById(role.getRoleId());
    }

    public RoleOverviewType getRoleById(String roleId) {
        logger.info("Retrieving role for roleId '{}'", roleId);

        RolesEntity roleEntity = rolesRepo.findByRoleId(roleId);
        RoleOverviewType role = AccessControlMapper.mapToSwaggerRole(roleEntity);

        return role;
    }

    public List<RoleOverviewType> getRoles() {
        logger.info("Retrieving all roles from db");

        List<RolesEntity> roleEntities = rolesRepo.findAll();
        List<RoleOverviewType> roles = AccessControlMapper.mapToSwaggerRoles(roleEntities);

        return roles;
    }

}
