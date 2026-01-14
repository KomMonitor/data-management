package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataWebServicesEntity;
import de.hsbo.kommonitor.datamanagement.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class WebServiceMapper {

    public static List<WebServiceOverviewType> mapToSwaggerWebServices(List<MetadataWebServicesEntity> webServicesEntities) {
        return webServicesEntities.stream()
                .map(WebServiceMapper::mapToSwaggerWebService)
                .collect(Collectors.toList());
    }

    public static WebServiceOverviewType mapToSwaggerWebService(MetadataWebServicesEntity entity) {
        WebServiceOverviewType dataset = new WebServiceOverviewType();
        dataset.setContact(entity.getContact());
        dataset.setDatabasis(entity.getDataBasis());

        dataset.setDatasource(entity.getDataSource());
        dataset.setDescription(entity.getDescription());
        dataset.setId(entity.getId());
        dataset.setNote(entity.getNote());
        dataset.setTitle(entity.getTitle());
        dataset.setTopicReference(entity.getTopicReference());
        dataset.setServiceResource(entity.getServiceResource());

        dataset.setConnectionDetails(mapConnectionDetails(entity.getConnectionDetails()));

        dataset.setPermissions(mapPermissions(entity.getPermissions()));
        dataset.setUserPermissions(entity.getUserPermissions());
        if (entity.getOwner() != null) {
            dataset.setOwnerId(entity.getOwner().getOrganizationalUnitId());
        }
        dataset.setIsPublic(entity.isPublic());
        return dataset;
    }

    public static MetadataWebServicesEntity mapToWebServicesEntity(WebServiceCreationType dataset) {
        MetadataWebServicesEntity entity = new MetadataWebServicesEntity();
        entity.setTitle(dataset.getTitle());
        entity.setContact(dataset.getContact());
        entity.setTopicReference(dataset.getTopicReference());
        entity.setDescription(dataset.getDescription());
        entity.setNote(dataset.getNote());
        entity.setPublic(dataset.getIsPublic());
        entity.setDataSource(dataset.getDatasource());
        entity.setDataBasis(dataset.getDatabasis());
        entity.setServiceResource(dataset.getServiceResource());
        if (dataset.getConnectionDetails() != null) {
            entity.setConnectionDetails(mapConnectionDetails(dataset.getConnectionDetails()));
        }

        return entity;
    }

    private static WmsConnectionInfoType mapConnectionDetails(ConnectionDetailsEntity connectionDetails) {
        WmsConnectionInfoType connectionInfoType = new WmsConnectionInfoType();
        connectionInfoType.setId(connectionDetails.getId());
        connectionInfoType.setServiceType(ServiceTypeEnum.WMS);
        if (connectionDetails instanceof WmsConnectionDetailsEntity w) {
            connectionInfoType.setBaseUrl(w.getBaseUrl());
            connectionInfoType.setLayerName(w.getLayerName());
        }
        return connectionInfoType;
    }

    private static ConnectionDetailsEntity mapConnectionDetails(WmsConnectionInfoType connectionInfo) {
        WmsConnectionDetailsEntity entity = new WmsConnectionDetailsEntity();
        entity.setBaseUrl(connectionInfo.getBaseUrl());
        entity.setLayerName(connectionInfo.getLayerName());

        return entity;
    }

    private static List<String> mapPermissions(HashSet<PermissionEntity> permissions) {
        return permissions
                .stream()
                .map(PermissionEntity::getPermissionId)
                .collect(Collectors.toList());
    }
}
