package de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ApiException;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitInputType;
import de.hsbo.kommonitor.datamanagement.model.OrganizationalUnitOverviewType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
@Component
public class OrganizationalUnitManager {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationalUnitManager.class);

    @Autowired
    OrganizationalUnitRepository organizationalUnitRepository;

    @Autowired
    RolesManager rolesManager;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String defaultAnonymousOUname;

    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String defaultAuthenticatedOUname;

    public OrganizationalUnitOverviewType addOrganizationalUnit(
        OrganizationalUnitInputType inputOrganizationalUnit
    ) throws Exception {
        String name = inputOrganizationalUnit.getName();
        logger.info("Trying to persist OrganizationalUnit with name '{}'", name);

        if (organizationalUnitRepository.existsByName(name)) {
            logger.error(
                "The OrganizationalUnit with name '{}' already exists. Thus aborting add OrganizationalUnit request.",
                name);
            throw new Exception("OrganizationalUnit already exists. Aborting addOrganizationalUnit request.");
        }

        /*
         * ID will be autogenerated by JPA / Hibernate
         */
        OrganizationalUnitEntity jpaUnit = new OrganizationalUnitEntity();
        jpaUnit.setName(name);
        jpaUnit.setContact(inputOrganizationalUnit.getContact());
        jpaUnit.setDescription(inputOrganizationalUnit.getDescription());
        OrganizationalUnitEntity saved = organizationalUnitRepository.saveAndFlush(jpaUnit);

        // Generate appropriate roles
        List<RolesEntity> roles = new ArrayList<>();
        for (PermissionLevelType level : PermissionLevelType.values()) {
            roles.add(rolesManager.addRole(saved, level));
        }
        saved.setRoles(roles);

        return AccessControlMapper.mapToSwaggerOrganizationalUnit(saved);
    }

    public boolean deleteOrganizationalUnitAndRolesById(String organizationalUnitId) throws ApiException {
        OrganizationalUnitEntity unit = organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
        if (unit != null) {
            // Prevent deletion of default units.
            if (unit.getName().equals(defaultAnonymousOUname) || unit.getName().equals(defaultAuthenticatedOUname)) {
                logger.error("Trying to delete default OrganizationalUnits.");
                throw new ApiException(HttpStatus.FORBIDDEN.value(),
                                       "Tried to delete default OrganizationalUnits");
            }

            // This should automatically propagate to associated roles via @CascadeType.REMOVE
            organizationalUnitRepository.deleteByOrganizationalUnitId(organizationalUnitId);
            return true;
        } else {
            logger.error("No OrganizationalUnit with id '{}' was found in database. Delete request has no effect.",
                         organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                                                "Tried to delete OrganizationalUnit, but no OrganizationalUnit " +
                                                    "existes with id " +
                                                    organizationalUnitId);
        }
    }

    public OrganizationalUnitOverviewType getOrganizationalUnitById(String organizationalUnitId) {
        logger.info("Retrieving OrganizationalUnit for organizationalUnitId '{}'", organizationalUnitId);

        OrganizationalUnitEntity OrganizationalUnitEntity =
            organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);
        OrganizationalUnitOverviewType organizationalUnit =
            AccessControlMapper.mapToSwaggerOrganizationalUnit(OrganizationalUnitEntity);

        return organizationalUnit;
    }

    public List<OrganizationalUnitOverviewType> getOrganizationalUnits() {
        logger.info("Retrieving all organizationalUnits from db");

        List<OrganizationalUnitEntity> OrganizationalUnitEntities = organizationalUnitRepository.findAll();
        List<OrganizationalUnitOverviewType> organizationalUnits =
            AccessControlMapper.mapToSwaggerOrganizationalUnits(OrganizationalUnitEntities);

        return organizationalUnits;
    }

    public String updateOrganizationalUnit(OrganizationalUnitInputType newData,
                                           String organizationalUnitId) throws ResourceNotFoundException {
        logger.info("Trying to update OrganizationalUnit with organizationalUnitId '{}'", organizationalUnitId);
        if (organizationalUnitRepository.existsByOrganizationalUnitId(organizationalUnitId)) {
            OrganizationalUnitEntity organizationalUnitEntity =
                organizationalUnitRepository.findByOrganizationalUnitId(organizationalUnitId);

            organizationalUnitEntity.setName(newData.getName());
            organizationalUnitEntity.setContact(newData.getContact());
            organizationalUnitEntity.setDescription(newData.getDescription());

            organizationalUnitRepository.saveAndFlush(organizationalUnitEntity);
            return organizationalUnitEntity.getOrganizationalUnitId();
        } else {
            logger.error("No OrganizationalUnit with id '{}' was found in database. Update request has no effect.",
                         organizationalUnitId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                                                "Tried to update OrganizationalUnit, but no OrganizationalUnit " +
                                                    "exists with id " + organizationalUnitId);
        }
    }

}
