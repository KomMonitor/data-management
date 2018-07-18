package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;

import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;

public class GeoJSON2DatabaseTool {

	public static boolean writeGeoJSONFeaturesToDatabase(ResourceTypeEnum resourceType, String geoJSONFeatures,
			PeriodOfValidityType periodOfValidity, String correspondingMetadataDatasetId) throws IOException {

		/*
		 * TODO implement
		 */

		InputStream stream = new ByteArrayInputStream(geoJSONFeatures.getBytes());

		FeatureJSON featureJSON = new FeatureJSON();
		SimpleFeatureType featureSchema = featureJSON.readFeatureCollectionSchema(stream, false);
		FeatureCollection featureCollection = featureJSON.readFeatureCollection(geoJSONFeatures);
//		GeoJSONUtil
//				.readFeatureCollection(stream);

		DataStore postGisStore = getPostGisDataStore();
		featureSchema = enrichWithKomMonitorProperties(featureSchema, postGisStore, resourceType);

		featureCollection = initializeKomMonitorProperties(featureCollection, periodOfValidity);

		postGisStore.createSchema(featureSchema);

		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(featureSchema.getName());
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) featureSource; // write
																			// access!
			store.addFeatures(featureCollection);
		}

		/*
		 * after writing to DB set the unique db tableName within the
		 * corresponding MetadataEntry
		 */
		updateResourceMetadataEntry(featureSchema.getName().toString(), correspondingMetadataDatasetId);

		return true;
	}

	private static void updateResourceMetadataEntry(String tableName, String correspondingMetadataDatasetId) {
		// TODO FIXME update metadata entry: set name of associated dbTable

	}

	private static FeatureCollection initializeKomMonitorProperties(FeatureCollection featureCollection,
			PeriodOfValidityType periodOfValidity) {
		FeatureIterator featureIterator = featureCollection.features();

		while (featureIterator.hasNext()) {
			Feature next = featureIterator.next();

			/*
			 * take the values from the PeriodOfValidity
			 */

			next.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME)
					.setValue(periodOfValidity.getStartDate());
			next.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME)
					.setValue(periodOfValidity.getEndDate());
			/*
			 * arisonFrom cannot be set, when features are initialized for the
			 * first time.
			 */
			next.getProperty(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME).setValue(null);
		}

		return featureCollection;
	}

	private static SimpleFeatureType enrichWithKomMonitorProperties(SimpleFeatureType featureSchema,
			DataStore dataStore, ResourceTypeEnum resourceType) throws IOException {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(createUniqueTableNameForResourceType(resourceType, dataStore));
		tb.setNamespaceURI(featureSchema.getName().getNamespaceURI());
		tb.setCRS(featureSchema.getCoordinateReferenceSystem());
		tb.addAll(featureSchema.getAttributeDescriptors());
		tb.setDefaultGeometry(featureSchema.getGeometryDescriptor().getLocalName());

		/*
		 * add KomMonitor specific properties!
		 */
		tb.add(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, Date.class);
		tb.add(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, Date.class);
		tb.add(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME, String.class);

		return tb.buildFeatureType();
	}

	private static String createUniqueTableNameForResourceType(ResourceTypeEnum resourceType, DataStore dataStore)
			throws IOException {
		int numberSuffix = 0;
		String resourceTypeName = resourceType.name();

		String potentialDBTableName = createPotentialDBTableName(resourceTypeName, numberSuffix);

		SimpleFeatureSource featureSource = null;
		boolean uniqueTableNameFound = false;
		do {
			featureSource = dataStore.getFeatureSource(potentialDBTableName);
			if (featureSource == null) {
				uniqueTableNameFound = true;
				break;
			} else {
				// increment suffix and try again
				potentialDBTableName = createPotentialDBTableName(resourceTypeName, numberSuffix++);
			}
		} while (!uniqueTableNameFound);

		return potentialDBTableName;
	}

	private static String createPotentialDBTableName(String resourceTypeName, int numberSuffix) {
		// TODO Auto-generated method stub
		return resourceTypeName + "_" + numberSuffix;
	}

	private static DataStore getPostGisDataStore() throws IOException {

		Properties properties = new Properties();

		/*
		 * TODO If environment variables are used for DB connection then change
		 * this FIXME If environment variables are used for DB connection then
		 * change this
		 */
		properties.load(GeoJSON2DatabaseTool.class.getResourceAsStream("/application.properties"));

		Map<String, Object> params = new HashMap<>();
		params.put("dbtype", "postgis");
		params.put("host", properties.getProperty("database.host"));
		params.put("port", 5432);
		params.put("schema", "public");
		params.put("database", properties.getProperty("database.name"));
		params.put("user", properties.getProperty("spring.datasource.username"));
		params.put("passwd", properties.getProperty("spring.datasource.password"));

		DataStore dataStore = DataStoreFinder.getDataStore(params);

		return dataStore;
	}

}
