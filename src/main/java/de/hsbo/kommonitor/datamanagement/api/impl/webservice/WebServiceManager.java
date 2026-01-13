package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitManager;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionManager;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataWebServicesEntity;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.model.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Repository
@Component
public class WebServiceManager {
    private static final Logger LOG = LoggerFactory.getLogger(WebServiceManager.class);

    @Autowired
    private WebServicesRepository webServicesRepository;

    @Autowired
    private OrganizationalUnitManager orgaManager;

    @Autowired
    private PermissionManager permissionManager;

    public List<WebServiceOverviewType> getAllWebServicesMetadata() throws Exception {
        LOG.info("Retrieving all public web services metadata from db");

        List<MetadataWebServicesEntity> metadataWebServicesEntities = webServicesRepository.findAll().stream()
                .filter(MetadataWebServicesEntity::isPublic)
                .toList();

        return getWebServicesOverview(metadataWebServicesEntities);
    }

    public List<WebServiceOverviewType> getAllWebServicesMetadata(AuthInfoProvider provider) throws Exception {
        LOG.info("Retrieving secured web services metadata from db");

        List<MetadataWebServicesEntity> metadataWebServicesEntities =  fetchWebServicesMetadataEntities(provider);

        return getWebServicesOverview(metadataWebServicesEntities);
    }

    private List<WebServiceOverviewType> getWebServicesOverview(List<MetadataWebServicesEntity> metadataWebServicesEntities) {
        List<WebServiceOverviewType> webServices = WebServiceMapper.mapToSwaggerWebServices(metadataWebServicesEntities);
        webServices.sort(Comparator.comparing(WebServiceOverviewType::getTitle));
        return WebServiceMapper.mapToSwaggerWebServices(metadataWebServicesEntities);
    }

    private List<MetadataWebServicesEntity> fetchWebServicesMetadataEntities(AuthInfoProvider provider) {
        List<MetadataWebServicesEntity> georesourcesMeatadataEntities = webServicesRepository.findAll().stream()
                .filter(g -> provider.checkPermissions(g, PermissionLevelType.VIEWER))
                .collect(Collectors.toList());

        Iterator<MetadataWebServicesEntity> iter = georesourcesMeatadataEntities.iterator();
        while(iter.hasNext()) {
            MetadataWebServicesEntity m = iter.next();
            try {
                m.setUserPermissions(provider.getPermissions(m));
            } catch(NoSuchElementException ex) {
                LOG.error("No permissions found for web service '{}'. Entity will be removed from resulting list.",
                        m.getId());
                iter.remove();
            }
        }
        return georesourcesMeatadataEntities;
    }

    private MetadataWebServicesEntity createMetadata(WebServiceType data) throws Exception {
        LOG.info("Trying to add web service metadata entry.");
        MetadataWebServicesEntity entity = WebServiceMapper.mapToWebServicesEntity(data);

        entity.setPermissions(permissionManager.retrievePermissions(data.getPermissions()));
        entity.setOwner(orgaManager.getOrganizationalUnitEntity(data.getOwnerId()));

        // persist in db
        webServicesRepository.saveAndFlush(entity);
        LOG.info("Completed to add web service metadata entry with id {}.",  entity.getId());

        return entity;
    }


}
