package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
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
import org.geotools.geojson.geom.GeometryJSON;
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
import de.hsbo.kommonitor.datamanagement.api.impl.util.GeometrySimplifierUtil;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPropertiesWithoutGeomType;

public class IndicatorDatabaseHandler {

	
	private static final String VALUE_SUFFIX = "_VALUES";
	private static final String VIEW_SUFFIX = "_VIEW";
	public static final String DATE_PREFIX = "DATE_";
	private static Logger logger = LoggerFactory.getLogger(IndicatorDatabaseHandler.class);
	private static boolean ADDITIONAL_PROPERTIES_WERE_SET = false;
	
	private static FeatureJSON instantiateFeatureJSON() {
		GeometryJSON geometryJSON = new GeometryJSON(KomMonitorFeaturePropertyConstants.NUMBER_OF_DECIMALS_FOR_GEOJSON_OUTPUT);
		
		return new FeatureJSON(geometryJSON);
	}
	
	public static String createIndicatorValueTable(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) throws IOException, CQLException, SQLException {

		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();

		List<Date> availableDatesForIndicator = collectIndicatorDates(indicatorValues);

		logger.info("Create SimpleFeatureType for indicator");

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

	private static String createOrOverwriteIndicatorView(String indicatorValueTableName,
			String spatialUnitName) throws IOException, SQLException {
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();

		String viewTableName = indicatorValueTableName.split(VALUE_SUFFIX)[0] + VIEW_SUFFIX;

		/*
		 * CREATE VIEW vw_combined AS SELECT * FROM TABLE1 t1 JOIN TABLE2 t2 ON
		 * t2.col = t1.col
		 */
		MetadataSpatialUnitsEntity spatialUnitEntity = DatabaseHelperUtil
				.getSpatialUnitMetadataEntityByName(spatialUnitName);
		String spatialUnitsTable = spatialUnitEntity.getDbTableName();

		// the correct naming of the properies/columns has to be ensured within input dataset!
		String indicatorColumnName = KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME;
		String spatialUnitColumnName = KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME;
		
//		String createTableCommand = "DROP TABLE IF EXISTS \"" + viewTableName + "\"; CREATE TABLE \"" + viewTableName + "\" as select indicator.*, spatialunit." + 
//				KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME + ", spatialunit.\"" + 
//				KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME + "\", spatialunit.\"" + 
//				KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\", spatialunit.\"" + 
//				KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" from \"" + indicatorTempTableName
//				+ "\" indicator join \"" + spatialUnitsTable + "\" spatialunit on indicator.\"" 
//				+ indicatorColumnName + "\" = CAST(spatialunit.\"" + spatialUnitColumnName + "\" AS varchar); " + 
//				"create sequence IF NOT EXISTS seq_" + viewTableName + " increment by 1 minvalue 0 maxvalue 1000000; ALTER TABLE \"" + viewTableName 
//				+ "\" ADD COLUMN unique_id int default nextval('seq_" + viewTableName + "');" + 
//				"UPDATE \"" + viewTableName + "\" SET unique_id=nextval('seq_" + viewTableName + "'); ALTER TABLE \"" + viewTableName 
//				+ "\" ADD PRIMARY KEY (unique_id);";
		
		String createViewCommand = "create or replace view \"" + viewTableName + "\" as select indicator.*, spatialunit." + 
				KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME + ", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME + "\", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" from \"" + indicatorValueTableName
				+ "\" indicator join \"" + spatialUnitsTable + "\" spatialunit on indicator.\"" 
				+ indicatorColumnName + "\" = CAST(spatialunit.\"" + spatialUnitColumnName + "\" AS varchar);";
		
		//INSERT into gt_metadata_pk table to enable GeoServer publishment of PK column and thus WFS 2.0 support
		
		// insert on conflict --> DO NOTHING as there already is an entry for the current viewName
		createViewCommand += " INSERT INTO gt_pk_metadata(table_schema, table_name, pk_column, pk_column_idx, pk_policy, pk_sequence)" +
				"VALUES ('public','" + viewTableName + "','" +KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME + "',1,'autogenerated',null) " + 
				"ON CONFLICT (table_schema, table_name, pk_column) DO NOTHING;";

		
		logger.info("Created the following SQL command to create or update indicator table: '{}'", createViewCommand);
		
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

			features.add(builder.buildFeature(spatialReferenceKey));
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
		tb.setName(DatabaseHelperUtil.createUniqueTableNameForResourceType(resourceType, dataStore) + VALUE_SUFFIX);
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
		
		String monthString = String.valueOf(month);
		String dayString = String.valueOf(dayOfMonth);
		
		if (month < 10)
			monthString = "0" + month;
		if (dayOfMonth < 10)
			dayString = "0" + dayOfMonth;

		String dateString = DATE_PREFIX + year + "-" + monthString + "-" + dayString;
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

			if (attributeName.contains(DATE_PREFIX)){
				/*
				 * remove date prefix from date property names
				 */
				attributeName = attributeName.split(DATE_PREFIX)[1];
				availableDates.add(attributeName);
			}
				
		}

		postGisStore.dispose();
		
		availableDates = sortDatesAscending(availableDates);
		
		return availableDates;

	}

	private static List<String> sortDatesAscending(List<String> availableDates) {
		availableDates.sort(Comparator.naturalOrder());
		return availableDates;
	}

	public static void updateIndicatorFeatures(IndicatorPUTInputType indicatorData, String indicatorDbViewName) throws Exception {
		/*
		 * update indicator featue table with the submitted values
		 * 
		 * if column for date already exists, then overwrite values
		 * 
		 * else if column name for date does not exists
		 * 	--> then add new column and insert values
		 * 
		 */
		
		String indicatorValueTableName = indicatorDbViewName.split(VIEW_SUFFIX)[0] + VALUE_SUFFIX;
		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(indicatorValueTableName);
		SimpleFeatureType schema = featureSource.getSchema();
		
		String typeName = schema.getTypeName();
		
		List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = indicatorData.getIndicatorValues();
		/*
		 * get sample time stamps
		 */
		List<IndicatorPOSTInputTypeValueMapping> sampleValueMapping = indicatorValues.get(0).getValueMapping();
//		schema = updateSchema(schema, sampleValueMapping);
		List<String> additionalPropertyNamesToAddAsFloatColumns = identifyNewProperties(schema, sampleValueMapping);
		
		postGisStore.dispose();
		
		// update schema in db to ensure all new columns are created
		if(ADDITIONAL_PROPERTIES_WERE_SET){
			// establish JDBC connection
			Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			Statement statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("ALTER TABLE \"" + indicatorValueTableName + "\" ");
			
			Iterator<String> iterator = additionalPropertyNamesToAddAsFloatColumns.iterator();
			
			while(iterator.hasNext()){
				String columnName = iterator.next();
				
				// use dataType real, as only new timeseries will be added for indicators
				builder.append("ADD COLUMN \"" + columnName + "\" real");
				
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
			
			// send ALTER TABLE statement to add new property
		}
//			postGisStore.updateSchema(typeName, schema);
		
		
		
		/*
		 * refetch schema of database table due to updated columns!
		 */
		postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		featureSource = postGisStore.getFeatureSource(indicatorValueTableName);

		DataAccess<SimpleFeatureType, SimpleFeature> dataStore = featureSource.getDataStore();
		
		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore store = (SimpleFeatureStore) featureSource; // write
																			// access!
			Transaction transaction = new DefaultTransaction("Update features in Table " + indicatorValueTableName);
			
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

	private static List<String> identifyNewProperties(SimpleFeatureType schema,
			List<IndicatorPOSTInputTypeValueMapping> sampleValueMapping) {
		List<String> newPropertyNames = new ArrayList<String>();		
		/*
		 * for each timestamp within indicator value mapping
		 * 
		 * check if columns already exists
		 * 		then do nothing
		 * 
		 * if it not exists,
		 * 		then add it to schema
		 */
		
		ADDITIONAL_PROPERTIES_WERE_SET  = false;
		
		
		for (IndicatorPOSTInputTypeValueMapping indicatorValueMappingEntry : sampleValueMapping) {
			Date date = DateTimeUtil.fromLocalDate(indicatorValueMappingEntry.getTimestamp());
			String datePropertyName = createDateStringForDbProperty(date);
			
			if(!schemaContainsDateProperty(schema, datePropertyName)){
				// add new Property
				logger.debug("Add new propert/column '{}' to table '{}'", datePropertyName, schema.getTypeName());
				newPropertyNames.add(datePropertyName);
				ADDITIONAL_PROPERTIES_WERE_SET = true;
			}
		}
		
		return newPropertyNames;
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
		Filter filter = CQL.toFilter(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + " = '" + spatialUnitId + "'");
		return filter;
	}

//	private static SimpleFeatureType updateSchema(SimpleFeatureType schema,
//			List<IndicatorPOSTInputTypeValueMapping> sampleValueMapping) {
//		/*
//		 * for each timestamp within indicator value mapping
//		 * 
//		 * check if columns already exists
//		 * 		then do nothing
//		 * 
//		 * if it not exists,
//		 * 		then add it to schema
//		 */
//		SimpleFeatureTypeBuilder sftBuilder = new SimpleFeatureTypeBuilder();
//		sftBuilder.setName(schema.getName());
////		sftBuilder.setNamespaceURI(schema.getName().getNamespaceURI());
//		sftBuilder.addAll(schema.getAttributeDescriptors());
//		
//		ADDITIONAL_PROPERTIES_WERE_SET  = false;
//		
//		
//		for (IndicatorPOSTInputTypeValueMapping indicatorValueMappingEntry : sampleValueMapping) {
//			Date date = DateTimeUtil.fromLocalDate(indicatorValueMappingEntry.getTimestamp());
//			String datePropertyName = createDateStringForDbProperty(date);
//			
//			if(!schemaContainsDateProperty(schema, datePropertyName)){
//				// add new Property
//				logger.debug("Add new propert/column '{}' to table '{}'", datePropertyName, schema.getTypeName());
//				sftBuilder.add(datePropertyName, Float.class);
//				ADDITIONAL_PROPERTIES_WERE_SET = true;
//			}
//		}
//		
//		if(! ADDITIONAL_PROPERTIES_WERE_SET)
//			return schema;
//		else
//			return sftBuilder.buildFeatureType();
//	}

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

	public static String getValidFeatures(String featureViewName, BigDecimal year, BigDecimal month, BigDecimal day, String simplifyGeometries) throws Exception {
		logger.info("Fetch indicator features for table with name {} and timestamp '{}-{}-{}' and simplificationType '{}'", featureViewName, year, month, day, simplifyGeometries);
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
		 
		 features = GeometrySimplifierUtil.simplifyGeometriesAccordingToParameter(features, simplifyGeometries);

		int indicatorFeaturesSize = features.size();
		logger.info("Transform {} found indicator features to GeoJSON", indicatorFeaturesSize);

		String geoJson = null;

		if (indicatorFeaturesSize > 0) {
			FeatureJSON toGeoJSON = instantiateFeatureJSON();
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

	public static void deleteIndicatorValueTable(String dbViewName) throws IOException, SQLException {
		logger.info("Deleting indicator value table {}.", dbViewName);

//		DataStore store = DatabaseHelperUtil.getPostGisDataStore();
//
//		store.removeSchema(dbTableName);
		
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();
		
		String dropTableCommand = "drop view \"" + dbViewName + "\" CASCADE;";
		String valueTableName = dbViewName.split(VIEW_SUFFIX)[0] + VALUE_SUFFIX;
		dropTableCommand += "drop table \"" + valueTableName + "\" CASCADE";
		
		// TODO check if works
		statement.executeUpdate(dropTableCommand);

		statement.close();
		jdbcConnection.close();

		logger.info("Deletion of table {} and view {} was successful", valueTableName, dbViewName);
//
//		store.dispose();
		
	}

	public static String getIndicatorFeatures(String featureViewTableName, String simplifyGeometries) throws Exception {

		logger.info("Fetch indicator features for table with name {}", featureViewTableName);
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();


		SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureViewTableName);

		FeatureCollection features = featureSource.getFeatures();
		
		features = GeometrySimplifierUtil.simplifyGeometriesAccordingToParameter(features, simplifyGeometries);

		int indicatorFeaturesSize = features.size();
		logger.info("Transform {} found indicator features to GeoJSON", indicatorFeaturesSize);

		String geoJson = null;

		if (indicatorFeaturesSize > 0) {
			FeatureJSON toGeoJSON = instantiateFeatureJSON();
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

	public static String createOrReplaceIndicatorView(String indicatorValueTableName, String spatialUnitName) throws IOException, SQLException {
		/*
		 * create view containing the geometry and indicatorValues
		 * for each indicator feature also set ViewName in Metadata
		 */
		String viewTableName = createOrOverwriteIndicatorView(indicatorValueTableName,
				spatialUnitName);

//		logger.info(
//				"Modifying the indicator metadata entry with id {} to set the name of the formerly created feature database table named {} and also the created featureViewTable with name {}.",
//				correspondingMetadataDatasetId, indicatorTableName, viewTableName);
//		DatabaseHelperUtil.updateIndicatorMetadataEntry(ResourceTypeEnum.INDICATOR, correspondingMetadataDatasetId,
//				indicatorTableName, viewTableName);
		
		return viewTableName;
	}

	public static List<Float> getAllIndicatorValues(String indicatorValueTableName, String datePropertyName) throws SQLException, IOException {
		
		List<Float> indicatorValues = new ArrayList<Float>();
		
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();
		
		if(!datePropertyName.startsWith(DATE_PREFIX)){
			datePropertyName = DATE_PREFIX + datePropertyName;
		}
		
		String createTableCommand = "SELECT \"" + datePropertyName + "\" FROM \"" + indicatorValueTableName + "\";";
		
		logger.info("Created the following SQL command to create or update indicator table: '{}'", createTableCommand);
		
		ResultSet result = statement.executeQuery(createTableCommand);
		
		while(result.next()){
			indicatorValues.add(result.getFloat(datePropertyName));
		}
		
		result.close();
		statement.close();
		jdbcConnection.close();

		return indicatorValues;
	}

	public static FeatureCollection getValidFeaturesAsFeatureCollection(DataStore dataStore, String indicatorValueTableName,
			BigDecimal year, BigDecimal month, BigDecimal day) throws IOException, CQLException {
		logger.info("Fetch indicator features as FeatureCollection for table with name {} and timestamp '{}-{}-{}'", indicatorValueTableName, year, month, day);
		/*
		 * here all indicators for the requested spatial unit shall be retrieved. However, the timeseries shall be reduced
		 * to only contain the requested timestamp
		 */

		SimpleFeatureSource featureSource = dataStore.getFeatureSource(indicatorValueTableName);

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

		return features;
	}

	public static List<IndicatorPropertiesWithoutGeomType> getIndicatorFeaturePropertiesWithoutGeometries(
			String indicatorValueTableName) throws SQLException, IOException {
		logger.info("Fetch indicator feature properties without geometry for table with name {}", indicatorValueTableName);

		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<IndicatorPropertiesWithoutGeomType>();
		
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();

		
		String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorValueTableName + "\";";
		
		logger.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
		
		ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
		
		addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
		
		result.close();
		statement.close();
		jdbcConnection.close();

		return indicatorFeaturePropertiesWithoutGeom;
	}

	public static List<IndicatorPropertiesWithoutGeomType> getValidFeaturePropertiesWithoutGeometries(
			String indicatorValueTableName, BigDecimal year, BigDecimal month, BigDecimal day) throws IOException, SQLException {
		logger.info("Fetch indicator feature properties without geometry for table with name {}", indicatorValueTableName);

		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<IndicatorPropertiesWithoutGeomType>();
		
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();
		
		Calendar cal = Calendar.getInstance();
		// -1 in month, as month is 0-based
		cal.set(year.intValue(), month.intValue() - 1, day.intValue());
		Date date = cal.getTime();
		
		String dateString = createDateStringForDbProperty(date);
		dateString = dateString.split(DATE_PREFIX)[1];
		
		String whereClause = "WHERE \"" + KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\"::DATE <= '" + dateString + "'::DATE AND (\"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" is NULL OR \"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\"::DATE >= '" + dateString + "'::DATE)";
		
		String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorValueTableName + "\" " + whereClause + ";";
		
		logger.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
		
		ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
		
		addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
		
		result.close();
		statement.close();
		jdbcConnection.close();

		return indicatorFeaturePropertiesWithoutGeom;
	}

	private static void addPropertiesWithoutGeometry(
			List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom, ResultSet result)
			throws SQLException {
		int columnCount = result.getMetaData().getColumnCount();
		
		while(result.next()){
			
			IndicatorPropertiesWithoutGeomType featureProps = new IndicatorPropertiesWithoutGeomType();
			
			featureProps.setId(result.getString(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME));
			featureProps.setName(result.getString(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME));
			featureProps.setValidStartDate(result.getString(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME));
			featureProps.setValidEndDate(result.getString(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME));
			
			//result set index start with 1!
			for(int i=1; i<=columnCount; i++){

				 if(result.getMetaData().getColumnName(i).equalsIgnoreCase(KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME)){
					// ignore an do nothing
				}
				else{
					featureProps.put(result.getMetaData().getColumnName(i), result.getString(i));
				}
				
			}			
			
			indicatorFeaturePropertiesWithoutGeom.add(featureProps);
		}
	}

}
