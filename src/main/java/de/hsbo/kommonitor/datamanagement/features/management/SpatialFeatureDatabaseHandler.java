package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
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
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.temporal.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.DecodingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.precision.GeometryPrecisionReducer;

import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.util.GeometrySimplifierUtil;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
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

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		org.geojson.FeatureCollection featureCollection_jackson = objectMapper.readValue(geoJSONFeatures,
				org.geojson.FeatureCollection.class);

		FeatureCollection featureCollection = toGeoToolsFeatureCollection(featureCollection_jackson, featureSchema);
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

	private static FeatureCollection toGeoToolsFeatureCollection(
			org.geojson.FeatureCollection featureCollection_jackson, SimpleFeatureType featureSchema)
			throws JsonProcessingException, IOException {
		Iterator<org.geojson.Feature> featureIterator = featureCollection_jackson.iterator();

		FeatureJSON featureJSON = new FeatureJSON();
		ObjectMapper mapper = new ObjectMapper();

		List<SimpleFeature> geotoolsFeatures = new ArrayList<>(featureCollection_jackson.getFeatures().size());

		// Each SimpleFeature will be then read by the use of GeoTools and
		// handled separately, in order to avoid
		// parsing issues.
		while (featureIterator.hasNext()) {
			org.geojson.Feature jakcsonFeature = featureIterator.next();
			SimpleFeature simpleFeature = featureJSON.readFeature(mapper.writeValueAsString(jakcsonFeature));
			
			/*
			 * modify period of validity entries if present, to set hours/minutes/seconds to 0
			 */
			Object startDate = simpleFeature.getAttribute(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME);
			Object endDate = simpleFeature.getAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
			if(startDate != null) {
				Date startDate_date = DateTimeUtil.fromISO8601UTC((String)startDate);
				
				simpleFeature.setAttribute(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDate_date);
			}
			if(endDate != null) {
				Date endDate_date = DateTimeUtil.fromISO8601UTC((String)endDate);
				
				simpleFeature.setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDate_date);
			}
			
			try {
				boolean add = geotoolsFeatures.add(simpleFeature);

			} catch (DecodingException ex) {
				logger.error(String.format("Decoding failed for feature %s", simpleFeature.getID()));
				logger.debug(String.format("Failed feature decoding attributes: %s", simpleFeature.getAttributes()));
			}
		}

//		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
//		featureCollection.addAll(geotoolsFeatures);

		SimpleFeatureCollection collection = new ListFeatureCollection(featureSchema, geotoolsFeatures);

		return collection;

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
		Filter filter_startDate = CQL.toFilter(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + " is null");
		Filter filter_endDate = CQL.toFilter(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + " is null");

		transaction = new DefaultTransaction(
				"Modify (initialize periodOfValidity) features in Table " + featureSchema.getTypeName());
		store.setTransaction(transaction);
		try {
			store.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
					periodOfValidity.getStartDate(), filter_startDate);
			store.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, periodOfValidity.getEndDate(),
					filter_endDate);
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
		tb.setName(DatabaseHelperUtil.createUniqueTableNameForResourceType(resourceType, dataStore, ""));
		tb.setNamespaceURI(featureSchema.getName().getNamespaceURI());
		tb.setCRS(featureSchema.getCoordinateReferenceSystem());
		List<AttributeDescriptor> attributeDescriptors = featureSchema.getAttributeDescriptors();
		List<AttributeDescriptor> usableAttributeDescriptors = new ArrayList<>();
		for (AttributeDescriptor attributeDescriptor : attributeDescriptors) {
			if (!(attributeDescriptor instanceof GeometryDescriptorImpl)){
				
				usableAttributeDescriptors.add(attributeDescriptor);
			}
		}
		tb.addAll(usableAttributeDescriptors);
//		tb.setDefaultGeometry(featureSchema.getGeometryDescriptor().getLocalName());
		tb.add(featureSchema.getGeometryDescriptor().getLocalName(), Geometry.class);
		tb.setDefaultGeometry(featureSchema.getGeometryDescriptor().getLocalName());
		
		/*
		 * add KomMonitor specific properties!
		 */

		/*
		 * if property already exists then insert it as DATE!!!!!! so we must
		 * update the property type to Date
		 */

		AttributeDescriptor attributeDescriptor_startDate = tb
				.get(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME);
		AttributeDescriptor attributeDescriptor_endDate = tb
				.get(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
		AttributeDescriptor attributeDescriptor_arisenFrom = tb
				.get(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME);
		if (attributeDescriptor_startDate == null) {
			tb.add(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, java.sql.Date.class);
		} else {

			AttributeTypeBuilder builder = new AttributeTypeBuilder();
			builder.setName("DateType");
			builder.setBinding(java.sql.Date.class);
			builder.setNillable(true);
			AttributeType buildType = builder.buildType();
			attributeDescriptor_startDate = new AttributeDescriptorImpl(buildType,
					attributeDescriptor_startDate.getName(), attributeDescriptor_startDate.getMinOccurs(),
					attributeDescriptor_startDate.getMaxOccurs(), attributeDescriptor_startDate.isNillable(),
					attributeDescriptor_startDate.getDefaultValue());

			tb.set(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, attributeDescriptor_startDate);
		}

		if (attributeDescriptor_endDate == null) {
			tb.add(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, java.sql.Date.class);
		} else {

			AttributeTypeBuilder builder = new AttributeTypeBuilder();
			builder.setName("DateType");
			builder.setBinding(java.sql.Date.class);
			builder.setNillable(true);
			AttributeType buildType = builder.buildType();
			attributeDescriptor_endDate = new AttributeDescriptorImpl(buildType, attributeDescriptor_endDate.getName(),
					attributeDescriptor_endDate.getMinOccurs(), attributeDescriptor_endDate.getMaxOccurs(),
					attributeDescriptor_endDate.isNillable(), attributeDescriptor_endDate.getDefaultValue());

			tb.set(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, attributeDescriptor_endDate);
		}

		if (attributeDescriptor_arisenFrom == null) {
			tb.add(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME, String.class);
		} else {

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

	public static AvailablePeriodsOfValidityType getAvailablePeriodsOfValidity(String dbTableName)
			throws IOException, SQLException {

		Connection jdbcConnection = null;
		Statement statement = null;
		ResultSet rs = null;

		AvailablePeriodsOfValidityType validityPeriods = null;

		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			statement = jdbcConnection.createStatement();

			// EXAMPLE SELECT DISTINCT "validStartDate","validEndDate" FROM
			// "GEORESOURCE_10";
			rs = statement.executeQuery("SELECT DISTINCT \"" + KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME
					+ "\", \"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" FROM \"" + dbTableName
					+ "\"");

			validityPeriods = new AvailablePeriodsOfValidityType();
			while (rs.next()) { // check if a result was returned
				PeriodOfValidityType period = new PeriodOfValidityType();

				Date potentialStartDate = rs.getDate(1);
				Date potentialEndDate = rs.getDate(2);
				period.setStartDate(DateTimeUtil.toLocalDate(potentialStartDate));
				if (potentialEndDate != null) {
					period.setEndDate(DateTimeUtil.toLocalDate(potentialEndDate));
				}
				validityPeriods.add(period);
			}

			rs.close();
		} catch (Exception e) {
			try {
				rs.close();
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {

			}

			throw e;
		} finally {
			try {
				rs.close();
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {

			}
		}

		return validityPeriods;
	}

	public static String getAllFeatures(String dbTableName, String simplifyGeometries) throws Exception {
		/*
		 * fetch all features from table
		 * 
		 * then transform the featureCollection to GeoJSON! return geojsonString
		 */
		logger.info("Fetch all features for from table with name {}", dbTableName);
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();

		FeatureCollection features;

		SimpleFeatureSource featureSource = dataStore.getFeatureSource(dbTableName);
		features = featureSource.getFeatures();
		
		features = DateTimeUtil.fixDateResonseTypes(features);

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

	private static FeatureCollection fetchAllFeatures(SimpleFeatureSource featureSource) {
		// TODO Auto-generated method stub
		return null;
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
		
		features = DateTimeUtil.fixDateResonseTypes(features);

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
		
		LocalDate dateWithoutTime = DateTimeUtil.toLocalDate(date);

		FilterFactory ff = new FilterFactoryImpl();
		//
		// ff.before(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
		// date);ExpressionF
		// String iso8601utc = DateTimeUtil.toISO8601UTC(date);
		// System.out.println(iso8601utc);

//		Instant temporalInstant = new DefaultInstant(new DefaultPosition(date));
		

		// Simple check if property is after provided temporal instant
		Filter endDateAfterOrEqual = ff.greaterOrEqual(ff.property(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME),
				ff.literal(dateWithoutTime));
		Filter endDateNull = CQL.toFilter(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + " is null");
		Filter startDateBeforeOrEqual = ff.lessOrEqual(ff.property(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME),
				ff.literal(dateWithoutTime));

		Or endDateNullOrAfter = ff.or(endDateNull, endDateAfterOrEqual);

		And andFilter = ff.and(startDateBeforeOrEqual, endDateNullOrAfter);

		SimpleFeatureCollection features = featureSource.getFeatures(andFilter);
		return features;
	}

	public static void updateGeoresourceFeatures(GeoresourcePUTInputType featureData, String dbTableName)
			throws Exception {

		PeriodOfValidityType periodOfValidity = featureData.getPeriodOfValidity();
		String geoJsonString = featureData.getGeoJsonString();

		updateSpatialFeatureTable(dbTableName, periodOfValidity, geoJsonString);
	}

	public static void updateSpatialUnitFeatures(SpatialUnitPUTInputType featureData, String dbTableName)
			throws Exception {

		PeriodOfValidityType periodOfValidity = featureData.getPeriodOfValidity();
		String geoJsonString = featureData.getGeoJsonString();

		updateSpatialFeatureTable(dbTableName, periodOfValidity, geoJsonString);
	}

	private static void updateSpatialFeatureTable(String dbTableName, PeriodOfValidityType periodOfValidity,
			String geoJsonString) throws IOException, Exception {
		/*
		 * idea: check all features from input:
		 * 
		 * if (feature exists in db with the same geometry and identical
		 * property values), then only update the validity period else (feature
		 * does not exist at all or has different geometry or different property
		 * values) then if (only geometry/propeties changed, id remains) then
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
		// FeatureCollection inputFeatureCollection =
		// featureJSON.readFeatureCollection(geoJsonString);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		org.geojson.FeatureCollection inputFeatureCollection_jackson = objectMapper.readValue(geoJsonString,
				org.geojson.FeatureCollection.class);

		FeatureCollection inputFeatureCollection = toGeoToolsFeatureCollection(inputFeatureCollection_jackson, inputFeatureSchema);

		List<SimpleFeature> newFeaturesToBeAdded = new ArrayList<SimpleFeature>();

		if (inputFeatureSchema.getDescriptor(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME) != null)
			inputFeaturesHaveArisonFromAttribute = true;

		handleUpdateProcess(dbTableName, startDate_new, endDate_new, ff, inputFeatureSchema, inputFeatureCollection,
				newFeaturesToBeAdded);
		
		DatabaseHelperUtil.runVacuumAnalyse(dbTableName);

		logger.info(
				"Update of feature table {} was successful. Modified {} entries. Added {} new entries. Marked {} entries as outdated.",
				dbTableName, numberOfModifiedEntries, numberOfInsertedEntries, numberOfEntriesMarkedAsOutdated);
	}

	private static FeatureJSON instantiateFeatureJSON() {
		GeometryJSON geometryJSON = new GeometryJSON(
				KomMonitorFeaturePropertyConstants.NUMBER_OF_DECIMALS_FOR_GEOJSON_OUTPUT);

		return new FeatureJSON(geometryJSON);
	}

	private static void handleUpdateProcess(String dbTableName, Date startDate_new, Date endDate_new, FilterFactory ff,
			SimpleFeatureType inputFeatureSchema, FeatureCollection inputFeatureCollection,
			List<SimpleFeature> newFeaturesToBeAdded)
			throws IOException, Exception {
		
		DataStore store = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = store.getFeatureSource(dbTableName);
		
		if (featureSource instanceof SimpleFeatureStore) {			

			SimpleFeatureType dbSchema = featureSource.getSchema();
			compareSchemas(inputFeatureSchema, dbSchema, dbTableName);
			
			// reretrieve feature source if new properties were added
			if(ADDITIONAL_PROPERTIES_WERE_SET){
				store.dispose();
				store = DatabaseHelperUtil.getPostGisDataStore();
				featureSource = store.getFeatureSource(dbTableName);
				dbSchema = featureSource.getSchema();
			}		
			
			SimpleFeatureStore sfStore = (SimpleFeatureStore) featureSource; // write
			// access!
			SimpleFeatureCollection dbFeatures = featureSource.getFeatures();

			/*
			 * check all dbEntries, if they might have to be assigned with a new
			 * endDate in case they are no longer present in the inputFeatures
			 */

			compareDbFeaturesToInputFeatures(dbTableName, startDate_new, endDate_new, ff, inputFeatureCollection,
					newFeaturesToBeAdded, dbFeatures, sfStore);

			compareInputFeaturesToDbFeatures(dbTableName, startDate_new, endDate_new, ff, inputFeatureSchema, inputFeatureCollection,
					newFeaturesToBeAdded, dbFeatures, sfStore);
		}
		store.dispose();
	}

	private static void compareSchemas(SimpleFeatureType inputFeatureSchema, SimpleFeatureType dbSchema,
			String dbTableName) throws SQLException, IOException {
		/*
		 * Compare input schema to DB schema
		 * 
		 * if input schema contains any property, that is not within db schema,
		 * then add that property to DB schema (enrich feature table with new
		 * column)
		 * 
		 * only inspect those properties that are not required by KomMonitor
		 */

		List<AttributeDescriptor> inputAttributeDescriptors_original = inputFeatureSchema.getAttributeDescriptors();
		List<AttributeDescriptor> dbAttributeDescriptors_original = dbSchema.getAttributeDescriptors();

		List<AttributeDescriptor> inputAttributeDescriptors = new ArrayList<AttributeDescriptor>();
		List<AttributeDescriptor> dbAttributeDescriptors = new ArrayList<AttributeDescriptor>();

		// remove KomMonitor related properties prior to inspection
		for (AttributeDescriptor dbAttributeDescriptor : dbAttributeDescriptors_original) {
			if (!isKomMonitorAttributeDescriptor(dbAttributeDescriptor)) {
				dbAttributeDescriptors.add(dbAttributeDescriptor);
			}
		}

		for (AttributeDescriptor inputAttributeDescriptor : inputAttributeDescriptors_original) {
			if (!isKomMonitorAttributeDescriptor(inputAttributeDescriptor)) {
				inputAttributeDescriptors.add(inputAttributeDescriptor);
			}
		}

		int numberOfVerifiedDbProperties = 0;

		List<AttributeDescriptor> newProperties = new ArrayList<AttributeDescriptor>();

		for (AttributeDescriptor inputAttributeDesc : inputAttributeDescriptors) {
			boolean dbTableContainsProperty = false;
			for (AttributeDescriptor dbAttributeDesc : dbAttributeDescriptors) {
				if (inputAttributeDesc.getName().equals(dbAttributeDesc.getName())) {
					dbTableContainsProperty = true;
					// to clarify at the end, whether all db properties are
					// still present in inputFeatures schema
					numberOfVerifiedDbProperties++;
					break;
				}
			}

			if (!dbTableContainsProperty) {
				ADDITIONAL_PROPERTIES_WERE_SET = true;
				newProperties.add(inputAttributeDesc);
			}
		}

		if (numberOfVerifiedDbProperties < dbAttributeDescriptors.size()) {
			// check if the
			// obviously the inputFeatures do not have all previously defined
			// attributes
			MISSING_PROPERTIES_DETECTED = true;
		}

		// update schema in db to ensure all new columns are created
		if (ADDITIONAL_PROPERTIES_WERE_SET) {
			appendNewPropertyColumnsInDbTable(dbTableName, newProperties);
		}
	}

	private static boolean isKomMonitorAttributeDescriptor(AttributeDescriptor attributeDescriptor) {
		String name = attributeDescriptor.getName().toString();

		if (name.equals(KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME)
				|| name.equals(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME)
				|| name.equals(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME)
				|| name.equals(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME)
				|| name.equals(KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME)
				|| name.equals(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME)
				|| name.equals(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME)) {
			return true;
		} else {
			return false;
		}
	}

	private static void appendNewPropertyColumnsInDbTable(String dbTableName, List<AttributeDescriptor> newProperties)
			throws IOException, SQLException {

		Connection jdbcConnection = null;
		Statement statement = null;

		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			statement = jdbcConnection.createStatement();

			StringBuilder builder = new StringBuilder();

			builder.append("ALTER TABLE \"" + dbTableName + "\" ");

			Iterator<AttributeDescriptor> iterator = newProperties.iterator();

			while (iterator.hasNext()) {
				AttributeDescriptor property = iterator.next();

				// use dataType varchar, to import new columns as string
				builder.append("ADD COLUMN \"" + property.getName() + "\" varchar");

				if (iterator.hasNext()) {
					builder.append(", ");
				} else {
					builder.append(";");
				}
			}

			String alterTableCommand = builder.toString();

			logger.info("Send following ALTER TABLE command to database: " + alterTableCommand);

			// TODO check if works
			statement.executeUpdate(alterTableCommand);
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {

			}

			throw e;
		} finally {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {

			}
		}
	}

	private static void compareDbFeaturesToInputFeatures(String dbTableName, Date startDate_new, Date endDate_new,
			FilterFactory ff, FeatureCollection inputFeatureCollection, List<SimpleFeature> newFeaturesToBeAdded,
			SimpleFeatureCollection dbFeatures, SimpleFeatureStore sfStore) throws Exception {
		DefaultTransaction transaction = new DefaultTransaction(
				"Compare database features to inputFeatures and mark database features as outdated that are not valid anymore in Table "
						+ dbTableName);
		sfStore.setTransaction(transaction);
		try {

			/*
			 * now compare each of the database features with the inputFeatures
			 * 
			 * identify those database features, that are not within input
			 * features
			 * 
			 * compare their starting dates and end dates
			 * 
			 * only investigate features whose starting date lies within the
			 * time period of the input features
			 * 
			 * because
			 * 
			 * if db feature is another time period (historical or future) then
			 * it is irrelevant for the target time period of input features
			 * 
			 * but if db feature with a starting date within time period of
			 * input feature is found that is no longer within input collection
			 * then we must remove it from db!
			 * 
			 * --> always expect full datasets!!!!
			 */

			FeatureIterator dbFeaturesIterator = dbFeatures.features();

			while (dbFeaturesIterator.hasNext()) {
				Feature dbFeature = dbFeaturesIterator.next();

				Property dbFeatureIdProperty = dbFeature
						.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME);
				if(dbFeatureIdProperty != null){
					Object dbFeatureIdValue = dbFeatureIdProperty.getValue();
					if (!dbFeatureIdIsWithinInputFeatures(String.valueOf(dbFeatureIdValue),
							inputFeatureCollection)) {

						// compare db feature start date to input time period
						boolean dbFeatureIsWithinInputTimePeriod = false;
						Date dbFeatureStartDate = (Date) dbFeature
								.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
						Date dbFeatureEndDate = (Date) dbFeature
								.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME).getValue();

						if (dbFeatureStartDate.equals(startDate_new) || dbFeatureStartDate.after(startDate_new)) {
							// if no endDate was specified
							if (endDate_new == null) {
								dbFeatureIsWithinInputTimePeriod = true;
							} else if (dbFeatureStartDate.before(endDate_new)) {
								dbFeatureIsWithinInputTimePeriod = true;
							}
							
							if (dbFeatureIsWithinInputTimePeriod) {
								// delete the feature from db as it is no longer present
								// in the updated input feature collection for the
								// target
								// time period
								Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, dbFeature);
								sfStore.removeFeatures(filterForDbFeatureId);
							}
						}						
						
						/*
						 * now check if there is any db feature not included in the input feature set 
						 * whose startDate is before the inputTimePeriod!
						 * those we must check if their end date has to be adjusted to the new startDate
						 */
						else if (dbFeatureStartDate.before(startDate_new)){
							if (dbFeatureEndDate == null || dbFeatureEndDate.after(startDate_new)) {
								Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, dbFeature);
								sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDate_new,
										filterForDbFeatureId);
								numberOfEntriesMarkedAsOutdated++;
							}
						}
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
			logger.error("An error occured while updating the feature table with name '" + dbTableName
					+ "'. Update failed. Error message is: '" + eek.getMessage() + "'");
			throw new Exception("An error occured while updating the feature table with name '" + dbTableName
					+ "'. Update failed. Error message is: '" + eek.getMessage() + "'");
		}
	}

	private static boolean dbFeatureIdIsWithinInputFeatures(String dbFeatureId,
			FeatureCollection inputFeatureCollection) {
		boolean exists = false;

		FeatureIterator inputFeatureIterator = inputFeatureCollection.features();

		while (inputFeatureIterator.hasNext()) {
			Feature inputFeature = inputFeatureIterator.next();
			String inputFeatureId = String.valueOf(inputFeature
					.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME).getValue());

			if (inputFeatureId.equals(dbFeatureId)) {
				inputFeatureIterator.close();
				return true;
			}
		}
		return exists;
	}

	private static void compareInputFeaturesToDbFeatures(String dbTableName, Date startDate_new, Date endDate_new,
			FilterFactory ff, SimpleFeatureType inputFeatureSchema, FeatureCollection inputFeatureCollection, List<SimpleFeature> newFeaturesToBeAdded,
			SimpleFeatureCollection dbFeatures, SimpleFeatureStore sfStore) throws IOException, Exception {
		DefaultTransaction transaction = new DefaultTransaction(
				"Compare inputFeatures to database features and modify database features where necessary in Table "
						+ dbTableName);
		sfStore.setTransaction(transaction);
		try {

			/*
			 * now compare each of the input features with the dbFeatures
			 * 
			 * modify elements of dbFeatures, if necessary and then replace db
			 * content with modified features
			 * 
			 * collect completetly new features separately in order to add them
			 * and initialize their KomMonitor field in a second step
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
			 * hence we have to modify this properties according to the input
			 * data
			 */
			insertNewFeatures(startDate_new, endDate_new, ff, inputFeatureSchema, newFeaturesToBeAdded, sfStore);

			transaction.commit(); // actually writes out the features in one
			// go
			transaction.close();
		} catch (Exception eek) {
			transaction.rollback();
			eek.printStackTrace();
			logger.error("An error occured while updating the feature table with name '" + dbTableName
					+ "'. Update failed. Error message is: '" + eek.getMessage() + "'");
			throw new Exception("An error occured while updating the feature table with name '" + dbTableName
					+ "'. Update failed. Error message is: '" + eek.getMessage() + "'");
		}
	}

	private static void insertNewFeatures(Date startDate_new, Date endDate_new, FilterFactory ff,
			SimpleFeatureType inputFeatureSchema, List<SimpleFeature> newFeaturesToBeAdded, SimpleFeatureStore sfStore)
			throws IOException, CQLException {
		
		SimpleFeatureCollection collection = new ListFeatureCollection(inputFeatureSchema, newFeaturesToBeAdded);
		
		List<FeatureId> newFeatureIds = sfStore.addFeatures(collection);
		numberOfInsertedEntries = newFeatureIds.size();

		// only update those new features whose startDate and/or endDate are not
		// already set!
		// each feature might have an individual setting here in contrast to
		// global setting
		// ONLY ADJUST FILTER TO INLCUDE QUERY WHERE startDATE is null ||
		// endDATE is null

		Set<Identifier> featureIdSet = new HashSet<Identifier>();
		featureIdSet.addAll(newFeatureIds);
		Filter filter_startDateIsNull = CQL
				.toFilter("\"" + KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\" IS NULL");
		Filter filter_endDateIsNull = CQL
				.toFilter("\"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" IS NULL");
		Filter filterForNewFeatures_startDate = ff.and(ff.id(featureIdSet), filter_startDateIsNull);
		Filter filterForNewFeatures_endDate = ff.and(ff.id(featureIdSet), filter_endDateIsNull);
		sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDate_new,
				filterForNewFeatures_startDate);
		sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDate_new,
				filterForNewFeatures_endDate);
	}

	private static void compareInputFeatureToDbFeatures(Date startDate_new, Date endDate_new, FilterFactory ff,
			List<SimpleFeature> newFeaturesToBeAdded, SimpleFeatureStore sfStore,
			SimpleFeatureCollection dbFeatures, FeatureIterator inputFeaturesIterator) throws IOException {

		/*
		 * 
		 * (how to ensure that there is always exactly one valid entry for each
		 * point in time?) - endDate can only be null once - always sort all
		 * dbEntries according to startTime, then identify where the inut
		 * feature is located and decide what to do - if is most actual --> easy
		 * set as new most current, set endDate for old feature - if it most
		 * former --> easy set as new most former, endDate (if not present)
		 * cannot be further as the one of first dbEntry - if it is in the
		 * middle --> if = any startDate --> modify that entry but take care of
		 * endDate wjhich must only be as far as the next entry - if it is in
		 * the middle --> if != any startDate --> new entry but take care of
		 * endDates of former and later feature (adjust endDates and startDates
		 * if required)
		 */

		Feature inputFeature = inputFeaturesIterator.next();

		List<Feature> correspondingDbFeatures = findCorrespondingDbFeaturesById(inputFeature, dbFeatures);

		if (correspondingDbFeatures != null && correspondingDbFeatures.size() > 0) {
			// compare geometries, attributes and period of validty
			compareFeatures(startDate_new, endDate_new, ff, newFeaturesToBeAdded, sfStore, inputFeature,
					correspondingDbFeatures);
		} else {
			/*
			 * no corresponding db feature entry has been found. hence this
			 * feature is completely new
			 */
			newFeaturesToBeAdded.add((SimpleFeature) inputFeature);
		}
	}

	private static void compareFeatures(Date startDate_new, Date endDate_new, FilterFactory ff,
			List<SimpleFeature> newFeaturesToBeAdded, SimpleFeatureStore sfStore, Feature inputFeature,
			List<Feature> correspondingDbFeatures) throws IOException {

		/*
		 * 
		 * (how to ensure that there is always exactly one valid entry for each
		 * point in time?) - endDate can only be null once - always sort all
		 * dbEntries according to startTime, then identify where the inut
		 * feature is located and decide what to do - if is most actual --> easy
		 * set as new most current, set endDate for old feature - if it most
		 * former --> easy set as new most former, endDate (if not present)
		 * cannot be further as the one of first dbEntry - if it is in the
		 * middle --> if = any startDate --> modify that entry but take care of
		 * endDate wjhich must only be as far as the next entry - if it is in
		 * the middle --> if != any startDate --> new entry but take care of
		 * endDates of former and later feature (adjust endDates and startDates
		 * if required)
		 */

		Date startDateInputFeature = null;
		Date endDateInputFeature = null;

		Property startDateProperty = inputFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME);
		boolean hasValidStartDateProperty = startDateProperty != null;
		if (hasValidStartDateProperty) {
			PropertyType type = startDateProperty.getType();
			Object startDateProperyValue = startDateProperty.getValue();
			if (startDateProperyValue instanceof String) {
				startDateInputFeature = DateTimeUtil.fromISO8601UTC((String) startDateProperyValue);

			} else if (startDateProperyValue instanceof Date) {
				startDateInputFeature = (Date) startDateProperyValue;
			} else {
				startDateInputFeature = (Date) startDateProperyValue;
			}
		}
		Property endDateProperty = inputFeature.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
		boolean hasValidEndDateProperty = endDateProperty != null;
		if (hasValidEndDateProperty) {
			PropertyType type = endDateProperty.getType();
			Object endDateProperyValue = endDateProperty.getValue();
			if (endDateProperyValue instanceof String) {
				endDateInputFeature = DateTimeUtil.fromISO8601UTC((String) endDateProperyValue);

			} else if (endDateProperyValue instanceof Date) {
				endDateInputFeature = (Date) endDateProperyValue;
			} else {
				endDateInputFeature = (Date) endDateProperyValue;
			}
		}

		if (!hasValidEndDateProperty || !hasValidStartDateProperty) {
			inputFeature = reTypeFeatureToIncludeMissingKomMonitorProperties(inputFeature);
		}

		if (startDateInputFeature == null) {
			startDateInputFeature = startDate_new;

			((SimpleFeature) inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
					startDateInputFeature);
		}
		if (endDateInputFeature == null) {
			endDateInputFeature = endDate_new;
			((SimpleFeature) inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME,
					endDateInputFeature);
		}

		// sort db features according to startDate ascending
		Collections.sort(correspondingDbFeatures, new Comparator<Feature>() {
			@Override
			public int compare(Feature feat1, Feature feat2) {
				Object startDate_feat1_object = feat1
						.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
				Object startDate_feat2_object = feat2
						.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
				return ((Date) startDate_feat1_object).compareTo((Date) startDate_feat2_object);
			}
		});

		Feature latestDbFeature = correspondingDbFeatures.get(correspondingDbFeatures.size() - 1);
		Feature earliestDbFeature = correspondingDbFeatures.get(0);
		Date latestStartDateOfDBFeatures = (Date) latestDbFeature
				.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
		Date earliestStartDateOfDBFeatures = (Date) earliestDbFeature
				.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();

		// check against latest feature
		if (startDateInputFeature.after(latestStartDateOfDBFeatures)) {

			// compare geometry and properties
			// if same geometry and same properties then simply update the DB
			// feature to new endDate

			// if other geometry or other property values (or new properties)
			// then insert as new feature
			Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, latestDbFeature);

			if (hasSameGeometry(inputFeature, latestDbFeature) && hasSameProperties(inputFeature, latestDbFeature)) {
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDateInputFeature,
						filterForDbFeatureId);
				numberOfModifiedEntries++;
			} else {
				// modify endDate of dbFeature to new start date
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDateInputFeature,
						filterForDbFeatureId);
				numberOfModifiedEntries++;
				numberOfEntriesMarkedAsOutdated++;
				newFeaturesToBeAdded.add((SimpleFeature) inputFeature);
			}
		}
		// check against earliest feature
		else if (startDateInputFeature.before(earliestStartDateOfDBFeatures)) {
			// compare geometry and properties
			// if same geometry and same properties then simply update the DB
			// feature to new startDate

			// if other geometry or other property values (or new properties)
			// then insert as new feature
			Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, earliestDbFeature);

			if (hasSameGeometry(inputFeature, earliestDbFeature)
					&& hasSameProperties(inputFeature, earliestDbFeature)) {
				sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, startDateInputFeature,
						filterForDbFeatureId);
				numberOfModifiedEntries++;
			} else {
				// if endDate of input feature is unset or after startDate of
				// dbFeature then use startDate of dbFeature as new endDate
				if (endDateInputFeature == null || endDateInputFeature.after(earliestStartDateOfDBFeatures)) {
					((SimpleFeature) inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME,
							earliestStartDateOfDBFeatures);
				}
				newFeaturesToBeAdded.add((SimpleFeature) inputFeature);
			}
		}
		// must be somewhere in the middle along the timeline so there are
		// previous and later feature
		else {
			int indexOfPreviousDbFeature = -1;
			int indexOfLaterDbFeature = -1;
			int indexOfDbFeatureWithEqualStartDate = -1;

			for (int i = 0; i < correspondingDbFeatures.size(); i++) {
				Feature dbFeature = correspondingDbFeatures.get(i);
				Date dbFeatureStartDate = (Date) dbFeature
						.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();

				// find the first dbFeature whose start date is equal to input
				// feature start date
				// or is after input features start date
				if (startDateInputFeature.equals(dbFeatureStartDate)) {
					indexOfDbFeatureWithEqualStartDate = i;
					break;
				}
				if (startDateInputFeature.before(dbFeatureStartDate)) {
					indexOfLaterDbFeature = i;
					indexOfPreviousDbFeature = i - 1;
					break;
				}
			}

			if (indexOfDbFeatureWithEqualStartDate >= 0) {
				// perform an update of an existing db feature
				// make sanity checks on end date
				Feature dbFeatureToModify = correspondingDbFeatures.get(indexOfDbFeatureWithEqualStartDate);
				Filter filterForDbFeatureId = createFilterForUniqueFeatureId(ff, dbFeatureToModify);

				// sanity check on endDate
				// only if there is a subsequent feature
				if (correspondingDbFeatures.size() > (indexOfDbFeatureWithEqualStartDate + 1)) {
					Date startDateOfNextDbFeature = (Date) correspondingDbFeatures
							.get(indexOfDbFeatureWithEqualStartDate + 1)
							.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
					if (endDateInputFeature == null || endDateInputFeature.after(startDateOfNextDbFeature)) {
						endDateInputFeature = startDateOfNextDbFeature;
					}
				}

				if (hasSameGeometry(inputFeature, dbFeatureToModify)
						&& hasSameProperties(inputFeature, dbFeatureToModify)) {
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDateInputFeature,
							filterForDbFeatureId);
					numberOfModifiedEntries++;
				} else {
					// only if there is a subsequent feature
					if (correspondingDbFeatures.size() > (indexOfDbFeatureWithEqualStartDate + 1)) {
						Date startDateOfNextDbFeature = (Date) correspondingDbFeatures
								.get(indexOfDbFeatureWithEqualStartDate + 1)
								.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
						((SimpleFeature) inputFeature).setAttribute(
								KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, startDateOfNextDbFeature);
					}
					// modify each property

					Collection<Property> properties = inputFeature.getProperties();

					for (Property property : properties) {
						sfStore.modifyFeatures(property.getName(), property.getValue(), filterForDbFeatureId);
					}
					numberOfModifiedEntries++;
				}

			} else {
				Feature previousDbFeature = correspondingDbFeatures.get(indexOfPreviousDbFeature);
				Feature laterDbFeature = correspondingDbFeatures.get(indexOfLaterDbFeature);
				Filter filterForPreviousDbFeatureId = createFilterForUniqueFeatureId(ff, previousDbFeature);
				Filter filterForLaterDbFeatureId = createFilterForUniqueFeatureId(ff, laterDbFeature);

				Date previousDbFeatureEndDate = (Date) previousDbFeature
						.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME).getValue();
				Date laterDbFeatureStartDate = (Date) laterDbFeature
						.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME).getValue();
				Date laterDbFeatureEndDate = (Date) laterDbFeature
						.getProperty(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME).getValue();

				// if end date overlaps with later start date then cut if off at
				// later start date
				if (endDateInputFeature.after(laterDbFeatureStartDate)) {
					endDateInputFeature = laterDbFeatureStartDate;
					((SimpleFeature) inputFeature).setAttribute(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME,
							laterDbFeatureStartDate);
				}
				// it is a new feature insertion in the middle

				// now we know the neighbour features.
				// compare geometry and attributes to them

				// if equal then we might just update the timestamps of the
				// surrounding features

				// if not equqal, then insert as new feature and adjust
				// surrounding features

				boolean isSameGeomAndProperties_previousFeature = hasSameGeometry(inputFeature, previousDbFeature)
						&& hasSameProperties(inputFeature, previousDbFeature);
				boolean isSameGeomAndProperties_laterFeature = hasSameGeometry(inputFeature, laterDbFeature)
						&& hasSameProperties(inputFeature, laterDbFeature);

				// geom and properties are equal to both surrounding features
				if (isSameGeomAndProperties_previousFeature) {
					// make timeLine fitting for input object
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, endDateInputFeature,
							filterForPreviousDbFeatureId);
					numberOfModifiedEntries++;
				}
				if (isSameGeomAndProperties_laterFeature) {
					// make timeLine fitting for input object
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
							endDateInputFeature, filterForLaterDbFeatureId);
					numberOfModifiedEntries++;
				}

				// geom and props are not equal
				if (!isSameGeomAndProperties_previousFeature || !isSameGeomAndProperties_laterFeature) {
					// insert as new feature
					// adjust timeline of the others
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME,
							startDateInputFeature, filterForPreviousDbFeatureId);
					numberOfModifiedEntries++;
					sfStore.modifyFeatures(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME,
							endDateInputFeature, filterForLaterDbFeatureId);
					numberOfModifiedEntries++;

					newFeaturesToBeAdded.add((SimpleFeature) inputFeature);
					numberOfInsertedEntries++;
				}
			}
		}
	}

	private static Feature reTypeFeatureToIncludeMissingKomMonitorProperties(Feature inputFeature) {
		SimpleFeatureType featureType = ((SimpleFeature) inputFeature).getFeatureType();
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName(featureType.getName());
		builder.setNamespaceURI(featureType.getName().getNamespaceURI());
		builder.setCRS(featureType.getCoordinateReferenceSystem());
		builder.addAll(featureType.getAttributeDescriptors());
		builder.setDefaultGeometry(featureType.getGeometryDescriptor().getLocalName());

		AttributeDescriptor attributeDescriptor_startDate = builder
				.get(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME);
		AttributeDescriptor attributeDescriptor_endDate = builder
				.get(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
		AttributeDescriptor attributeDescriptor_arisenFrom = builder
				.get(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME);
		if (attributeDescriptor_startDate == null) {
			builder.add(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, Date.class);
		}
		if (attributeDescriptor_endDate == null) {
			builder.add(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, Date.class);
		}
		if (attributeDescriptor_arisenFrom == null) {
			builder.add(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME, String.class);
		}

		SimpleFeatureType newFeatureType = builder.buildFeatureType();

		inputFeature = DataUtilities.reType(newFeatureType, (SimpleFeature) inputFeature);
		return inputFeature;
	}

	private static boolean hasSameProperties(Feature inputFeature, Feature dbFeature) {
		// check properties and propety values

		// input feature might contain new properties

		// ADDITIONAL_PROPERTIES_WERE_SET is set in compareSchemas()
		if (ADDITIONAL_PROPERTIES_WERE_SET || MISSING_PROPERTIES_DETECTED)
			return false;
		else {
			Collection<Property> inputProperties = inputFeature.getProperties();
			Collection<Property> dbProperties = dbFeature.getProperties();

			for (Property dbProperty : dbProperties) {
				// do not compare star and end date as they might be encoded
				// differently
				if (!dbProperty.getName().toString()
						.equalsIgnoreCase(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME)
						&& !dbProperty.getName().toString()
								.equalsIgnoreCase(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME)
						&& !dbProperty.getName().toString()
								.equalsIgnoreCase(KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME)
						&& !dbProperty.getName().toString()
								.equalsIgnoreCase(KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME)
						&& !dbProperty.getName().toString()
								.equalsIgnoreCase(KomMonitorFeaturePropertyConstants.ARISEN_FROM_NAME)) {
					for (Property inputProperty : inputProperties) {
						if (dbProperty.getName().equals(inputProperty.getName())) {
							if (dbProperty.getValue() != null && !dbProperty.getValue().equals(inputProperty.getValue())) {
								return false;
							}
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
		
		// round input geometries to 6 decimals
		PrecisionModel precision = new PrecisionModel(1000000);		

		// reduce coordinates in order to prevent topology exceptions
		// test out, which number of decimals is appropriate
		// for WGS 84 inputs, the 6th decimal represents decimeter precision
		dbGeometry = GeometryPrecisionReducer.reduce(dbGeometry, precision); 
		inputGeometry = GeometryPrecisionReducer.reduce(inputGeometry, precision); 
		
		try {
			return dbGeometry.equals(inputGeometry);
		} catch (Exception e) {
			logger.error("Geometry comparison failed with error: {}", e.getMessage());
			logger.info("Geometry comparison will return false");
		}
		
		return false;
	}

	private static Filter createFilterForUniqueFeatureId(FilterFactory ff, Feature correspondingDbFeature) {
		String uniqueFeatureIdValue = correspondingDbFeature.getIdentifier().getID();
		String uniqueFeatureIdPropertyName = KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME;
		Set<Identifier> set = new HashSet<Identifier>();
		set.add(correspondingDbFeature.getIdentifier());
		Filter filterForFeatureId = ff.id(set);
		return filterForFeatureId;
	}

	private static List<Feature> findCorrespondingDbFeaturesById(Feature inputFeature,
			SimpleFeatureCollection dbFeatures) {

		// find all db features that have the sme featureIF

		// then sort them according to validStartDate
		List<Feature> identifiedDbFeatures = new ArrayList<Feature>();
		SimpleFeatureIterator dbFeatureIterator = dbFeatures.features();

		String inputFeatureId = String.valueOf(
				inputFeature.getProperty(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME).getValue());

		while (dbFeatureIterator.hasNext()) {
			SimpleFeature dbFeature = dbFeatureIterator.next();

			String dbFeatureId = String
					.valueOf(dbFeature.getAttribute(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME));
			if (inputFeatureId.equals(dbFeatureId)) {
				identifiedDbFeatures.add(dbFeature);
			}
		}

		// sort if number is more than 1
		if (identifiedDbFeatures.size() > 1) {
			identifiedDbFeatures.sort(new Comparator<Feature>() {

				@Override
				public int compare(Feature o1, Feature o2) {
					// compare by validStartDate
					Date startDate1 = (Date) o1.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME)
							.getValue();
					Date startDate2 = (Date) o2.getProperty(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME)
							.getValue();

					return startDate1.compareTo(startDate2);
				}
			});
		}

		dbFeatureIterator.close();
		return identifiedDbFeatures;
	}

	public static void deleteAllFeaturesFromFeatureTable(ResourceTypeEnum georesource, String dbTableName)
			throws Exception {
		DataStore store = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = store.getFeatureSource(dbTableName);

		SimpleFeatureType schema = featureSource.getSchema();

		List<AttributeDescriptor> dbAttributeDescriptors_original = schema.getAttributeDescriptors();

		List<AttributeDescriptor> dbAttributeDescriptors = new ArrayList<AttributeDescriptor>();

		// remove KomMonitor related properties prior to inspection
		for (AttributeDescriptor dbAttributeDescriptor : dbAttributeDescriptors_original) {
			if (!isKomMonitorAttributeDescriptor(dbAttributeDescriptor)) {
				dbAttributeDescriptors.add(dbAttributeDescriptor);
			}
		}

		store.dispose();

		Connection jdbcConnection = null;
		Statement statement = null;

		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			statement = jdbcConnection.createStatement();

			StringBuilder builder = new StringBuilder();

			builder.append("DELETE FROM \"" + dbTableName + "\"; ");

			if (dbAttributeDescriptors.size() > 0) {
				builder.append("ALTER TABLE \"" + dbTableName + "\" ");

				Iterator<AttributeDescriptor> iterator = dbAttributeDescriptors.iterator();

				while (iterator.hasNext()) {
					AttributeDescriptor property = iterator.next();

					// use dataType varchar, to import new columns as string
					builder.append("DROP COLUMN \"" + property.getName() + "\" ");

					if (iterator.hasNext()) {
						builder.append(", ");
					} else {
						builder.append(";");
					}
				}
			}

			String deleteCommand = builder.toString();

			logger.info("Send following DELETE/ALTER TABLE command to database: " + deleteCommand);

			// TODO check if works
			statement.executeUpdate(deleteCommand);
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {

			}

			throw e;
		} finally {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {

			}
		}

		logger.info("Deletion of all features and their properties from feature table {} was successful.", dbTableName);

	}
}
