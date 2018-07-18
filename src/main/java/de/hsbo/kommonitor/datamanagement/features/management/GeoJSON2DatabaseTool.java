package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;

public class GeoJSON2DatabaseTool {

	private static Logger logger = LoggerFactory.getLogger(GeoJSON2DatabaseTool.class);

	public static boolean writeGeoJSONFeaturesToDatabase(ResourceTypeEnum resourceType, String geoJSONFeatures,
			PeriodOfValidityType periodOfValidity, String correspondingMetadataDatasetId)
			throws IOException, CQLException {

		/*
		 * TODO implement
		 */

		// InputStream stream = new
		// ByteArrayInputStream(geoJSONFeatures.getBytes());

		logger.info("Parsing GeoJSON into features and schema");

		FeatureJSON featureJSON = new FeatureJSON();
		SimpleFeatureType featureSchema = featureJSON.readFeatureCollectionSchema(geoJSONFeatures, false);
		FeatureCollection featureCollection = featureJSON.readFeatureCollection(geoJSONFeatures);
		// GeoJSONUtil
		// .readFeatureCollection(stream);

		logger.info("Enriching featureSchema with KomMonitor specific properties");
		;

		DataStore postGisStore = getPostGisDataStore();
		featureSchema = enrichWithKomMonitorProperties(featureSchema, postGisStore, resourceType);

		logger.info("create new Table from featureSchema using table name {}", featureSchema.getTypeName());
		postGisStore.createSchema(featureSchema);

		logger.info("Start to add the actual features to table with name {}", featureSchema.getTypeName());
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(featureSchema.getTypeName());
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) featureSource; // write
																			// access!

			Transaction transaction = new DefaultTransaction("Add features in Table " + featureSchema.getTypeName());
			store.setTransaction(transaction);
			try {
				store.addFeatures(featureCollection);
				transaction.commit(); // actually writes out the features in one
										// go
			} catch (Exception eek) {
				transaction.rollback();
			}

			transaction.close();

			logger.info("Features should have been added to table with name {}", featureSchema.getTypeName());

			logger.info("Start to modify the features (set periodOfValidity) in table with name {}",
					featureSchema.getTypeName());
			final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
			Filter filter = CQL.toFilter(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + " is null");

			transaction = new DefaultTransaction(
					"Modify (initialize periodOfValidity) features in Table " + featureSchema.getTypeName());
			store.setTransaction(transaction);
			try {
				store.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
						periodOfValidity.getStartDate(), filter);
				store.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME,
						periodOfValidity.getEndDate(), filter);
				transaction.commit(); // actually writes out the features in one
										// go
			} catch (Exception eek) {
				transaction.rollback();
			}

			transaction.close();

			logger.info("Modification of features finished  for table with name {}", featureSchema.getTypeName());
		}

		// /*
		// * now fetch the entries again from db
		// * in order to set the custom added properties!
		// */
		// featureCollection = featureSource.getFeatures();
		//
		// featureCollection = initializeKomMonitorProperties(featureCollection,
		// periodOfValidity);
		//
		// if (featureSource instanceof SimpleFeatureStore) {
		// SimpleFeatureStore store = (SimpleFeatureStore) featureSource; //
		// write
		// // access
		// }

		/*
		 * after writing to DB set the unique db tableName within the
		 * corresponding MetadataEntry
		 */

		logger.info(
				"Modifying the metadata entry to set the name of the formerly created feature database table. MetadataId for resourceType {} is {}",
				resourceType.name(), correspondingMetadataDatasetId);
		updateResourceMetadataEntry(featureSchema.getTypeName().toString(), correspondingMetadataDatasetId);

		postGisStore.dispose();

		return true;
	}

	private static void updateResourceMetadataEntry(String tableName, String correspondingMetadataDatasetId) {
		// TODO FIXME update metadata entry: set name of associated dbTable

	}

//	private static FeatureCollection initializeKomMonitorProperties(FeatureCollection featureCollection,
//			PeriodOfValidityType periodOfValidity) {
//		FeatureIterator featureIterator = featureCollection.features();
//
//		while (featureIterator.hasNext()) {
//			Feature next = featureIterator.next();
//
//			/*
//			 * take the values from the PeriodOfValidity
//			 */
//
//			next.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME)
//					.setValue(periodOfValidity.getStartDate());
//			next.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME)
//					.setValue(periodOfValidity.getEndDate());
//			/*
//			 * arisonFrom cannot be set, when features are initialized for the
//			 * first time.
//			 */
//			next.getProperty(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME).setValue(null);
//		}
//
//		return featureCollection;
//	}

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
			try {
				featureSource = dataStore.getFeatureSource(potentialDBTableName);
			} catch (Exception e) {
				// we assume that should the retrieval of featureSource have
				// failed, then
				// there is no database table for the potentialName
				uniqueTableNameFound = true;
				break;
			}

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
