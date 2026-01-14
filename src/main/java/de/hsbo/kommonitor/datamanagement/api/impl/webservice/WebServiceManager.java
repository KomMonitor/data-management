package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitManager;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionManager;
import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataWebServicesEntity;
import de.hsbo.kommonitor.datamanagement.auth.provider.AuthInfoProvider;
import de.hsbo.kommonitor.datamanagement.model.*;
import de.hsbo.kommonitor.datamanagement.msg.MessageResolver;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Repository
@Component
public class WebServiceManager {
    private static final Logger LOG = LoggerFactory.getLogger(WebServiceManager.class);

    private static final String MSG_WEBSERVICE_EXISTS_ERROR = "web-service-exists-error";

    @Autowired
    private WebServicesRepository webServicesRepository;

    @Autowired
    private OrganizationalUnitManager orgaManager;

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private MessageResolver messageResolver;

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

    private MetadataWebServicesEntity createMetadata(WebServiceCreationType data) throws Exception {
        LOG.info("Trying to add web service metadata entry.");
        MetadataWebServicesEntity entity = WebServiceMapper.mapToWebServicesEntity(data);

        entity.setPermissions(permissionManager.retrievePermissions(data.getPermissions()));
        entity.setOwner(orgaManager.getOrganizationalUnitEntity(data.getOwnerId()));

        // persist in db
        webServicesRepository.saveAndFlush(entity);
        LOG.info("Completed to add web service metadata entry with id {}.",  entity.getId());

        return entity;
    }


    public WebServiceOverviewType addWebService(WebServiceCreationType webServiceType) throws Exception {
        String metadataId = null;
        try {
            String title = webServiceType.getTitle();
            LOG.info("Trying to persist web service with title '{}'", title);

            if (webServicesRepository.existsByTitle(title)) {
                MetadataWebServicesEntity existingWebService = webServicesRepository.findByTitle(title);
                LOG.error(
                        "The web service with title '{}' already exists. Thus aborting add web service request.", title);
                String errMsg = messageResolver.getMessage(MSG_WEBSERVICE_EXISTS_ERROR);
                throw new Exception(String.format(errMsg, title, existingWebService.getOwner().getMandant().getName()));
            }

            MetadataWebServicesEntity existingWebService = createMetadata(webServiceType);
            metadataId = existingWebService.getId();

            return WebServiceMapper.mapToSwaggerWebService(existingWebService);
        } catch (Exception e) {
            LOG.error("Error while creating web service.", e);
            LOG.info("Deleting partially created resources");
            try {
                LOG.info("Delete metadata entry if exists for id '{}'", metadataId);
                if (metadataId != null) {
                    if (webServicesRepository.existsById(metadataId))
                        webServicesRepository.deleteById(metadataId);
                }
            } catch (Exception e2) {
                LOG.error("Error while deleting partially created web service.", e2);
                throw e;
            }
            throw e;
        }
    }

    public boolean deleteWebServiceDatasetById(String webServiceId) throws ResourceNotFoundException {
        LOG.info("Trying to delete web service dataset with datasetId '{}'", webServiceId);
        if (webServicesRepository.existsById(webServiceId)) {
            boolean success = true;

            MetadataWebServicesEntity webServicesEntity = webServicesRepository.findById(webServiceId);

            // delete any linked roles first
            try {
                webServicesEntity = removeAnyLinkedRoles(webServicesEntity);
            } catch (Exception e) {
                LOG.error("Error while deleting roles for georesource with id {}", webServiceId, e);
            }

            // remove user favorites
            try {
                MetadataWebServicesEntity webServicesMetadata = webServicesRepository.findById(webServiceId);
                webServicesEntity.getUserFavorites().forEach(u -> u.removeWebServiceFavourite(webServicesMetadata));
                webServicesRepository.saveAndFlush(webServicesEntity);
            } catch (Exception e) {
                LOG.error("Error while deleting user favorites for web services", e);
            }

            try {
                webServicesRepository.deleteById(webServiceId);
            } catch (Exception e) {
                LOG.error("Error while deleting metadata entry for web services with id {}", webServiceId, e);
                success = false;
            }
            return success;
        } else {
            LOG.error(
                    "No web service dataset with ID '{}' was found in database. Delete request has no effect.", webServiceId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to delete web service dataset, but no dataset exists with ID " + webServiceId);
        }
    }

    private MetadataWebServicesEntity removeAnyLinkedRoles(MetadataWebServicesEntity webServicesEntity) {
        webServicesEntity.setPermissions(new ArrayList<>());
        webServicesRepository.saveAndFlush(webServicesEntity);
        return webServicesRepository.findById(webServicesEntity.getId());
    }

    public WebServiceOverviewType getWebServiceById(String webServiceId) throws ResourceNotFoundException {
        return getWebServiceById(webServiceId, null);
    }

    public WebServiceOverviewType getWebServiceById(String webServiceId, AuthInfoProvider provider) throws ResourceNotFoundException {
        LOG.info("Retrieving web service metadata for ID '{}'", webServiceId);

        MetadataWebServicesEntity webServicesEntity = webServicesRepository.findById(webServiceId);

        if (provider == null) {
            if (webServicesEntity == null || !webServicesEntity.isPublic()) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", webServiceId));
            }
        } else {
            if (webServicesEntity == null || !provider.checkPermissions(webServicesEntity, PermissionLevelType.VIEWER)) {
                throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", webServiceId));
            }
            try {
                webServicesEntity.setUserPermissions(provider.getPermissions(webServicesEntity));
            } catch (NoSuchElementException ex) {
                LOG.error("No permissions found for web service '{}'", webServicesEntity.getId());
            }
        }
        return WebServiceMapper.mapToSwaggerWebService(webServicesEntity);
    }

    public List<PermissionLevelType> getWebServicePermissionsById(String webServiceId, AuthInfoProvider provider) throws ResourceNotFoundException {
        LOG.info("Retrieving web Service permissions for datasetId '{}'", webServiceId);

        MetadataWebServicesEntity webServicesEntity = webServicesRepository.findById(webServiceId);

        if (webServicesEntity == null || !provider.checkPermissions(webServicesEntity, PermissionLevelType.VIEWER)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), String.format("The requested resource '%s' was not found.", webServiceId));
        }

        return provider.getPermissions(webServicesEntity);
    }

    public String updateMetadata(WebServiceType metadata, String webServiceId) throws ResourceNotFoundException {
        LOG.info("Trying to update web service metadata with ID '{}'", webServiceId);

        if (webServicesRepository.existsById(webServiceId)) {
            MetadataWebServicesEntity metadataEntity = webServicesRepository.findById(webServiceId);

            updateMetadata(metadata, metadataEntity);

            webServicesRepository.saveAndFlush(metadataEntity);
            return webServiceId;
        } else {
            LOG.error(
                    "No web service dataset with ID '{}' was found in database. Update web service request has no effect.",
                    webServiceId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update web service metadata, but no dataset exists with datasetId " + webServiceId);
        }
    }

    public String updateOwnership(OwnerInputType ownerData, String webServiceId) throws ResourceNotFoundException {
        LOG.info("Trying to update web service ownership for ID '{}'", webServiceId);
        if (webServicesRepository.existsById(webServiceId)) {
            MetadataWebServicesEntity metadataEntity = webServicesRepository.findById(webServiceId);

            metadataEntity.setOwner(orgaManager.getOrganizationalUnitEntity(ownerData.getOwnerId()));

            webServicesRepository.saveAndFlush(metadataEntity);
            return webServiceId;
        } else {
            LOG.error("No web service dataset with ID '{}' was found in database. Update ownership request has no effect.", webServiceId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update web service ownership, but no dataset exists with ID " + webServiceId);
        }
    }

    public String updatePermissions(PermissionLevelInputType permissionLevelInput, String webServiceId) throws ResourceNotFoundException {
        LOG.info("Trying to update web service permissions for ID '{}'", webServiceId);
        if (webServicesRepository.existsById(webServiceId)) {
            MetadataWebServicesEntity metadataEntity = webServicesRepository.findById(webServiceId);

            metadataEntity.setPermissions(permissionManager.retrievePermissions(permissionLevelInput.getPermissions()));
            metadataEntity.setPublic(permissionLevelInput.getIsPublic());

            webServicesRepository.saveAndFlush(metadataEntity);
            return webServiceId;
        } else {
            LOG.error(
                    "No web service dataset with ID '{}' was found in database. Update permissions request has no effect.",
                    webServiceId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
                    "Tried to update web service permissions, but no dataset exists with ID " + webServiceId);
        }
    }

    private void updateMetadata(WebServiceType metadata, MetadataWebServicesEntity entity) {
        entity.setTitle(metadata.getTitle());
        entity.setContact(metadata.getContact());
        entity.setTopicReference(metadata.getTopicReference());
        entity.setDescription(metadata.getDescription());
        entity.setNote(metadata.getNote());
        entity.setDataSource(metadata.getDatasource());
        entity.setDataBasis(metadata.getDatabasis());
        entity.setServiceResource(metadata.getServiceResource());
        if (metadata.getConnectionDetails() != null) {
            if (entity.getConnectionDetails() instanceof WmsConnectionDetailsEntity w) {
                w.setBaseUrl(metadata.getConnectionDetails().getBaseUrl());
                w.setLayerName(metadata.getConnectionDetails().getLayerName());
                entity.setConnectionDetails(w);
            }
        }
    }
}
