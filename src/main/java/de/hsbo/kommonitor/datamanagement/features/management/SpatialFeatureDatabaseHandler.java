package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
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
import de.hsbo.kommonitor.datamanagement.api.impl.util.GeometrySimplifierUtil;
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
	
	private static boolean ADDITIONAL_PROPERTIES_WERE_SET = false;
	private static boolean MISSING_PROPERTIES_DETECTED = false;

	public static String writeGeoJSONFeaturesToDatabase(ResourceTypeEnum resourceType, String geoJSONFeatures,
			PeriodOfValidityType periodOfValidity, String correspondingMetadataDatasetId)
			throws IOException, CQLException {

		logger.info("Parsing GeoJSON into features and schema");

		FeatureJSON featureJSON = instantiateFeatureJSON();
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
			
			eek.printStackTrace();
			throw eek;
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
			eek.printStackTrace();
			throw eek;
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
		
		/*
		 * if property already exists then insert it as DATE!!!!!!
		 * so we must update the property type to Date
		 */
		
		AttributeDescriptor attributeDescriptor_startDate = tb.get(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME);
		AttributeDescriptor attributeDescriptor_endDate = tb.get(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
		AttributeDescriptor attributeDescriptor_arisenFrom = tb.get(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME);
		if(attributeDescriptor_startDate == null){
			tb.add(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, Date.class);
		}
		else {

			AttributeTypeBuilder builder = new AttributeTypeBuilder();
			builder.setName("DateType");
			builder.setBinding(Date.class);
			builder.setNillable(true);
			AttributeType buildType = builder.buildType();
			attributeDescriptor_startDate = new AttributeDescriptorImpl(buildType,
					attributeDescriptor_startDate.getName(), attributeDescriptor_startDate.getMinOccurs(),
					attributeDescriptor_startDate.getMaxOccurs(), attributeDescriptor_startDate.isNillable(),
					attributeDescriptor_startDate.getDefaultValue());
			
			tb.set(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, attributeDescriptor_startDate);
		}
			
		if(attributeDescriptor_endDate == null){
			tb.add(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, Date.class);
		}
		else {

			AttributeTypeBuilder builder = new AttributeTypeBuilder();
			builder.setName("DateType");
			builder.setBinding(Date.class);
			builder.setNillable(true);
			AttributeType buildType = builder.buildType();
			attributeDescriptor_endDate = new AttributeDescriptorImpl(buildType,
					attributeDescriptor_endDate.getName(), attributeDescriptor_endDate.getMinOccurs(),
					attributeDescriptor_endDate.getMaxOccurs(), attributeDescriptor_endDate.isNillable(),
					attributeDescriptor_endDate.getDefaultValue());

			tb.set(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, attributeDescriptor_endDate);
		}
		
		if(attributeDescriptor_arisenFrom == null){
			tb.add(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME, String.class);
		}
		else {

			AttributeTypeBuilder builder = new AttributeTypeBuilder();
			builder.setName("DateType");
			builder.setBinding(String.class);
			builder.setNillable(true);
			AttributeType buildType = builder.buildType();
			attributeDescriptor_arisenFrom = new AttributeDescriptorImpl(buildType,
					attributeDescriptor_arisenFrom.getName(), attributeDescriptor_arisenFrom.getMinOccurs(),
					attributeDescriptor_arisenFrom.getMaxOccurs(), attributeDescriptor_arisenFrom.isNillable(),
					attributeDescriptor_arisenFrom.getDefaultValue());

			tb.set(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME, attributeDescriptor_arisenFrom);
		}

		return tb.buildFeatureType();
	}

	public static void deleteFeatureTable(ResourceTypeEnum resourceType, String dbTableName) throws IOException {
		logger.info("Deleting feature table {}.", dbTableName);

		DataStore store = DatabaseHelperUtil.getPostGisDataStore();

		try {
			store.removeSchema(dbTableName);

			logger.info("Deletion of table {} was successful {}", dbTableName);
		} catch (Exception e) {
			logger.error("Error while deleting database table with name '{}'", dbTableName);
			e.printStackTrace();
		}
		

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

	public static String getValidFeatures(Date date, String dbTableName, String simplifyGeometries) throws Exception {
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
		
		features = GeometrySimplifierUtil.simplifyGeometriesAccordingToParameter(features, simplifyGeometries);

		int validFeaturesSize = features.size();
		logger.info("Transform {} found features to GeoJSON", validFeaturesSize);

		String geoJson = null;

		if (validFeaturesSize > 0) {
			FeatureJSON toGeoJSON = instantiateFeatureJSON();
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
//		String iso8601utc = DateTimeUtil.toISO8601UTC(date);
//		System.out.println(iso8601utc);

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
		 * if (feature exists in db with the same geometry and identical property values), then only update
		 * the validity period else (feature does not exist at all or has
		 * different geometry or different property values) then if (only geometry/propeties changed, id remains) then
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
		ADDITIONAL_PROPERTIES_WERE_SET = false;
		MISSING_PROPERTIES_DETECTED = false;
		
		
		Date startDate_new = DateTimeUtil.fromLocalDate(periodOfValidity.getStartDate());
		Date endDate_new = null;
		if (periodOfValidity.getEndDate() != null)
			endDate_new = DateTimeUtil.fromLocalDate(periodOfValidity.getEndDate());
		
		FilterFactory ff = new FilterFactoryImpl();

		FeatureJSON featureJSON = instantiateFeatureJSON();
		SimpleFeatureType inputFeatureSchema = featureJSON.readFeatureCollectionSchema(geoJsonString, false);
		FeatureCollection inputFeatureCollection = featureJSON.readFeatureCollection(geoJsonString);

		DefaultFeatureCollection newFeaturesToBeAdded = new DefaultFeatureCollection();

		if (inputFeatureSchema.getDescriptor(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME) != null)
			inputFeaturesHaveArisonFromAttribute = true;

		DataStore store = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = store.getFeatureSource(dbTableName);
		
		handleUpdateProcess(dbTableName, startDate_new, endDate_new, ff, inputFeatureSchema, inputFeatureCollection, newFeaturesToBeAdded,
				featureSource);

		logger.info("Update of feature table {} was successful. Modified {} entries. Added {} new entries. Marked {} entries as outdated.",
				dbTableName, numberOfModifiedEntries, numberOfInsertedEntries, numberOfEntriesMarkedAsOutdated);		

		store.dispose();
	}

	private static FeatureJSON instantiateFeatureJSON() {
		GeometryJSON geometryJSON = new GeometryJSON(KomMonitorFeaturePropertyConstants.NUMBER_OF_DECIMALS_FOR_GEOJSON_OUTPUT);
		
		return new FeatureJSON(geometryJSON);
	}

	private static void handleUpdateProcess(String dbTableName, Date startDate_new, Date endDate_new, FilterFactory ff,
			SimpleFeatureType inputFeatureSchema, FeatureCollection inputFeatureCollection, DefaultFeatureCollection newFeaturesToBeAdded,
			SimpleFeatureSource featureSource) throws IOException, Exception {
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore sfStore = (SimpleFeatureStore) featureSource; // write
																				// access!
			
			
			
			SimpleFeatureCollection dbFeatures = featureSource.getFeatures();
			
			compareSchemas(inputFeatureSchema, featureSource.getSchema(), dbTableName);

			/*
			 * check all dbEntries, if they might have to be assigned with a new endDate in case
			 * they are no longer present in the inputFeatures
			 */
			
			compareDbFeaturesToInputFeatures(dbTableName, startDate_new, endDate_new, ff, inputFeatureCollection,
					newFeaturesToBeAdded, dbFeatures, sfStore);
			
			compareInputFeaturesToDbFeatures(dbTableName, startDate_new, endDate_new, ff, inputFeatureCollection,
					newFeaturesToBeAdded, dbFeatures, sfStore);
		}
	}

	private static void compareSchemas(SimpleFeatureType inputFeatureSchema, SimpleFeatureType dbSchema,
			String dbTableName) throws SQLException, IOException {
		/*
		 * Compare input schema to DB schema
		 * 
		 * if input schema contains any property, that is not within db schema, then add that property to DB schema (enrich feature table with new column)
		 */
		
		List<AttributeDescriptor> inputAttributeDescriptors = inputFeatureSchema.getAttributeDescriptors();
		List<AttributeDescriptor> dbAttributeDescriptors = dbSchema.getAttributeDescriptors();
		int numberOfVerifiedDbProperties = 0;
		
		List<AttributeDescriptor> newProperties = new ArrayList<AttributeDescriptor>(); 
		
		for (AttributeDescriptor inputAttributeDesc : inputAttributeDescriptors) {
			boolean dbTableContainsProperty = false;
			for (AttributeDescriptor dbAttributeDesc : dbAttributeDescriptors) {
				if (inputAttributeDesc.getName().equals(dbAttributeDesc.getName())){
					dbTableContainsProperty = true;	
					// to clarify at the end, whether all db properties are still present in inputFeatures schema
					numberOfVerifiedDbProperties ++;
					break;
				}
			}
			
			if(!dbTableContainsProperty){
				ADDITIONAL_PROPERTIES_WERE_SET = true;
				newProperties.add(inputAttributeDesc);
			}
		}
		
		if(numberOfVerifiedDbProperties < dbAttributeDescriptors.size()){
			// obviously the inputFeatures do not have all previously defined attributes
			MISSING_PROPERTIES_DETECTED = true;
		}
		
		// update schema in db to ensure all new columns are created
		if(ADDITIONAL_PROPERTIES_WERE_SET){
			appendNewPropertyColumnsInDbTable(dbTableName, newProperties);
		}		
	}

	private static void appendNewPropertyColumnsInDbTable(String dbTableName, List<AttributeDescriptor> newProperties)
			throws IOException, SQLException {
		// establish JDBC connection
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
		
		Statement statement = jdbcConnection.createStatement();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("ALTER TABLE \"" + dbTableName + "\" ");
		
		Iterator<AttributeDescriptor> iterator = newProperties.iterator();
		
		while(iterator.hasNext()){
			AttributeDescriptor property = iterator.next();
			
			// use dataType varchar, to import new columns as string
			builder.append("ADD COLUMN \"" + property.getName() + "\" varchar");
			
			if(iterator.hasNext()){
				builder.append(", ");
			}
			else{
				builder.append(";");
			}
		}
		
		String alterTableCommand = builder.toString();
		
		logger.info("Send following ALTER TABLE command to database: " + alterTableCommand);
		
		// TODO check if works
		statement.executeUpdate(alterTableCommand);

		statement.close();
		jdbcConnection.close();
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
			 * identify those database features, that are not within input features
			 * 
			 * compare their starting dates and end dates
			 * 
			 * only investigate features whose starting date lies within the time period of the input features
			 * 
			 * because
			 * 
			 * if db feature is another time period (historical or future) then it is irrelevant for the target time period of input features
			 * 
			 * but if db feature with a starting date within time period of input feature is found that is no longer within input collection
			 * then we must remove it from db!
			 * 
			 * --> always expect full datasets!!!!
			 */		

			FeatureIterator dbFeaturesIterator = dbFeatures.features();

			while (dbFeaturesIterator.hasNext()) {
				Feature dbFeature = dbFeaturesIterator.next();				
				
				if (!dbFeatureIdIsWithinInputFeatures(String.valueOf(dbFeature.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME).getValue()), inputFeatureCollection)){

					// compare db feature start date to input time period
					boolean dbFeatureIsWithinInputTimePeriod = false;
					Date dbFeatureStartDate = (Date) dbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
					
					if(dbFeatureStartDate.equals(startDate_new) || dbFeatureStartDate.after(startDate_new)){
						// if no endDate was specified
						if (endDate_new == null){
							dbFeatureIsWithinInputTimePeriod = true;
						}
						else if (dbFeatureStartDate.before(endDate_new)){
							dbFeatureIsWithinInputTimePeriod = true;							
						}
					}
					
					if(dbFeatureIsWithinInputTimePeriod){
						// delete the feature from db as it is no longer present in the updated input feature collection for the target 
						// time period
						Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, dbFeature);
						sfStore.removeFeatures(filterForDbFeatureId);
					}					
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
			DefaultFeatureCollection newFeaturesToBeAdded, SimpleFeatureStore sfStore) throws IOException, CQLException {
		List<FeatureId> newFeatureIds = sfStore.addFeatures(newFeaturesToBeAdded);
		numberOfInsertedEntries = newFeatureIds.size();
		
		// only update those new features whose startDate and/or endDate are not already set!
		// each feature might have an individual setting here in contrast to global setting
		// ONLY ADJUST FILTER TO INLCUDE QUERY WHERE startDATE is null || endDATE is null
		
		Set<Identifier> featureIdSet = new HashSet<Identifier>();
		featureIdSet.addAll(newFeatureIds);
		Filter filter_startDateIsNull = CQL.toFilter("\"" + KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\" IS NULL");
		Filter filter_endDateIsNull = CQL.toFilter("\"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" IS NULL");
		Filter filterForNewFeatures_startDate = ff.and(ff.id(featureIdSet), filter_startDateIsNull);
		Filter filterForNewFeatures_endDate = ff.and(ff.id(featureIdSet), filter_endDateIsNull);
		sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDate_new, filterForNewFeatures_startDate);
		sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDate_new, filterForNewFeatures_endDate);
	}

	private static void compareInputFeatureToDbFeatures(Date startDate_new, Date endDate_new, FilterFactory ff,
			DefaultFeatureCollection newFeaturesToBeAdded, SimpleFeatureStore sfStore,
			SimpleFeatureCollection dbFeatures, FeatureIterator inputFeaturesIterator) throws IOException {
		
		/*
		 * 
		(how to ensure that there is always exactly one valid entry for each point in time?)
		  - endDate can only be null once 
		  - always sort all dbEntries according to startTime, then identify where the inut feature is located and decide what to do
			- if is most actual --> easy set as new most current, set endDate for old feature
			- if it most former --> easy set as new most former, endDate (if not present) cannot be further as the one of first dbEntry
			- if it is in the middle --> if = any startDate --> modify that entry but take care of endDate wjhich must only be as far as the next entry
			- if it is in the middle --> if != any startDate --> new entry but take care of endDates of former and later feature (adjust endDates and startDates if required)
		 */
		
		Feature inputFeature = inputFeaturesIterator.next();

		List<Feature> correspondingDbFeatures = findCorrespondingDbFeaturesById(inputFeature, dbFeatures);

		if (correspondingDbFeatures != null && correspondingDbFeatures.size() > 0) {
			// compare geometries, attributes and period of validty
			compareFeatures(startDate_new, endDate_new, ff, newFeaturesToBeAdded, sfStore, inputFeature,
					correspondingDbFeatures);
		}else{
			/*
			 * no corresponding db feature entry has been found.
			 * hence this feature is completely new
			 */
			newFeaturesToBeAdded.add((SimpleFeature)inputFeature);
		}
	}

	private static void compareFeatures(Date startDate_new, Date endDate_new, FilterFactory ff,
			DefaultFeatureCollection newFeaturesToBeAdded, SimpleFeatureStore sfStore, Feature inputFeature,
			List<Feature> correspondingDbFeatures) throws IOException {
		
		/*
		 * 
		(how to ensure that there is always exactly one valid entry for each point in time?)
		  - endDate can only be null once 
		  - always sort all dbEntries according to startTime, then identify where the inut feature is located and decide what to do
			- if is most actual --> easy set as new most current, set endDate for old feature
			- if it most former --> easy set as new most former, endDate (if not present) cannot be further as the one of first dbEntry
			- if it is in the middle --> if = any startDate --> modify that entry but take care of endDate wjhich must only be as far as the next entry
			- if it is in the middle --> if != any startDate --> new entry but take care of endDates of former and later feature (adjust endDates and startDates if required)
		 */
		
		Date startDateInputFeature = null;
		Date endDateInputFeature = null;
		
		if(inputFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME) != null){
			startDateInputFeature = (Date) inputFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();			
		}
		if(inputFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME) != null){
			endDateInputFeature = (Date) inputFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME).getValue();
		}
		
		if (startDateInputFeature == null){
			startDateInputFeature = startDate_new;
			((SimpleFeature)inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDateInputFeature);
		}
		if (endDateInputFeature == null){
			endDateInputFeature = endDate_new;
			((SimpleFeature)inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDateInputFeature);
		}		
		
		Feature latestDbFeature = correspondingDbFeatures.get(correspondingDbFeatures.size() - 1);
		Feature earliestDbFeature = correspondingDbFeatures.get(0);
		Date latestStartDateOfDBFeatures = (Date) latestDbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
		Date earliestStartDateOfDBFeatures = (Date) earliestDbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
		
		// check against latest feature
		if(startDateInputFeature.after(latestStartDateOfDBFeatures)){
			
			// compare geometry and properties
			// if same geometry and same properties then simply update the DB feature to new endDate
			
			// if other geometry or other property values (or new properties) then insert as new feature
			Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, latestDbFeature);
			
			if (hasSameGeometry(inputFeature, latestDbFeature) && hasSameProperties(inputFeature, latestDbFeature)){				
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDateInputFeature, filterForDbFeatureId);
				numberOfModifiedEntries++;
			}
			else{
				// modify endDate of dbFeature to new start date				
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDateInputFeature, filterForDbFeatureId);
				numberOfModifiedEntries++;
				numberOfEntriesMarkedAsOutdated++;
				newFeaturesToBeAdded.add((SimpleFeature)inputFeature);
			}			
		}
		// check against earliest feature
		else if (startDateInputFeature.before(earliestStartDateOfDBFeatures)){
			// compare geometry and properties
						// if same geometry and same properties then simply update the DB feature to new startDate
						
						// if other geometry or other property values (or new properties) then insert as new feature
			Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, earliestDbFeature);
			
			if (hasSameGeometry(inputFeature, earliestDbFeature) && hasSameProperties(inputFeature, earliestDbFeature)){				
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDateInputFeature, filterForDbFeatureId);
				numberOfModifiedEntries++;
			}
			else{
				// if endDate of input feature is unset or after startDate of dbFeature then use startDate of dbFeature as new endDate
				if (endDateInputFeature == null || endDateInputFeature.after(earliestStartDateOfDBFeatures)){
					((SimpleFeature)inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, earliestStartDateOfDBFeatures);
				}
				newFeaturesToBeAdded.add((SimpleFeature)inputFeature);
			}	
		}
		// must be somewhere in the middle along the timeline so there are previous and later feature
		else{
			int indexOfPreviousDbFeature = -1;
			int indexOfLaterDbFeature = -1;
			int indexOfDbFeatureWithEqualStartDate = -1;
			
			for (int i=0; i<correspondingDbFeatures.size(); i++){
				Feature dbFeature = correspondingDbFeatures.get(i);
				Date dbFeatureStartDate = (Date) dbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
				
				// find the first dbFeature whose start date is equal to input feature start date
				// or is after input features start date
				if (startDateInputFeature.equals(dbFeatureStartDate)){
					indexOfDbFeatureWithEqualStartDate = i;
					break;
				}
				if (startDateInputFeature.before(dbFeatureStartDate)){
					indexOfLaterDbFeature = i;
					indexOfPreviousDbFeature = i-1;
					break;
				}
			}
			
			if (indexOfDbFeatureWithEqualStartDate >= 0){
				// perform an update of an existing db feature
				// make sanity checks on end date
				Feature dbFeatureToModify = correspondingDbFeatures.get(indexOfDbFeatureWithEqualStartDate);
				Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, dbFeatureToModify);
				
				// sanity check on endDate
				// only if there is a subsequent feature
				if (correspondingDbFeatures.size() > (indexOfDbFeatureWithEqualStartDate + 1)){
					Date startDateOfNextDbFeature = (Date) correspondingDbFeatures.get(indexOfDbFeatureWithEqualStartDate + 1).getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
					if (endDateInputFeature == null || endDateInputFeature.after(startDateOfNextDbFeature)){
						endDateInputFeature = startDateOfNextDbFeature;
					}
				}				
				
				if (hasSameGeometry(inputFeature, dbFeatureToModify) && hasSameProperties(inputFeature, dbFeatureToModify)){									
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDateInputFeature, filterForDbFeatureId);
					numberOfModifiedEntries++;
				}
				else{
					// only if there is a subsequent feature
					if (correspondingDbFeatures.size() > (indexOfDbFeatureWithEqualStartDate + 1)){
						Date startDateOfNextDbFeature = (Date) correspondingDbFeatures.get(indexOfDbFeatureWithEqualStartDate + 1).getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
						((SimpleFeature)inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDateOfNextDbFeature);						
					}	
					// modify each property 
					
					Collection<Property> properties = inputFeature.getProperties();
					
					for (Property property : properties) {
						sfStore.modifyFeatures(property.getName(), property.getValue(), filterForDbFeatureId);						
					}
					numberOfModifiedEntries++;
				}
				
			}
			else{
				Feature previousDbFeature = correspondingDbFeatures.get(indexOfPreviousDbFeature);
				Feature laterDbFeature = correspondingDbFeatures.get(indexOfLaterDbFeature);
				Filter filterForPreviousDbFeatureId = createFilterForUniqueFeatureId(ff, previousDbFeature);
				Filter filterForLaterDbFeatureId = createFilterForUniqueFeatureId(ff, laterDbFeature);
				
				Date previousDbFeatureEndDate = (Date) previousDbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME).getValue();
				Date laterDbFeatureStartDate = (Date) laterDbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
				Date laterDbFeatureEndDate = (Date) laterDbFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME).getValue();
				
				// if end date overlaps with later start date then cut if off at later start date
				if (endDateInputFeature.after(laterDbFeatureStartDate)){
					endDateInputFeature = laterDbFeatureStartDate;
					((SimpleFeature)inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, laterDbFeatureStartDate);					
				}
				// it is a new feature insertion in the middle
				
				// now we know the neighbour features.
				// compare geometry and attributes to them
				
				// if equal then we might just update the timestamps of the surrounding features
				
				// if not equqal, then insert as new feature and adjust surrounding features
				
				boolean isSameGeomAndProperties_previousFeature = hasSameGeometry(inputFeature, previousDbFeature) && hasSameProperties(inputFeature, previousDbFeature);
				boolean isSameGeomAndProperties_laterFeature = hasSameGeometry(inputFeature, laterDbFeature) && hasSameProperties(inputFeature, laterDbFeature);
				
				// geom and properties are equal to both surrounding features
				if (isSameGeomAndProperties_previousFeature){
					// make timeLine fitting for input object
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDateInputFeature, filterForPreviousDbFeatureId);	
					numberOfModifiedEntries++;
				}
				if (isSameGeomAndProperties_laterFeature){
					// make timeLine fitting for input object
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, endDateInputFeature, filterForLaterDbFeatureId);	
					numberOfModifiedEntries++;
				}
				
				// geom and props are not equal
				if (!isSameGeomAndProperties_previousFeature || !isSameGeomAndProperties_laterFeature){
					// insert as new feature
					// adjust timeline of the others
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDateInputFeature, filterForPreviousDbFeatureId);	
					numberOfModifiedEntries++;
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, endDateInputFeature, filterForLaterDbFeatureId);	
					numberOfModifiedEntries++;
					
					newFeaturesToBeAdded.add((SimpleFeature) inputFeature);
					numberOfInsertedEntries++;
				}
			}
		}
	}

	private static boolean hasSameProperties(Feature inputFeature, Feature dbFeature) {
		// check properties and propety values
		
		// input feature might contain new properties
		
		// ADDITIONAL_PROPERTIES_WERE_SET is set in compareSchemas()
		if (ADDITIONAL_PROPERTIES_WERE_SET || MISSING_PROPERTIES_DETECTED)
			return false;
		else{
			Collection<Property> inputProperties = inputFeature.getProperties();
			Collection<Property> dbProperties = dbFeature.getProperties();
			
			for (Property dbProperty : dbProperties) {
				for (Property inputProperty : inputProperties) {
					if (dbProperty.getName().equals(inputProperty.getName())){
						if (! dbProperty.getValue().equals(inputProperty.getValue())){
							return false;
						}
					}
				}
			}
		}
		
		// if code reaches this then assume that all properties were identical
		return true;
	}

	private static boolean hasSameGeometry(Feature inputFeature, Feature dbFeature) {
		Geometry dbGeometry = (Geometry) dbFeature.getDefaultGeometryProperty().getValue();
		Geometry inputGeometry = (Geometry) inputFeature.getDefaultGeometryProperty().getValue();
		
		return dbGeometry.equals(inputGeometry);
	}

	private static Filter createFilterForUniqueFeatureId(FilterFactory ff, Feature correspondingDbFeature) {
		String uniqueFeatureIdValue = correspondingDbFeature.getIdentifier().getID();
		String uniqueFeatureIdPropertyName = KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME;
		Set<Identifier> set = new HashSet<Identifier>();
		set.add(correspondingDbFeature.getIdentifier());
		Filter filterForFeatureId = ff.id(set);
		return filterForFeatureId;
	}

	private static List<Feature> findCorrespondingDbFeaturesById(Feature inputFeature, SimpleFeatureCollection dbFeatures) {
		
		//  find all db features that have the sme featureIF
		
		// then sort them according to validStartDate 
		List<Feature> identifiedDbFeatures = new ArrayList<Feature>();
		SimpleFeatureIterator dbFeatureIterator = dbFeatures.features();
		

		String inputFeatureId = String.valueOf(inputFeature.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME).getValue());
		
		while (dbFeatureIterator.hasNext()){
			SimpleFeature dbFeature = dbFeatureIterator.next();
			
			String dbFeatureId = String.valueOf(dbFeature.getAttribute(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME));
			if(inputFeatureId.equals(dbFeatureId)){
				identifiedDbFeatures.add(dbFeature);				
			}			
		}
		
		// sort if number is more than 1
		if (identifiedDbFeatures.size() > 1){
			identifiedDbFeatures.sort(new Comparator<Feature>() 	
			{

				@Override
				public int compare(Feature o1, Feature o2) {
					// compare by validStartDate
					Date startDate1 = (Date) o1.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
					Date startDate2 = (Date) o2.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
					
					return startDate1.compareTo(startDate2);					
				}
			});
		}
		
		dbFeatureIterator.close();
		return identifiedDbFeatures;
	}
}
