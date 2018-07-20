package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPUTInputType;

public class GeoJSON2DatabaseTool {

	private static Logger logger = LoggerFactory.getLogger(GeoJSON2DatabaseTool.class);

	public static String writeGeoJSONFeaturesToDatabase(ResourceTypeEnum resourceType, String geoJSONFeatures,
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

		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		featureSchema = enrichWithKomMonitorProperties(featureSchema, postGisStore, resourceType);

		logger.info("create new Table from featureSchema using table name {}", featureSchema.getTypeName());
		postGisStore.createSchema(featureSchema);

		logger.info("Start to add the actual features to table with name {}", featureSchema.getTypeName());
		persistSpatialResource(periodOfValidity, featureSchema, featureCollection, postGisStore);

		/*
		 * after writing to DB set the unique db tableName within the
		 * corresponding MetadataEntry
		 */

		logger.info(
				"Modifying the metadata entry to set the name of the formerly created feature database table. MetadataId for resourceType {} is {}",
				resourceType.name(), correspondingMetadataDatasetId);
		DatabaseHelperUtil.updateResourceMetadataEntry(resourceType, featureSchema.getTypeName().toString(),
				correspondingMetadataDatasetId);

		postGisStore.dispose();

		return featureSchema.getTypeName();
	}

	private static void persistSpatialResource(PeriodOfValidityType periodOfValidity, SimpleFeatureType featureSchema,
			FeatureCollection featureCollection, DataStore postGisStore) throws IOException, CQLException {
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(featureSchema.getTypeName());
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) featureSource; // write
																			// access!
			addFeatureCollectionToTable(featureSchema, featureCollection, store);

			logger.info("Features should have been added to table with name {}", featureSchema.getTypeName());

			logger.info("Start to modify the features (set periodOfValidity) in table with name {}",
					featureSchema.getTypeName());

			initializePeriodOfValidityForAllEntries(periodOfValidity, featureSchema, store);

			logger.info("Modification of features finished  for table with name {}", featureSchema.getTypeName());
		}
	}

	private static void initializePeriodOfValidityForAllEntries(PeriodOfValidityType periodOfValidity,
			SimpleFeatureType featureSchema, SimpleFeatureStore store) throws CQLException, IOException {
		Transaction transaction;
		Filter filter = CQL.toFilter(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + " is null");

		transaction = new DefaultTransaction(
				"Modify (initialize periodOfValidity) features in Table " + featureSchema.getTypeName());
		store.setTransaction(transaction);
		try {
			store.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
					periodOfValidity.getStartDate(), filter);
			store.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, periodOfValidity.getEndDate(),
					filter);
			transaction.commit(); // actually writes out the features in one
									// go
		} catch (Exception eek) {
			transaction.rollback();
		}

		transaction.close();
	}

	private static void addFeatureCollectionToTable(SimpleFeatureType featureSchema,
			FeatureCollection featureCollection, SimpleFeatureStore store) throws IOException {
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
	}

	// private static FeatureCollection
	// initializeKomMonitorProperties(FeatureCollection featureCollection,
	// PeriodOfValidityType periodOfValidity) {
	// FeatureIterator featureIterator = featureCollection.features();
	//
	// while (featureIterator.hasNext()) {
	// Feature next = featureIterator.next();
	//
	// /*
	// * take the values from the PeriodOfValidity
	// */
	//
	// next.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME)
	// .setValue(periodOfValidity.getStartDate());
	// next.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME)
	// .setValue(periodOfValidity.getEndDate());
	// /*
	// * arisonFrom cannot be set, when features are initialized for the
	// * first time.
	// */
	// next.getProperty(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME).setValue(null);
	// }
	//
	// return featureCollection;
	// }

	private static SimpleFeatureType enrichWithKomMonitorProperties(SimpleFeatureType featureSchema,
			DataStore dataStore, ResourceTypeEnum resourceType) throws IOException {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(DatabaseHelperUtil.createUniqueTableNameForResourceType(resourceType, dataStore));
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

	public static void deleteFeatureTable(ResourceTypeEnum spatialUnit, String dbTableName) throws IOException {
		logger.info("Deleting feature table {}.", dbTableName);

		DataStore store = DatabaseHelperUtil.getPostGisDataStore();

		store.removeSchema(dbTableName);

		logger.info("Deletion of table {} was successful {}", dbTableName);

		store.dispose();
	}

	public static void updateFeatures(SpatialUnitPUTInputType featureData, String dbTableName) {
		// TODO FIXME implement

		// TODO check, if update was successful
	}

	public static AvailablePeriodOfValidityType getAvailablePeriodOfValidity(String dbTableName)
			throws IOException, SQLException {
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT min(\"" + KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME
				+ "\") FROM \"" + dbTableName + "\"");

		AvailablePeriodOfValidityType validityPeriod = new AvailablePeriodOfValidityType();
		if (rs.next()) { // check if a result was returned
			validityPeriod.setEarliestStartDate(new java.sql.Date(rs.getDate(1).getTime()).toLocalDate());
		}

		rs.close();
		statement.close();

		// handle endDate
		statement = jdbcConnection.createStatement();

		// be sure to quote the identifier!
		rs = statement.executeQuery("SELECT \"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" FROM \""
				+ dbTableName + "\" WHERE \"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" = null");

		if (rs.next()) { // check if a result was returned
			/*
			 * the result set has results. Thus there are features with endDate
			 * == null
			 * 
			 * Thus there is no known latestEndDate to be set
			 */
			validityPeriod.setEndDate(null);
		} else {
			/*
			 * we have to find the latest endDate (maxValue)
			 */
			// be sure to quote the identifier!
			rs = statement.executeQuery("SELECT max(\"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME
					+ "\") FROM \"" + dbTableName + "\"");
			if (rs.next()) { // check if a result was returned
				/*
				 * latestEndDate might be null, if it was never set
				 * 
				 * then it indicates, that there is no end date and all data is
				 * st
				 */
				java.sql.Date latestEndDate = rs.getDate(1);
				if (latestEndDate != null)
					validityPeriod.setEndDate(new java.sql.Date(latestEndDate.getTime()).toLocalDate());
			}

		}
		rs.close();
		statement.close();

		jdbcConnection.close();
		return validityPeriod;
	}

	public static String getValidFeatures(Date date, String dbTableName) throws IOException, CQLException {
		/*
		 * fetch all features from table where startDate <= date and (endDate >= date || endDate = null)
		 * 
		 * then transform the featureCollection to GeoJSON! 
		 * return geojsonString
		 */
		logger.info("Fetch features for validDate {} from table with name {}", date, dbTableName);
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();
		
		FeatureCollection features;
		
		SimpleFeatureSource featureSource = dataStore.getFeatureSource(dbTableName);
		features = fetchFeaturesForDate(featureSource, date);
		
		logger.info("Transform {} found features to GeoJSON", features.size());
		
		FeatureJSON toGeoJSON = new FeatureJSON();
		StringWriter writer = new StringWriter();
		toGeoJSON.writeFeatureCollection(features, writer);
		String geoJson = writer.toString();
		
		dataStore.dispose();
		
		return geoJson;
	}

	private static FeatureCollection fetchFeaturesForDate(SimpleFeatureSource featureSource, Date date)
			throws CQLException, IOException {
		// fetch all features from table where startDate <= date and (endDate >=
		// date || endDate = null)
		Filter filter = CQL.toFilter(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + " <= " + date + " AND ("
				+ KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + " = null OR "
				+ KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + " >= " + date + ")");
		SimpleFeatureCollection features = featureSource.getFeatures(filter);
		return features;
	}

}
