package de.hsbo.kommonitor.datamanagement.api.impl.webservice.management;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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

//	private static Properties env = null;
	
	private static Environment env;
	
	public GeoserverManager(Environment environment) {
		env = environment;
	}

	@Override
	public boolean publishDbLayerAsOgcService(String dbTableName, ResourceTypeEnum resourceType)
			throws Exception {

		publishDBLayerInGeoserver(dbTableName, resourceType);
		return true;
	}

	@Override
	public boolean unpublishDbLayer(String dbTableName, ResourceTypeEnum resourceType) throws MalformedURLException {
		GeoServerRESTManager geoserverManager = initializeGeoserverRestManager();

		GeoServerRESTReader reader = geoserverManager.getReader();
		GeoServerRESTPublisher publisher = geoserverManager.getPublisher();
		GeoServerRESTStoreManager storeManager = geoserverManager.getStoreManager();

		String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String targetDatastore = null;
		String targetSchema = null;

		switch (resourceType) {
		case SPATIAL_UNIT:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_SPATIALUNITS);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_SPATIALUNITS);
			break;
		case GEORESOURCE:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
			break;
		case INDICATOR:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_INDICATORS);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_INDICATORS);
			break;

		default:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
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
		// example: http://localhost:8080/geoserver/kommonitor/SPATIAL_UNIT_5/ows?service=WMS&request=GetCapabilities
		String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String wmsUrl = env.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/"+ dbTableName + "/wms?service=WMS&request=GetCapabilities";
		
		logger.info("created WMS URL '{}' for dbTable '{}'", wmsUrl, dbTableName);
		
		return wmsUrl;
	}

	@Override
	public String getWfsUrl(String dbTableName) {
		// example: http://localhost:8080/geoserver/kommonitor/SPATIAL_UNIT_5/ows?service=WFS&request=GetCapabilities
		String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String wfsUrl = env.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/"+ dbTableName + "/wfs?service=WFS&request=GetCapabilities";
		
		logger.info("created WFS URL '{}' for dbTable '{}'", wfsUrl, dbTableName);
		
		return wfsUrl;
	}

	@Override
	public String getWcsUrl(String dbTableName) {
		// example: http://localhost:8080/geoserver/kommonitor/SPATIAL_UNIT_5/ows?service=WCS&request=GetCapabilities
		String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String wcsUrl = env.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/"+ dbTableName + "/wcs?service=WCS&request=GetCapabilities";
		
		logger.info("created WCS URL '{}' for dbTable '{}'", wcsUrl, dbTableName);
		
		return wcsUrl;
	}

//	private static void parseResourceFiles() throws IOException, FileNotFoundException {
//
//		if (env == null)
//			;
//		env = parseProperties(ResourceFileConstants.geoserverPropertiesFileLocation);
//	}

//	private static Properties parseProperties(String filename) throws IOException, FileNotFoundException {
//		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//		String resourcePath = rootPath + filename;
//
//		Properties props = new Properties();
//		props.load(new FileInputStream(resourcePath));
//		return props;
//	}

	private static GeoServerRESTManager initializeGeoserverRestManager() throws MalformedURLException {
		String RESTURL = env.getProperty(GeoserverPropertiesConstants.REST_URL);
		String RESTUSER = env.getProperty(GeoserverPropertiesConstants.REST_USER);
		String RESTPW = env.getProperty(GeoserverPropertiesConstants.REST_PASSWORD);
		
		logger.info("Initialize GeoserverRESTManager with URL='{}' and username='{}' and password='{}'", RESTURL, RESTUSER, RESTPW);

		GeoServerRESTManager geoserverManager = new GeoServerRESTManager(new URL(RESTURL), RESTUSER, RESTPW);
		return geoserverManager;
	}

	private void publishDBLayerInGeoserver(String dbTableName, ResourceTypeEnum resourceType)
			throws Exception {
		GeoServerRESTManager geoserverManager = initializeGeoserverRestManager();

		GeoServerRESTReader reader = geoserverManager.getReader();
		GeoServerRESTPublisher publisher = geoserverManager.getPublisher();
		GeoServerRESTStoreManager storeManager = geoserverManager.getStoreManager();

		String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
		String targetDatastore = null;
		String targetSchema = null;

		switch (resourceType) {
		case SPATIAL_UNIT:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_SPATIALUNITS);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_SPATIALUNITS);
			break;
		case GEORESOURCE:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
			break;
		case INDICATOR:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_INDICATORS);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_INDICATORS);
			break;

		default:
			targetDatastore = env.getProperty(GeoserverPropertiesConstants.DATASTORE_GEORESOURCES);
			targetSchema = env.getProperty(GeoserverPropertiesConstants.DB_SCHEMA_GEORESOURCES);
			break;
		}
		
		String targetEPSG = env.getProperty(GeoserverPropertiesConstants.EPSG_DEFAULT);

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

					try {
						publishLayerOnGeoserver(dbTableName, publisher, targetWorkspace, targetDatastore, targetEPSG);
					} catch (Exception e) {
						logger.error("Error while publishing layer to Geoserver.");
						e.printStackTrace();
						throw e;
					}

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
			String targetWorkspace, String targetDatastore, String targetEPSG) throws Exception {
		GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
		fte.setProjectionPolicy(ProjectionPolicy.REPROJECT_TO_DECLARED);
		fte.addKeyword(relation_name);
		fte.setTitle(relation_name);
		fte.setName(relation_name);
		fte.setSRS(targetEPSG);

		final GSLayerEncoder layerEncoder = new GSLayerEncoder();

		boolean ok = publisher.publishDBLayer(targetWorkspace, targetDatastore, fte, layerEncoder);
		
		if (!ok)
			throw new Exception("Error while publishing ayer to Geoserver.");
	}

	private static void createNewDataStoreOnGeoserver(GeoServerRESTStoreManager storeManager, String targetWorkspace,
			String targetDatastore, String targetSchema) {
		GSPostGISDatastoreEncoder storeEncoder = new GSPostGISDatastoreEncoder(targetDatastore);
		storeEncoder.setDatabase(env.getProperty(GeoserverPropertiesConstants.DB_DATABASE));
		storeEncoder.setHost(env.getProperty(GeoserverPropertiesConstants.DB_HOST));
		storeEncoder.setPort(Integer.parseInt(env.getProperty(GeoserverPropertiesConstants.DB_PORT)));
		storeEncoder.setUser(env.getProperty(GeoserverPropertiesConstants.DB_USERNAME));
		storeEncoder.setPassword(env.getProperty(GeoserverPropertiesConstants.DB_PASSWORD));
		storeEncoder.setSchema(targetSchema);
		// storeEncoder.setNamespace(dbProps.getProperty("kommonitor"));
		logger.info("Creating new db store '{}' on geoserver" + storeEncoder);
		storeManager.create(targetWorkspace, storeEncoder);
	}
}
