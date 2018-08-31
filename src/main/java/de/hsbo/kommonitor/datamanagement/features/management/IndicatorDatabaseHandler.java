package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.temporal.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;

public class IndicatorDatabaseHandler {

	private static Logger logger = LoggerFactory.getLogger(IndicatorDatabaseHandler.class);
	
	public static String createIndicatorValueTable(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) throws IOException, CQLException, SQLException {

		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();

		List<Date> availableDatesForIndicator = collectIndicatorDates(indicatorValues);

		logger.info("Create SimpleFeatureTye for indicator");

		SimpleFeatureType featureType = createSimpleFeatureTypeForIndicators(postGisStore, ResourceTypeEnum.INDICATOR,
				availableDatesForIndicator);

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);

		logger.info("build features according to IndicatorValueMapping");

		/*
		 * A list to collect features as we create them.
		 */
		List<SimpleFeature> features = constructSimpleFeatures(indicatorValues, builder);

		logger.info("create new Table from featureSchema using table name {}", featureType.getTypeName());
		postGisStore.createSchema(featureType);

		logger.info("Start to add the actual features to table with name {}", featureType.getTypeName());

		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();

		featureCollection.addAll(features);

		persistIndicator(postGisStore, featureType, featureCollection);

		/*
		 * after writing to DB set the unique db tableName within the
		 * corresponding MetadataEntry
		 */

		postGisStore.dispose();

		return featureType.getTypeName();
	}

	private static String createOrOverwriteView(String indicatorTableName,
			String spatialUnitName) throws IOException, SQLException {
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();

		String viewTableName = "VIEW_" + indicatorTableName;

		/*
		 * CREATE VIEW vw_combined AS SELECT * FROM TABLE1 t1 JOIN TABLE2 t2 ON
		 * t2.col = t1.col
		 */
		MetadataSpatialUnitsEntity spatialUnitEntity = DatabaseHelperUtil
				.getSpatialUnitMetadataEntity(spatialUnitName);
		String spatialUnitsTable = spatialUnitEntity.getDbTableName();

		// the correct naming of the properies/columns has to be ensured within input dataset!
		String indicatorColumnName = KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME;
		String spatialUnitColumnName = KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME;
		
		
		
		String createViewCommand = "create or replace view \"" + viewTableName + "\" as select indicator.*, spatialunit." + 
				KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME + ", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME + "\", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" from \"" + indicatorTableName
				+ "\" indicator join \"" + spatialUnitsTable + "\" spatialunit on indicator.\"" 
				+ indicatorColumnName + "\" = CAST(spatialunit.\"" + spatialUnitColumnName + "\" AS varchar)";
		
		logger.info("Created the following SQL command to create or update view: '{}'", createViewCommand);
		
		// TODO check if works
		statement.executeUpdate(createViewCommand);

		statement.close();
		jdbcConnection.close();

		return viewTableName;
	}

	private static void persistIndicator(DataStore postGisStore, SimpleFeatureType featureType,
			DefaultFeatureCollection featureCollection) throws IOException {
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(featureType.getTypeName());
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) featureSource; // write
																			// access!

			Transaction transaction = new DefaultTransaction("Add features in Table " + featureType.getTypeName());
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

			logger.info("Features should have been added to table with name {}", featureType.getTypeName());

		}
	}

	private static List<SimpleFeature> constructSimpleFeatures(
			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues, SimpleFeatureBuilder builder) {
		List<SimpleFeature> features = new ArrayList<>();

		for (IndicatorPOSTInputTypeIndicatorValues indicatorEntry : indicatorValues) {
			/*
			 * type has attributes 1. spatialUnitId 2. one attribute for each
			 * timeStamp
			 */
			String spatialReferenceKey = indicatorEntry.getSpatialReferenceKey();

			builder.add(spatialReferenceKey);

			List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorEntry.getValueMapping();
			for (IndicatorPOSTInputTypeValueMapping mappingEntry : valueMapping) {
				// String dateString =
				// createDateStringForDbProperty(java.sql.Date.valueOf(timestamp));
				builder.add(mappingEntry.getIndicatorValue());
			}

			features.add(builder.buildFeature(null));
		}
		return features;
	}

	private static List<Date> collectIndicatorDates(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
		List<Date> availableDates = new ArrayList<>();

		List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorValues.get(0).getValueMapping();

		for (IndicatorPOSTInputTypeValueMapping entry : valueMapping) {
//			availableDates.add(java.sql.Date.valueOf(entry.getTimestamp()));
			availableDates.add(DateTimeUtil.fromLocalDate(entry.getTimestamp()));
		}

		return availableDates;
	}

	private static SimpleFeatureType createSimpleFeatureTypeForIndicators(DataStore dataStore,
			ResourceTypeEnum resourceType, List<Date> availableDatesForIndicator) throws IOException {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(DatabaseHelperUtil.createUniqueTableNameForResourceType(resourceType, dataStore));
		// tb.setNamespaceURI(featureSchema.getName().getNamespaceURI());
		// tb.setCRS(featureSchema.getCoordinateReferenceSystem());
		// tb.addAll(featureSchema.getAttributeDescriptors());
		// tb.setDefaultGeometry("Polygon");

		/*
		 * add KomMonitor specific properties!
		 */
		tb.add(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME, String.class);

		for (Date date : availableDatesForIndicator) {
			String dateString = createDateStringForDbProperty(date);
			tb.add(dateString, Float.class);
		}

		return tb.buildFeatureType();
	}

	private static String createDateStringForDbProperty(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		/*
		 * +1 because method return values between 0-11
		 */
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		String dateString = year + "-" + month + "-" + dayOfMonth;
		return dateString;
	}
	
	public static List<String> getAvailableDates(String dbTableName) throws IOException {
		List<String> availableDates = new ArrayList<String>();
		/*
		 * indicator db tables have two columns that are not required: - fid -
		 * spatialUnitId
		 * 
		 * the remaining columns are named by the date for which they apply.
		 * hence search for all columns except the two mentioned above
		 */
		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();

		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(dbTableName);
		SimpleFeatureType schema = featureSource.getSchema();
		List<AttributeDescriptor> attributeDescriptors = schema.getAttributeDescriptors();

		for (AttributeDescriptor attributeDescriptor : attributeDescriptors) {
			// TODO FIXME is this the correct column name?
			String attributeName = attributeDescriptor.getName().getLocalPart();

			if (!attributeName.equalsIgnoreCase(KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME)
					&& !attributeName.equalsIgnoreCase(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME))
				availableDates.add(attributeName);
		}

		postGisStore.dispose();
		
		return availableDates;

	}

	public static void updateIndicatorFeatures(IndicatorPUTInputType indicatorData, String dbTableName) throws Exception {
		/*
		 * update indicator featue table with the submitted values
		 * 
		 * if column for date already exists, then overwrite values
		 * 
		 * else if column name for date does not exists
		 * 	--> then add new column and insert values
		 */
		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(dbTableName);
		SimpleFeatureType schema = featureSource.getSchema();		
		
		List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = indicatorData.getIndicatorValues();
		/*
		 * get sample time stamps
		 */
		List<IndicatorPOSTInputTypeValueMapping> sampleValueMapping = indicatorValues.get(0).getValueMapping();
		schema = updateSchema(schema, sampleValueMapping);
		
		// update schema in db to ensure all new columns are created
		postGisStore.updateSchema(dbTableName, schema);
		
		DataAccess<SimpleFeatureType, SimpleFeature> dataStore = featureSource.getDataStore();
		
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) featureSource; // write
																			// access!
			Transaction transaction = new DefaultTransaction("Update features in Table " + dbTableName);
			
			try {

				applyModificationStatements(indicatorValues, store);

				transaction.commit(); // actually writes out the features in one
										// go
			} catch (Exception eek) {
				transaction.rollback();
				
				eek.printStackTrace();
				throw eek;
			}

			transaction.close();
		}
		
		postGisStore.dispose();
		
	}

	private static void applyModificationStatements(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues,
			SimpleFeatureStore store) throws CQLException, IOException {
		for (IndicatorPOSTInputTypeIndicatorValues indicatorValueMappingEntry : indicatorValues) {
			String spatialReferenceKey = indicatorValueMappingEntry.getSpatialReferenceKey();
			List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorValueMappingEntry
					.getValueMapping();

			for (IndicatorPOSTInputTypeValueMapping valueMappingEntry : valueMapping) {
				Date dateColumn = DateTimeUtil.fromLocalDate(valueMappingEntry.getTimestamp());
				String dateColumnName = createDateStringForDbProperty(dateColumn);
				
				Filter filter = createFilterForSpatialUnitId(spatialReferenceKey);
				
				store.modifyFeatures(dateColumnName, valueMappingEntry.getIndicatorValue(), filter);
			}
			
		}
	}
	
	private static Filter createFilterForSpatialUnitId(String spatialUnitId) throws CQLException {
		Filter filter = CQL.toFilter(KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME + " is " + spatialUnitId);
		return filter;
	}

	private static SimpleFeatureType updateSchema(SimpleFeatureType schema,
			List<IndicatorPOSTInputTypeValueMapping> sampleValueMapping) {
		/*
		 * for each timestamp within indicator value mapping
		 * 
		 * check if columns already exists
		 * 		then do nothing
		 * 
		 * if it not exists,
		 * 		then add it to schema
		 */
		SimpleFeatureTypeBuilder sftBuilder = new SimpleFeatureTypeBuilder();
		sftBuilder.setNamespaceURI(schema.getName().getNamespaceURI());
		sftBuilder.addAll(schema.getAttributeDescriptors());
		
		
		for (IndicatorPOSTInputTypeValueMapping indicatorValueMappingEntry : sampleValueMapping) {
			Date date = DateTimeUtil.fromLocalDate(indicatorValueMappingEntry.getTimestamp());
			String datePropertyName = createDateStringForDbProperty(date);
			
			if(!schemaContainsDateProperty(schema, datePropertyName)){
				// add new Property
				logger.debug("Add new propert/column '{}' to table '{}'", datePropertyName, schema.getTypeName());
				sftBuilder.add(datePropertyName, Float.class);
			}
		}
		return sftBuilder.buildFeatureType();
	}

	private static boolean schemaContainsDateProperty(SimpleFeatureType schema, String datePropertyName) {
		List<AttributeDescriptor> attributeDescriptors = schema.getAttributeDescriptors();
		
		for (AttributeDescriptor attributeDescriptor : attributeDescriptors) {
			/*
			 * TODO FIXME ist the string the correct name of the property?
			 */
			String attributeName = attributeDescriptor.getName().getLocalPart();
			if(attributeName.equals(datePropertyName))
				return true;
		}
		return false;
	}

	public static String getValidFeatures(String featureViewName, BigDecimal year, BigDecimal month, BigDecimal day) throws Exception {
		logger.info("Fetch indicator features for table with name {} and timestamp '{}-{}-{}'", featureViewName, year, month, day);
		/*
		 * here all indicators for the requested spatial unit shall be retrieved. However, the timeseries shall be reduced
		 * to only contain the requested timestamp
		 */
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();

		SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureViewName);

		Calendar cal = Calendar.getInstance();
		// -1 in month, as month is 0-based
		cal.set(year.intValue(), month.intValue() - 1, day.intValue());
		Date date = cal.getTime();
//		String datePropertyString = createDateStringForDbProperty(date);
//		/*
//		 * TODO FIXME check if that query actually works
//		 */
//		Query query = new Query(featureViewName, null, new String[] { datePropertyString });
//		FeatureCollection features = featureSource.getFeatures(query);
		
		 FeatureCollection features = fetchFeaturesForDate(featureSource, date);

		int indicatorFeaturesSize = features.size();
		logger.info("Transform {} found indicator features to GeoJSON", indicatorFeaturesSize);

		String geoJson = null;

		if (indicatorFeaturesSize > 0) {
			FeatureJSON toGeoJSON = new FeatureJSON();
			StringWriter writer = new StringWriter();
			toGeoJSON.writeFeatureCollection(features, writer);
			geoJson = writer.toString();
		} else {
			dataStore.dispose();
			throw new Exception("No features could be retrieved for the specified indicator feature table and timestamp ." 
			+ year + "-" + month + "-" + day);
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

	public static void deleteIndicatorValueTable(String dbTableName) throws IOException, SQLException {
		logger.info("Deleting indicator value table {}.", dbTableName);

//		DataStore store = DatabaseHelperUtil.getPostGisDataStore();
//
//		store.removeSchema(dbTableName);
		
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();
		
		String dropTableCommand = "drop table \"" + dbTableName + "\" CASCADE";
		
		// TODO check if works
		statement.executeUpdate(dropTableCommand);

		statement.close();
		jdbcConnection.close();

		logger.info("Deletion of table {} was successful", dbTableName);
//
//		store.dispose();
		
	}

	public static String getIndicatorFeatures(String featureViewTableName) throws Exception {

		logger.info("Fetch indicator features for table with name {}", featureViewTableName);
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();


		SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureViewTableName);

		FeatureCollection features = featureSource.getFeatures();

		int indicatorFeaturesSize = features.size();
		logger.info("Transform {} found indicator features to GeoJSON", indicatorFeaturesSize);

		String geoJson = null;

		if (indicatorFeaturesSize > 0) {
			FeatureJSON toGeoJSON = new FeatureJSON();
			StringWriter writer = new StringWriter();
			toGeoJSON.writeFeatureCollection(features, writer);
			geoJson = writer.toString();
		} else {
			dataStore.dispose();
			throw new Exception("No features could be retrieved for the specified indicator feature table.");
		}

		dataStore.dispose();

		return geoJson;
	}

//	public static void deleteIndicatorFeatureView(String featureViewTableName) throws IOException {
//		//TODO test if this works
//		logger.info("Deleting indicator feature view {}.", featureViewTableName);
//
//		DataStore store = DatabaseHelperUtil.getPostGisDataStore();
//
//		store.removeSchema(featureViewTableName);
//
//		logger.info("Deletion of view {} was successful", featureViewTableName);
//
//		store.dispose();
//		
//	}

	public static String createOrReplaceIndicatorFeatureView(String indicatorValueTableName, String spatialUnitName) throws IOException, SQLException {
		/*
		 * create view containing the geometry and indicatorValues
		 * for each indicator feature also set ViewName in Metadata
		 */
		String viewTableName = createOrOverwriteView(indicatorValueTableName,
				spatialUnitName);

//		logger.info(
//				"Modifying the indicator metadata entry with id {} to set the name of the formerly created feature database table named {} and also the created featureViewTable with name {}.",
//				correspondingMetadataDatasetId, indicatorTableName, viewTableName);
//		DatabaseHelperUtil.updateIndicatorMetadataEntry(ResourceTypeEnum.INDICATOR, correspondingMetadataDatasetId,
//				indicatorTableName, viewTableName);
		
		return viewTableName;
	}


}
