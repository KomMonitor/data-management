package de.hsbo.kommonitor.datamanagement.api.impl.webservice.management;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.geotools.brewer.color.StyleGenerator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.Classifier;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import de.hsbo.kommonitor.datamanagement.features.management.IndicatorDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.DefaultClassificationMappingItemType;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.DefaultClassificationMappingType;
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

	public static final String STYLE_PREFIX = "STYLE_";

	private static Logger logger = LoggerFactory.getLogger(GeoserverManager.class);

//	private static Properties env = null;
	
	private static Environment env;
	
	public GeoserverManager(Environment environment) {
		env = environment;
	}

	@Override
	public boolean publishDbLayerAsOgcService(String dbTableName, String title, String defaultStyleName, ResourceTypeEnum resourceType)
			throws Exception {
		
		if(Boolean.parseBoolean(env.getProperty(GeoserverPropertiesConstants.OGC_ENABLE_SERVICE_PUBLICATION))){
			publishDBLayerInGeoserver(dbTableName, title, defaultStyleName, resourceType);
			return true;
		}
		else{
			logger.info("OGC service management will be skipped according to service configuration. No publishing/unpublishing of layers is possible.");
			return false;
		}

	}

	@Override
	public boolean unpublishDbLayer(String dbTableName, ResourceTypeEnum resourceType) throws MalformedURLException {
		
		if(Boolean.parseBoolean(env.getProperty(GeoserverPropertiesConstants.OGC_ENABLE_SERVICE_PUBLICATION))){
			GeoServerRESTManager geoserverManager = initializeGeoserverRestManager();

			GeoServerRESTReader reader = geoserverManager.getReader();
			GeoServerRESTPublisher publisher = geoserverManager.getPublisher();

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
		else{
			logger.info("OGC service management will be skipped according to service configuration. No publishing/unpublishing of layers is possible.");
			return false;
		}
		
	}

	@Override
	public String getWmsUrl(String dbTableName) {
		
		
		if(Boolean.parseBoolean(env.getProperty(GeoserverPropertiesConstants.OGC_ENABLE_SERVICE_PUBLICATION))){
			// example: http://localhost:8080/geoserver/kommonitor/SPATIAL_UNIT_5/ows?service=WMS&request=GetCapabilities
			String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
			String wmsUrl = env.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/"+ dbTableName + "/wms?service=WMS&request=GetCapabilities";
			
			logger.info("created WMS URL '{}' for dbTable '{}'", wmsUrl, dbTableName);
			
			return wmsUrl;
		}
		else{
			logger.info("OGC service management will be skipped according to service configuration. No OGC service layer can exist.");
			return "";
		}
	}

	@Override
	public String getWfsUrl(String dbTableName) {	
		
		if(Boolean.parseBoolean(env.getProperty(GeoserverPropertiesConstants.OGC_ENABLE_SERVICE_PUBLICATION))){
			// example: http://localhost:8080/geoserver/kommonitor/SPATIAL_UNIT_5/ows?service=WFS&request=GetCapabilities
			String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
			String wfsUrl = env.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/"+ dbTableName + "/wfs?service=WFS&request=GetCapabilities";
			
			logger.info("created WFS URL '{}' for dbTable '{}'", wfsUrl, dbTableName);
			
			return wfsUrl;
		}
		else{
			logger.info("OGC service management will be skipped according to service configuration. No OGC service layer can exist.");
			return "";
		}
	}

	@Override
	public String getWcsUrl(String dbTableName) {
		
		if(Boolean.parseBoolean(env.getProperty(GeoserverPropertiesConstants.OGC_ENABLE_SERVICE_PUBLICATION))){
			// example: http://localhost:8080/geoserver/kommonitor/SPATIAL_UNIT_5/ows?service=WCS&request=GetCapabilities
			String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);
			String wcsUrl = env.getProperty(GeoserverPropertiesConstants.REST_URL) + "/" + targetWorkspace + "/"+ dbTableName + "/wcs?service=WCS&request=GetCapabilities";
			
			logger.info("created WCS URL '{}' for dbTable '{}'", wcsUrl, dbTableName);
			
			return wcsUrl;
		}
		else{
			logger.info("OGC service management will be skipped according to service configuration. No OGC service layer can exist.");
			return "";
		}
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

	private void publishDBLayerInGeoserver(String dbTableName, String title, String defaultStyleName, ResourceTypeEnum resourceType)
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
						publishLayerOnGeoserver(dbTableName, title, defaultStyleName, publisher, targetWorkspace, targetDatastore, targetEPSG);
					} catch (Exception e) {
						logger.error("Error while publishing layer to Geoserver.");
						e.printStackTrace();
						throw e;
					}

					logger.info("Layer should have been published as OGC service via geoserver!");
				} else {
					createNewDataStoreOnGeoserver(storeManager, targetWorkspace, targetDatastore,
							targetSchema);

					publishDBLayerInGeoserver(dbTableName, title, defaultStyleName, resourceType);
				}
			} else {
				boolean created = publisher.createWorkspace(targetWorkspace);
				if(!created)
					throw new Exception("Error while creating workspace. Processing failed.");
				
				publishDBLayerInGeoserver(dbTableName, title, defaultStyleName, resourceType);
			}
		}
	}

	private static void publishLayerOnGeoserver(String relation_name, String title, String defaultStyleName, GeoServerRESTPublisher publisher,
			String targetWorkspace, String targetDatastore, String targetEPSG) throws Exception {
		GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
		fte.setProjectionPolicy(ProjectionPolicy.REPROJECT_TO_DECLARED);
		fte.addKeyword(relation_name);
		fte.setTitle(title);
		fte.setName(relation_name);
		fte.setSRS(targetEPSG);

		final GSLayerEncoder layerEncoder = new GSLayerEncoder();
		if (defaultStyleName != null)
			layerEncoder.setDefaultStyle(targetWorkspace, defaultStyleName);

		boolean ok = publisher.publishDBLayer(targetWorkspace, targetDatastore, fte, layerEncoder);
		
		if (!ok)
			throw new Exception("Error while publishing layer to Geoserver.");
	}

	private static void createNewDataStoreOnGeoserver(GeoServerRESTStoreManager storeManager, String targetWorkspace,
			String targetDatastore, String targetSchema) throws Exception {
		GSPostGISDatastoreEncoder storeEncoder = new GSPostGISDatastoreEncoder(targetDatastore);
		storeEncoder.setDatabase(env.getProperty(GeoserverPropertiesConstants.DB_DATABASE));
		storeEncoder.setHost(env.getProperty(GeoserverPropertiesConstants.DB_HOST));
		storeEncoder.setPort(Integer.parseInt(env.getProperty(GeoserverPropertiesConstants.DB_PORT)));
		storeEncoder.setUser(env.getProperty(GeoserverPropertiesConstants.DB_USERNAME));
		storeEncoder.setPassword(env.getProperty(GeoserverPropertiesConstants.DB_PASSWORD));
		storeEncoder.setSchema(targetSchema);
		// storeEncoder.setNamespace(dbProps.getProperty("kommonitor"));
		logger.info("Creating new db store '{}' on geoserver" + storeEncoder);
		boolean created = storeManager.create(targetWorkspace, storeEncoder);
		
		if(!created)
			throw new Exception("Error while creating data store. Processing failed.");
	}

	@Override
	public String createAndPublishStyle(String datasetTitle, FeatureCollection features,
			DefaultClassificationMappingType defaultClassificationMappingType, String targetPropertyName) throws TransformerException, FactoryRegistryException, IllegalFilterException, MalformedURLException {
		
		if(Boolean.parseBoolean(env.getProperty(GeoserverPropertiesConstants.OGC_ENABLE_SERVICE_PUBLICATION))){
			if(! targetPropertyName.startsWith(IndicatorDatabaseHandler.DATE_PREFIX))
				targetPropertyName = IndicatorDatabaseHandler.DATE_PREFIX + targetPropertyName;
			
			String styleName = STYLE_PREFIX + datasetTitle + "_" + targetPropertyName;
			
			// execute classifiation using colorBrewer to classify the indicator values
			
			List<DefaultClassificationMappingItemType> classificationItems = defaultClassificationMappingType.getItems();
			int numberOfClasses = classificationItems.size();
			
			// STEP 0 Set up Color Brewer
//	        ColorBrewer brewer = ColorBrewer.instance();

	        // STEP 1 - call a classifier function to summarise your content
	        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
	        PropertyName propertyExpression = ff.property(targetPropertyName);

	        // classify into five categories using natural breaks (jenks)
	        Function classify = ff.function("Jenks", propertyExpression, ff.literal(numberOfClasses));
	        Classifier groups = (Classifier) classify.evaluate(features);

	        // STEP 2 - look up a predefined palette from color brewer
//	        String paletteName = "GrBu";
//	        Color[] colors = brewer.getPalette(paletteName).getColors(5);
	        Color[] colors = getColorsFromClassification(classificationItems);
	        
	        // STEP 3 - ask StyleGenerator to make a set of rules for the Classifier
	        // assigning features the correct color based on height
	        FeatureTypeStyle style =
	                StyleGenerator.createFeatureTypeStyle(
	                        groups,
	                        propertyExpression,
	                        colors,
	                        "Generated FeatureTypeStyle",
	                        features.getSchema().getGeometryDescriptor(),
	                        StyleGenerator.ELSEMODE_IGNORE,
	                        0.95,
	                        null);
	        
	        
			
			// create SLD from classes and colors for target date property
			String sld = generateSLD(datasetTitle, targetPropertyName, style);
			
			// publish style

			GeoServerRESTManager geoserverManager = initializeGeoserverRestManager();

			GeoServerRESTReader reader = geoserverManager.getReader();
			GeoServerRESTPublisher publisher = geoserverManager.getPublisher();
			GeoServerRESTStoreManager storeManager = geoserverManager.getStoreManager();

			String targetWorkspace = env.getProperty(GeoserverPropertiesConstants.WORKSPACE);

			publishOrModifySldOnGeoserver(reader, publisher, targetWorkspace, styleName, sld);
			
			// return styleName
			
			return styleName;
		}
		else{
			logger.info("OGC service management will be skipped according to service configuration. No publishing/unpublishing of styles is possible.");
			return "";
		}
	}
	
	private String generateSLD(String datasetTitle, String mostCurrentDate, FeatureTypeStyle featureTypeStyle) throws TransformerException {
		StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();
        sld.setName(datasetTitle + "_" + mostCurrentDate);
        sld.setTitle(datasetTitle + "_" + mostCurrentDate);
        sld.setAbstract(datasetTitle + "_" + mostCurrentDate);

        UserLayer layer = styleFactory.createUserLayer();
        layer.setName(datasetTitle + "_" + mostCurrentDate);

//        FeatureTypeConstraint constraint =
//                styleFactory.createFeatureTypeConstraint("Feature", Filter.INCLUDE, null);
//
//        layer.layerFeatureConstraints().add(constraint);

        Style style = styleFactory.createStyle();
        style.setName(datasetTitle + "_" + mostCurrentDate);
        style.getDescription().setTitle(datasetTitle + "_" + mostCurrentDate);
        style.getDescription().setAbstract(datasetTitle + "_" + mostCurrentDate);
        
        style.featureTypeStyles().add(featureTypeStyle);

        // define feature type styles used to actually
        // define how features are rendered
        //
        layer.userStyles().add(style);

        sld.layers().add(layer);
        
        SLDTransformer styleTransform = new SLDTransformer();
        String xml = styleTransform.transform(sld);
        
        logger.info("Generated the following SLD document: {}", xml);
        
        return xml;
	}

	private Color[] getColorsFromClassification(List<DefaultClassificationMappingItemType> classificationItems) {
		Color[] colors = new Color[classificationItems.size()];
		
		for (int i=0; i<classificationItems.size(); i++) {
			colors[i] = Color.decode(classificationItems.get(i).getDefaultColorAsHex());
		}
		return colors;
	}

//	private String createSldBody(String datasetTitle, List<Float> indicatorValues,
//			DefaultClassificationMappingType defaultClassificationMappingType, String mostCurrentDate) {
//		
//		StringBuilder builder = new StringBuilder();
//		
//		builder.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
//				builder.append("<StyledLayerDescriptor version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
//
//				builder.append("<NamedLayer>");
//				builder.append("<Name>" + datasetTitle + "_" + mostCurrentDate + "</Name>");
//				builder.append("<UserStyle>");
//				builder.append("<Title>" + datasetTitle + "_" + mostCurrentDate + "</Title>");
//				builder.append("<FeatureTypeStyle>");
//				   
//				/*
//				 * now create a SLD Rule for each class intervall with the corresponding color
//				 */
//				
//				
//				
//				builder.append("</FeatureTypeStyle>");
//				builder.append("</UserStyle>");
//				builder.append("</NamedLayer>");
//				builder.append("</StyledLayerDescriptor>");
//				
//			      
//			      
//			    
//			  
//		
//		String sldBody = builder.toString();
//		
//		
//		
//		return sldBody;
//	}

	private static void publishOrModifySldOnGeoserver(GeoServerRESTReader reader, GeoServerRESTPublisher publisher,
			String targetWorkspace, String styleName, String sld) {
		if (reader.existsStyle(targetWorkspace, styleName)) {
			logger.info("Updating existing style " + styleName);
			publisher.updateStyleInWorkspace(targetWorkspace, sld, styleName);
		} else {
			logger.info("Publishing new style " + styleName);
			publisher.publishStyleInWorkspace(targetWorkspace, sld, styleName);
		}
	}
}
