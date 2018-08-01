package de.hsbo.kommonitor.datamanagement.api.impl.webservice.management;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder.ProjectionPolicy;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;

/**
 * Implementation of {@link OGCWebServiceManager} that publishes spatial
 * datasets using an instance of GeoServer
 * 
 * @author CDB
 *
 */
@Component
public class GeoserverManager implements OGCWebServiceManager {

	private static Logger logger = LoggerFactory.getLogger(GeoserverManager.class);

	private static Properties geoserverProps = null;
	
	static{
		try {
			parseResourceFiles();
		} catch (FileNotFoundException e) {
			logger.error("Error while instantiating GeoserverManager.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Error while instantiating GeoserverManager.");
			e.printStackTrace();
		}
	}

	@Override
	public boolean publishDbLayerAsOgcService(String dbTableName, ResourceTypeEnum resourceType)
			throws FileNotFoundException, IOException {

		publishDBLayerInGeoserver(dbTableName, resourceType);
		return true;
	}

	@Override
	public boolean unpublishDbLayer(String dbTableName, ResourceTypeEnum resourceType) throws MalformedURLException {
		GeoServerRESTManager geoserverManager = initializeGeoserverRestManager();

		GeoServerRESTReader reader = geoserverManager.getReader();
		GeoServerRESTPublisher publisher = geoserverManager.getPublisher();
		GeoServerRESTStoreManager storeManager = geoserverManager.getStoreManager();

		String targetWorkspace = geoserverProps.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String targetDatastore = null;
		String targetSchema = null;

		switch (resourceType) {
		case SPATIAL_UNIT:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_SPATIALUNITS);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_SPATIALUNITS);
			break;
		case GEORESOURCE:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
			break;
		case INDICATOR:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_INDICATORS);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_INDICATORS);
			break;

		default:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
			break;
		}
		
		if (reader.existsFeatureType(targetWorkspace, targetDatastore, dbTableName)) {
			logger.info("Removing FeatureType '{}' on geoserver.", dbTableName);
			publisher.unpublishFeatureType(targetWorkspace, targetDatastore, dbTableName);
		}

		if (reader.existsLayer(targetWorkspace, dbTableName)
				|| (reader.getLayer(targetWorkspace, dbTableName) != null)) {
			publisher.removeLayer(targetWorkspace, dbTableName);
			logger.info("Removing Layer '{}' on geoserver", dbTableName);
		}
		return true;
	}

	@Override
	public String getWmsUrl(String dbTableName) {
		String targetWorkspace = geoserverProps.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String wmsUrl = geoserverProps.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/" + dbTableName + "/wms";
		
		logger.info("created WMS URL '{}' for dbTable '{}'" + wmsUrl, dbTableName);
		
		return wmsUrl;
	}

	@Override
	public String getWfsUrl(String dbTableName) {
		String targetWorkspace = geoserverProps.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String wfsUrl = geoserverProps.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/" + dbTableName + "/wfs";
		
		logger.info("created WFS URL '{}' for dbTable '{}'" + wfsUrl, dbTableName);
		
		return wfsUrl;
	}

	@Override
	public String getWcsUrl(String dbTableName) {
		String targetWorkspace = geoserverProps.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String wcsUrl = geoserverProps.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/" + dbTableName + "/wcs";
		
		logger.info("created WCS URL '{}' for dbTable '{}'" + wcsUrl, dbTableName);
		
		return wcsUrl;
	}

	private static void parseResourceFiles() throws IOException, FileNotFoundException {

		if (geoserverProps == null)
			;
		geoserverProps = parseProperties(ResourceFileConstants.geoserverPropertiesFileLocation);
	}

	private static Properties parseProperties(String filename) throws IOException, FileNotFoundException {
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String resourcePath = rootPath + filename;

		Properties props = new Properties();
		props.load(new FileInputStream(resourcePath));
		return props;
	}

	private static GeoServerRESTManager initializeGeoserverRestManager() throws MalformedURLException {
		String RESTURL = geoserverProps.getProperty(GeoserverPropertiesConstants.REST_URL);
		String RESTUSER = geoserverProps.getProperty(GeoserverPropertiesConstants.REST_USER);
		String RESTPW = geoserverProps.getProperty(GeoserverPropertiesConstants.REST_PASSWORD);

		GeoServerRESTManager geoserverManager = new GeoServerRESTManager(new URL(RESTURL), RESTUSER, RESTPW);
		return geoserverManager;
	}

	private void publishDBLayerInGeoserver(String dbTableName, ResourceTypeEnum resourceType)
			throws MalformedURLException {
		GeoServerRESTManager geoserverManager = initializeGeoserverRestManager();

		GeoServerRESTReader reader = geoserverManager.getReader();
		GeoServerRESTPublisher publisher = geoserverManager.getPublisher();
		GeoServerRESTStoreManager storeManager = geoserverManager.getStoreManager();

		String targetWorkspace = geoserverProps.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String targetDatastore = null;
		String targetSchema = null;

		switch (resourceType) {
		case SPATIAL_UNIT:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_SPATIALUNITS);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_SPATIALUNITS);
			break;
		case GEORESOURCE:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
			break;
		case INDICATOR:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_INDICATORS);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_INDICATORS);
			break;

		default:
			targetDatastore = geoserverProps.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = geoserverProps.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
			break;
		}
		
		String targetEPSG = geoserverProps.getProperty(GeoserverPropertiesConstants.EPSG_DEFAULT);

		if (reader.existsFeatureType(targetWorkspace, targetDatastore, dbTableName)) {
			logger.info("Removing FeatureType '{}' on geoserver.", dbTableName);
			publisher.unpublishFeatureType(targetWorkspace, targetDatastore, dbTableName);
		}

		if (reader.existsLayer(targetWorkspace, dbTableName)
				|| (reader.getLayer(targetWorkspace, dbTableName) != null)) {
			publisher.removeLayer(targetWorkspace, dbTableName);
			logger.info("Removing Layer '{}' on geoserver", dbTableName);
		}

		if (reader.existGeoserver()) {

			if (reader.existsWorkspace(targetWorkspace)) {

				if (reader.existsDatastore(targetWorkspace, targetDatastore)) {

					logger.info("Publishing Layer '{}' on geoserver", dbTableName);

					publishLayerOnGeoserver(dbTableName, publisher, targetWorkspace, targetDatastore, targetEPSG);

					logger.info("Layer should have been published as OGC service via geoserver!");
				} else {
					createNewDataStoreOnGeoserver(storeManager, targetWorkspace, targetDatastore,
							targetSchema);

					publishDBLayerInGeoserver(dbTableName, resourceType);
				}
			} else {
				publisher.createWorkspace(targetWorkspace);
				publishDBLayerInGeoserver(dbTableName, resourceType);
			}
		}
	}

	private static void publishLayerOnGeoserver(String relation_name, GeoServerRESTPublisher publisher,
			String targetWorkspace, String targetDatastore, String targetEPSG) {
		GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
		fte.setProjectionPolicy(ProjectionPolicy.REPROJECT_TO_DECLARED);
		fte.addKeyword(relation_name);
		fte.setTitle(relation_name);
		fte.setName(relation_name);
		fte.setSRS(targetEPSG);

		final GSLayerEncoder layerEncoder = new GSLayerEncoder();

		boolean ok = publisher.publishDBLayer(targetWorkspace, targetDatastore, fte, layerEncoder);
	}

	private static void createNewDataStoreOnGeoserver(GeoServerRESTStoreManager storeManager, String targetWorkspace,
			String targetDatastore, String targetSchema) {
		GSPostGISDatastoreEncoder storeEncoder = new GSPostGISDatastoreEncoder(targetDatastore);
		storeEncoder.setDatabase(geoserverProps.getProperty(GeoserverPropertiesConstants.DB_DATABASE));
		storeEncoder.setHost(geoserverProps.getProperty(GeoserverPropertiesConstants.DB_HOST));
		storeEncoder.setPort(Integer.parseInt(geoserverProps.getProperty(GeoserverPropertiesConstants.DB_PORT)));
		storeEncoder.setUser(geoserverProps.getProperty(GeoserverPropertiesConstants.DB_USERNAME));
		storeEncoder.setPassword(geoserverProps.getProperty(GeoserverPropertiesConstants.DB_PASSWORD));
		storeEncoder.setSchema(targetSchema);
		// storeEncoder.setNamespace(dbProps.getProperty("kommonitor"));
		logger.info("Creating new db store '{}' on geoserver" + storeEncoder);
		storeManager.create(targetWorkspace, storeEncoder);
	}
}
