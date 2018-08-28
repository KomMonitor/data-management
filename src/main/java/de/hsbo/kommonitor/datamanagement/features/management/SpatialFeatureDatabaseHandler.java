package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.temporal.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePUTInputType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPUTInputType;

public class SpatialFeatureDatabaseHandler {

	private static Logger logger = LoggerFactory.getLogger(SpatialFeatureDatabaseHandler.class);
	private static int numberOfModifiedEntries;
	private static int numberOfInsertedEntries;
	private static int numberOfEntriesMarkedAsOutdated;
	private static boolean inputFeaturesHaveArisonFromAttribute;

	public static String writeGeoJSONFeaturesToDatabase(ResourceTypeEnum resourceType, String geoJSONFeatures,
			PeriodOfValidityType periodOfValidity, String correspondingMetadataDatasetId)
			throws IOException, CQLException {

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



	public static AvailablePeriodOfValidityType getAvailablePeriodOfValidity(String dbTableName)
			throws IOException, SQLException {
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT min(\"" + KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME
				+ "\") FROM \"" + dbTableName + "\"");

		AvailablePeriodOfValidityType validityPeriod = new AvailablePeriodOfValidityType();
		if (rs.next()) { // check if a result was returned
			validityPeriod.setEarliestStartDate(DateTimeUtil.toLocalDate(rs.getDate(1)));
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
					validityPeriod.setEndDate(DateTimeUtil.toLocalDate(latestEndDate));
			}

		}
		rs.close();
		statement.close();

		jdbcConnection.close();
		return validityPeriod;
	}

	public static String getValidFeatures(Date date, String dbTableName) throws Exception {
		/*
		 * fetch all features from table where startDate <= date and (endDate >=
		 * date || endDate = null)
		 * 
		 * then transform the featureCollection to GeoJSON! return geojsonString
		 */
		logger.info("Fetch features for validDate {} from table with name {}", date, dbTableName);
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();

		FeatureCollection features;

		SimpleFeatureSource featureSource = dataStore.getFeatureSource(dbTableName);
		features = fetchFeaturesForDate(featureSource, date);

		int validFeaturesSize = features.size();
		logger.info("Transform {} found features to GeoJSON", validFeaturesSize);

		String geoJson = null;

		if (validFeaturesSize > 0) {
			FeatureJSON toGeoJSON = new FeatureJSON();
			StringWriter writer = new StringWriter();
			toGeoJSON.writeFeatureCollection(features, writer);
			geoJson = writer.toString();
		} else {
			dataStore.dispose();
			throw new Exception("No valid features could be retrieved for the specified date.");
		}

		dataStore.dispose();

		return geoJson;
	}

	private static FeatureCollection fetchFeaturesForDate(SimpleFeatureSource featureSource, Date date)
			throws CQLException, IOException {
		// fetch all features from table where startDate <= date and (endDate >=
		// date || endDate = null)

		FilterFactory ff = new FilterFactoryImpl();
		//
		// ff.before(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
		// date);ExpressionF
		String iso8601utc = DateTimeUtil.toISO8601UTC(date);
		System.out.println(iso8601utc);

		Instant temporalInstant = new DefaultInstant(new DefaultPosition(date));

		// Simple check if property is after provided temporal instant
		Filter endDateAfter = ff.after(ff.property(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME),
				ff.literal(temporalInstant));
		Filter endDateNull = CQL.toFilter(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + " is null");
		Filter startDateBefore = ff.before(ff.property(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME),
				ff.literal(temporalInstant));

		Or endDateNullOrAfter = ff.or(endDateNull, endDateAfter);

		And andFilter = ff.and(startDateBefore, endDateNullOrAfter);

		SimpleFeatureCollection features = featureSource.getFeatures(andFilter);
		return features;
	}
	
	public static void updateGeoresourceFeatures(GeoresourcePUTInputType featureData, String dbTableName)
			throws Exception {

		PeriodOfValidityType periodOfValidity = featureData.getPeriodOfValidity();
		String geoJsonString = featureData.getGeoJsonString();
		
		updateSpatialFeaturTable(dbTableName, periodOfValidity, geoJsonString);
	}
	
	public static void updateSpatialUnitFeatures(SpatialUnitPUTInputType featureData, String dbTableName)
			throws Exception {

		PeriodOfValidityType periodOfValidity = featureData.getPeriodOfValidity();
		String geoJsonString = featureData.getGeoJsonString();
		
		updateSpatialFeaturTable(dbTableName, periodOfValidity, geoJsonString);
	}

	private static void updateSpatialFeaturTable(String dbTableName, PeriodOfValidityType periodOfValidity,
			String geoJsonString) throws IOException, Exception {
		/*
		 * idea: check all features from input:
		 * 
		 * if (feature exists in db with the same geometry), then only update
		 * the validity period else (feature does not exist at all or has
		 * different geometry) then if (only geometry changed, id remains) then
		 * insert as new feature and set validity period end date for the OLD
		 * feature else (completely new feature with new id) then insert as new
		 * feature
		 * 
		 * arisenFrom will be implemented as parameter within geoJSON dataset.
		 * Hence no geometric operations are required for now
		 */

		logger.info("Updating feature table {}.", dbTableName);

		numberOfModifiedEntries = 0;
		numberOfInsertedEntries = 0;
		numberOfEntriesMarkedAsOutdated = 0;
		inputFeaturesHaveArisonFromAttribute = false;
		
		
		Date startDate_new = DateTimeUtil.fromLocalDate(periodOfValidity.getStartDate());
		Date endDate_new = null;
		if (periodOfValidity.getEndDate() != null)
			DateTimeUtil.fromLocalDate(periodOfValidity.getEndDate());
		
		FilterFactory ff = new FilterFactoryImpl();

		FeatureJSON featureJSON = new FeatureJSON();
		SimpleFeatureType inputFeatureSchema = featureJSON.readFeatureCollectionSchema(geoJsonString, false);
		FeatureCollection inputFeatureCollection = featureJSON.readFeatureCollection(geoJsonString);

		DefaultFeatureCollection newFeaturesToBeAdded = new DefaultFeatureCollection();

		if (inputFeatureSchema.getDescriptor(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME) != null)
			inputFeaturesHaveArisonFromAttribute = true;

		DataStore store = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = store.getFeatureSource(dbTableName);
		
		handleUpdateProcess(dbTableName, startDate_new, endDate_new, ff, inputFeatureCollection, newFeaturesToBeAdded,
				featureSource);

		logger.info("Update of feature table {} was successful. Modified {} entries. Added {} new entries. Marked {} entries as outdated.",
				dbTableName, numberOfModifiedEntries, numberOfInsertedEntries, numberOfEntriesMarkedAsOutdated);		

		store.dispose();
	}

	private static void handleUpdateProcess(String dbTableName, Date startDate_new, Date endDate_new, FilterFactory ff,
			FeatureCollection inputFeatureCollection, DefaultFeatureCollection newFeaturesToBeAdded,
			SimpleFeatureSource featureSource) throws IOException, Exception {
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore sfStore = (SimpleFeatureStore) featureSource; // write
																				// access!
			
			SimpleFeatureCollection dbFeatures = featureSource.getFeatures();

			compareInputFeaturesToDbFeatures(dbTableName, startDate_new, endDate_new, ff, inputFeatureCollection,
					newFeaturesToBeAdded, dbFeatures, sfStore);
			
			/*
			 * check all dbEntries, if they might have to be assigned with a new endDate in case
			 * they are no longer present in the inputFeatures
			 */
			
			compareDbFeaturesToInputFeatures(dbTableName, startDate_new, endDate_new, ff, inputFeatureCollection,
					newFeaturesToBeAdded, dbFeatures, sfStore);
		}
	}

	private static void compareDbFeaturesToInputFeatures(String dbTableName, Date startDate_new, Date endDate_new,
			FilterFactory ff, FeatureCollection inputFeatureCollection, DefaultFeatureCollection newFeaturesToBeAdded,
			SimpleFeatureCollection dbFeatures, SimpleFeatureStore sfStore) throws Exception {
		DefaultTransaction transaction = new DefaultTransaction("Compare database features to inputFeatures and mark database features as outdated that are not valid anymore in Table " + dbTableName);
		sfStore.setTransaction(transaction);
		try {

			/*
			 * now compare each of the database features with the inputFeatures
			 * 
			 * mark all as outdated whose ID is not present in inputFeatures
			 */

			FeatureIterator dbFeaturesIterator = dbFeatures.features();

			while (dbFeaturesIterator.hasNext()) {
				Feature dbFeature = dbFeaturesIterator.next();
				if (!dbFeatureIdIsWithinInputFeatures(String.valueOf(dbFeature.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME).getValue()), inputFeatureCollection)){
					// mark as outdated by setting validEndDate to dubmitted start date
					numberOfEntriesMarkedAsOutdated++;
					Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, dbFeature);
					
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDate_new, filterForDbFeatureId);
				}

			}

			dbFeaturesIterator.close();

			transaction.commit(); // actually writes out the features in one
			// go
			transaction.close();
		} catch (Exception eek) {
			transaction.rollback();
			eek.printStackTrace();
			logger.error("An error occured while updating the feature table with name '" + dbTableName + "'. Update failed. Error message is: '" + eek.getMessage() + "'");
			throw new Exception("An error occured while updating the feature table with name '" + dbTableName + "'. Update failed. Error message is: '" + eek.getMessage() + "'");
		}
	}

	private static boolean dbFeatureIdIsWithinInputFeatures(String dbFeatureId, FeatureCollection inputFeatureCollection) {
		boolean exists = false;
		
		FeatureIterator inputFeatureIterator = inputFeatureCollection.features();
		
		while (inputFeatureIterator.hasNext()){
			Feature inputFeature = inputFeatureIterator.next();
			String inputFeatureId = String.valueOf(inputFeature.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME).getValue());
			
			if(inputFeatureId.equals(dbFeatureId)){
				inputFeatureIterator.close();
				return true;
			}			
		}
		return exists;
	}

	private static void compareInputFeaturesToDbFeatures(String dbTableName, Date startDate_new, Date endDate_new,
			FilterFactory ff, FeatureCollection inputFeatureCollection, DefaultFeatureCollection newFeaturesToBeAdded,
			SimpleFeatureCollection dbFeatures, SimpleFeatureStore sfStore) throws IOException, Exception {
		DefaultTransaction transaction = new DefaultTransaction("Compare inputFeatures to database features and modify database features where necessary in Table " + dbTableName);
		sfStore.setTransaction(transaction);
		try {

			/*
			 * now compare each of the input features with the dbFeatures
			 * 
			 * modify elements of dbFeatures, if necessary and then replace
			 * db content with modified features
			 * 
			 * collect completetly new features separately in order to add
			 * them and initialize their KomMonitor field in a second step
			 */

			FeatureIterator inputFeaturesIterator = inputFeatureCollection.features();

			while (inputFeaturesIterator.hasNext()) {
				compareInputFeatureToDbFeatures(startDate_new, endDate_new, ff, newFeaturesToBeAdded, sfStore,
						dbFeatures, inputFeaturesIterator);
			}

			inputFeaturesIterator.close();
			
			/*
			 * now deal with the completely new features and add them all to db
			 * 
			 * they will have initial validStartDate = null, validEndDate = null
			 * hence we have to modify this properties according to the input data
			 */
			insertNewFeatures(startDate_new, endDate_new, ff, newFeaturesToBeAdded, sfStore);

			transaction.commit(); // actually writes out the features in one
			// go
			transaction.close();
		} catch (Exception eek) {
			transaction.rollback();
			eek.printStackTrace();
			logger.error("An error occured while updating the feature table with name '" + dbTableName + "'. Update failed. Error message is: '" + eek.getMessage() + "'");
			throw new Exception("An error occured while updating the feature table with name '" + dbTableName + "'. Update failed. Error message is: '" + eek.getMessage() + "'");
		}
	}

	private static void insertNewFeatures(Date startDate_new, Date endDate_new, FilterFactory ff,
			DefaultFeatureCollection newFeaturesToBeAdded, SimpleFeatureStore sfStore) throws IOException {
		List<FeatureId> newFeatureIds = sfStore.addFeatures(newFeaturesToBeAdded);
		numberOfInsertedEntries = newFeatureIds.size();
		
		Set<Identifier> featureIdSet = new HashSet<Identifier>();
		featureIdSet.addAll(newFeatureIds);
		Filter filterForNewFeatures = ff.id(featureIdSet);
		sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDate_new, filterForNewFeatures);
		sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDate_new, filterForNewFeatures);
	}

	private static void compareInputFeatureToDbFeatures(Date startDate_new, Date endDate_new, FilterFactory ff,
			DefaultFeatureCollection newFeaturesToBeAdded, SimpleFeatureStore sfStore,
			SimpleFeatureCollection dbFeatures, FeatureIterator inputFeaturesIterator) throws IOException {
		Feature inputFeature = inputFeaturesIterator.next();

		Feature correspondingDbFeature = findCorrespondingDbFeatureById(inputFeature, dbFeatures);

		if (correspondingDbFeature != null) {
			// compare geometries
			compareGeometries(startDate_new, endDate_new, ff, newFeaturesToBeAdded, sfStore, inputFeature,
					correspondingDbFeature);
		}else{
			/*
			 * no corresponding db feature entry has been found.
			 * hence this feature is completely new
			 */
			newFeaturesToBeAdded.add((SimpleFeature)inputFeature);
		}
	}

	private static void compareGeometries(Date startDate_new, Date endDate_new, FilterFactory ff,
			DefaultFeatureCollection newFeaturesToBeAdded, SimpleFeatureStore sfStore, Feature inputFeature,
			Feature correspondingDbFeature) throws IOException {
		Geometry dbGeometry = (Geometry) correspondingDbFeature.getDefaultGeometryProperty().getValue();
		Geometry inputGeometry = (Geometry) inputFeature.getDefaultGeometryProperty().getValue();
		Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, correspondingDbFeature);
		if (dbGeometry.equals(inputGeometry)) {
			// same object --> only update validity period!
			// create modify statement and add to transaction!
			
			boolean wasUpdated = false;
			
			//set validStartDate, if new one is earlier
			Date dbFeatureStartDate = (Date) correspondingDbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
			if (startDate_new.before(dbFeatureStartDate)){
				wasUpdated = true;
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDate_new, filterForDbFeatureId);
			}
			
			// setvalidEndDate, if new one is later or null
			Date dbFeatureEndDate = (Date) correspondingDbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME).getValue();
			if (endDate_new == null || endDate_new.after(dbFeatureEndDate)){
				wasUpdated = true;
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDate_new, filterForDbFeatureId);
			}
			
			// increase counter if necessary
			if (wasUpdated)
				numberOfModifiedEntries++;
		} else {
			// same id but different geometry --> hence mark old object as outdated
			// and add new inputFeature to newFeaturesToBeAdded
			numberOfEntriesMarkedAsOutdated++;
			
			// to mark old feature as outdated set the validEndData to the submitted startDate of the new feature
			sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDate_new, filterForDbFeatureId);
			
			// add inputFeature to newFeaturesToBeAdded will be processed later together with all other new features
			newFeaturesToBeAdded.add((SimpleFeature)inputFeature);
		}
	}

	private static Filter createFilterForUniqueFeatureId(FilterFactory ff, Feature correspondingDbFeature) {
		String uniqueFeatureIdValue = correspondingDbFeature.getIdentifier().getID();
		String uniqueFeatureIdPropertyName = KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME;
		Set<Identifier> set = new HashSet<Identifier>();
		set.add(correspondingDbFeature.getIdentifier());
		Filter filterForFeatureId = ff.id(set);
		return filterForFeatureId;
	}

	private static Feature findCorrespondingDbFeatureById(Feature inputFeature, SimpleFeatureCollection dbFeatures) {
		SimpleFeatureIterator dbFeatureIterator = dbFeatures.features();
		
		while (dbFeatureIterator.hasNext()){
			SimpleFeature dbFeature = dbFeatureIterator.next();
			/*
			 * if both have the same id AND validEndDate is null OR in the future
			 * then we have found the feature
			 * 
			 * --> although there might be multiple features with the same ID already,
			 * only one geometry can be currently valid!!! 
			 */
			Date validEndDate = (Date) dbFeature.getAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
			Date now = new Date();
			
			String inputFeatureId = String.valueOf(inputFeature.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME).getValue());
			String dbFeatureId = String.valueOf(dbFeature.getAttribute(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME));
			if(inputFeatureId.equals(dbFeatureId)
					&& (validEndDate == null || validEndDate.after(now))){
				dbFeatureIterator.close();
				return dbFeature;
			}			
		}
		// if code reaches this, then no dvFeature was found
		return null;
	}
}
