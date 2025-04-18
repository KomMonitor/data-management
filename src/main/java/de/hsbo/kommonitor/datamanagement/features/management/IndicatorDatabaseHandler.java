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
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Or;
import org.geotools.api.temporal.Instant;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.simple.SimpleFeatureCollection;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.api.impl.util.GeometrySimplifierUtil;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPUTInputType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPropertiesWithoutGeomType;

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
		
		Connection jdbcConnection = null;
		Statement statement = null;
		String viewTableName = getViewTableNameFromValueTableName(indicatorValueTableName);
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();

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
			String uniqueFidColumnName = KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME;
			
//			String createViewCommand = "drop view if exists \"" + viewTableName + "\"; create view \"" + viewTableName + "\" as select indicator.*, spatialunit." + 
//					KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME + ", spatialunit.\"" + 
//					KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME + "\", spatialunit.\"" + 
//					KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\", spatialunit.\"" + 
//					KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" from \"" + indicatorValueTableName
//					+ "\" indicator join \"" + spatialUnitsTable + "\" spatialunit on indicator.\"" 
//					+ indicatorColumnName + "\" = CAST(spatialunit.\"" + spatialUnitColumnName + "\" AS varchar);";
			
			// recreate view and INSERT into gt_metadata_pk table to enable GeoServer publishment of PK column and thus WFS 2.0 support
			// insert on conflict --> DO NOTHING as there already is an entry for the current viewName
			
			String createViewCommand = "DO $$ " + "DECLARE str text;";
			createViewCommand += " begin ";
			createViewCommand += "str := ";
			createViewCommand += "format('drop view if exists \"" + viewTableName + "\"; create view \"" + viewTableName + "\" as select indicator.*, %s from \"" + indicatorValueTableName
					+ "\" indicator join \"" + spatialUnitsTable + "\" spatialunit on indicator.\"" 
					+ indicatorColumnName + "\" = CAST(spatialunit.\"" + spatialUnitColumnName + "\" AS varchar);"
							+ "', "
							+ "array_to_string(Array(SELECT 'spatialunit' || '.\"' || c.column_name || '\"'"
							+ "        FROM information_schema.columns As c"
							+ "            WHERE table_name = '" + spatialUnitsTable + "' "
							+ "            AND  c.column_name NOT IN('" + spatialUnitColumnName + "', '" + uniqueFidColumnName + "')), ', '));";
			
			
						createViewCommand += " ";
			
			createViewCommand += "EXECUTE str;";
					
			createViewCommand += " END $$;  ";
			
			createViewCommand += "INSERT INTO gt_pk_metadata(table_schema, table_name, pk_column, pk_column_idx, pk_policy, pk_sequence)"
					+ "	VALUES ('public','" + viewTableName + "','" + uniqueFidColumnName + "',1,'autogenerated',null) "
					+ " ON CONFLICT (table_schema, table_name, pk_column) DO NOTHING;";

			
			logger.info("Created the following SQL command to create or update indicator table: '{}'", createViewCommand);
			
			// TODO check if works
			statement.execute(createViewCommand);
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}
		

		return viewTableName;
	}

	private static String getViewTableNameFromValueTableName(String indicatorValueTableName) {
		return indicatorValueTableName.split(VALUE_SUFFIX)[0] + VIEW_SUFFIX;
	}

	private static void persistIndicator(DataStore postGisStore, SimpleFeatureType featureType,
			DefaultFeatureCollection featureCollection) throws IOException {
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(featureType.getTypeName());
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

	private static List<SimpleFeature> constructSimpleFeatures(
			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues, SimpleFeatureBuilder builder) {
		List<SimpleFeature> features = new ArrayList<>();

		if(indicatorValues != null && indicatorValues.size() > 0){
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
					try {
						builder.add(mappingEntry.getIndicatorValue());
					} catch (Exception ex) {
						logger.error("Error while building feature.", ex);
					}

				}

				features.add(builder.buildFeature(spatialReferenceKey));
			}
		}
		
		return features;
	}

	private static List<Date> collectIndicatorDates(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
		List<Date> availableDates = new ArrayList<>();

		if(indicatorValues == null || indicatorValues.size() == 0){
			logger.info("submitted post body included null or empty list of indicatorValues. Hence no timestamp values can be created.");
		}
		else{
			List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorValues.get(0).getValueMapping();

			for (IndicatorPOSTInputTypeValueMapping entry : valueMapping) {
//				availableDates.add(java.sql.Date.valueOf(entry.getTimestamp()));
				availableDates.add(DateTimeUtil.fromLocalDate(entry.getTimestamp()));
			}
		}
		

		return availableDates;
	}

	private static SimpleFeatureType createSimpleFeatureTypeForIndicators(DataStore dataStore,
			ResourceTypeEnum resourceType, List<Date> availableDatesForIndicator) throws IOException {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(DatabaseHelperUtil.createUniqueTableNameForResourceType(resourceType, dataStore, VALUE_SUFFIX));
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

	public static String createDateStringForDbProperty(Date date) {
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
	
	public static List<String> getAvailableDates(String viewTableName) throws IOException {
		List<String> availableDates = new ArrayList<String>();
		/*
		 * indicator db tables have two columns that are not required: - fid -
		 * spatialUnitId
		 * 
		 * the remaining columns are named by the date for which they apply.
		 * hence search for all columns except the two mentioned above
		 */
		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();

		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(viewTableName);
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
		 * update indicator feature table with the submitted values
		 * 
		 * if column for date already exists, then overwrite values
		 * 
		 * else if column name for date does not exist
		 * 	--> then add new column and insert values
		 * 
		 */
		
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorDbViewName);
		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(indicatorValueTableName);
		SimpleFeatureType schema = featureSource.getSchema();
		
		String typeName = schema.getTypeName();
		
		List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = indicatorData.getIndicatorValues();
		if(indicatorValues == null || indicatorValues.size() == 0){
			logger.info("submitted put body included null or empty list of indicatorValues. Hence no changes can be applied.");
			throw new Exception("submitted put body included null or empty list of indicatorValues");
		}
		else if(indicatorValues.size() > 0){
			
			
			/*
			 * get sample time stamps
			 */
			List<IndicatorPOSTInputTypeValueMapping> sampleValueMapping = indicatorValues.get(0).getValueMapping();
//			schema = updateSchema(schema, sampleValueMapping);
			List<String> additionalPropertyNamesToAddAsFloatColumns = identifyNewProperties(schema, sampleValueMapping);
			
			postGisStore.dispose();
			
			// update schema in db to ensure all new columns are created
			if(ADDITIONAL_PROPERTIES_WERE_SET){
				
				addNewColumnsToTable(indicatorValueTableName, additionalPropertyNamesToAddAsFloatColumns);

			}
			
			addDataToTable(indicatorValueTableName, indicatorValues);
			
			
			// RUN VACUUM ANALYSE
			DatabaseHelperUtil.runVacuumAnalyse(indicatorValueTableName);
		}
		
	}

	private static void addDataToTable(String indicatorValueTableName,
			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) throws IOException, Exception {
		DataStore postGisStore;
		SimpleFeatureSource featureSource;
		/*
		 * refetch schema of database table due to updated columns!
		 */
		postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		featureSource = postGisStore.getFeatureSource(indicatorValueTableName);

		DataAccess<SimpleFeatureType, SimpleFeature> dataStore = featureSource.getDataStore();
		
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
		
		postGisStore.dispose();
	}

//	private static void addDataToTable(String indicatorValueTableName,
//			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
//		DefaultFeatureCollection newFeaturesToBeAdded = new DefaultFeatureCollection();
//		
//		if(indicatorValues != null && indicatorValues.size() > 0){
//			for (IndicatorPOSTInputTypeIndicatorValues indicatorValueMappingEntry : indicatorValues) {
//				String spatialReferenceKey = indicatorValueMappingEntry.getSpatialReferenceKey();
//				Filter filter = createFilterForSpatialUnitId(spatialReferenceKey);
//				List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorValueMappingEntry
//						.getValueMapping();
//				
//				// no existing feature was found for the current spatial ref key
//				// hence add to newFeaturesToBeAdded;
//				if(store.getFeatures(filter).isEmpty()){
//					SimpleFeatureType featureType = store.getSchema();
//					
//					SimpleFeatureBuilder sfBuilder = new SimpleFeatureBuilder(featureType);
//					sfBuilder.set(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME, spatialReferenceKey);
//					for (IndicatorPOSTInputTypeValueMapping valueMappingEntry : valueMapping) {
//						Date dateColumn = DateTimeUtil.fromLocalDate(valueMappingEntry.getTimestamp());
//						String dateColumnName = createDateStringForDbProperty(dateColumn);			
//						sfBuilder.set(dateColumnName, valueMappingEntry.getIndicatorValue());
//					}
//					newFeaturesToBeAdded.add(sfBuilder.buildFeature(null));
//				}
//				else{
//					for (IndicatorPOSTInputTypeValueMapping valueMappingEntry : valueMapping) {
//						Date dateColumn = DateTimeUtil.fromLocalDate(valueMappingEntry.getTimestamp());
//						String dateColumnName = createDateStringForDbProperty(dateColumn);			
//						store.modifyFeatures(dateColumnName, valueMappingEntry.getIndicatorValue(), filter);
//					}
//				}	
//			}
//		}
//		
//		
//		// add any new features id required
//		if (newFeaturesToBeAdded.size() > 0){
//			store.addFeatures(newFeaturesToBeAdded);
//		}
//		
//	}

	private static void addNewColumnsToTable(String indicatorValueTableName,
			List<String> additionalPropertyNamesToAddAsFloatColumns) throws Exception {
		Connection jdbcConnection = null;
		Statement alterTableStmt = null;
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			alterTableStmt = jdbcConnection.createStatement();
			
			Iterator<String> iterator = additionalPropertyNamesToAddAsFloatColumns.iterator();
			
			while(iterator.hasNext()){
				String columnName = iterator.next();
				alterTableStmt.addBatch("ALTER TABLE \"" + indicatorValueTableName + "\" ADD COLUMN \"" + columnName + "\" real");
			}
			
			logger.info("Adding new DATABASE COLUMNS...");
			alterTableStmt.executeBatch();

		} catch (Exception e) {
			try {
				alterTableStmt.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				alterTableStmt.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}
	}

	private static String getValueTableNameFromViewTableName(String indicatorDbViewName) {
		return indicatorDbViewName.split(VIEW_SUFFIX)[0] + VALUE_SUFFIX;
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
		
		DefaultFeatureCollection newFeaturesToBeAdded = new DefaultFeatureCollection();
		
		SimpleFeatureCollection existingFeatures = store.getFeatures();
		
		if(indicatorValues != null && indicatorValues.size() > 0){
			for (IndicatorPOSTInputTypeIndicatorValues indicatorValueMappingEntry : indicatorValues) {
				String spatialReferenceKey = indicatorValueMappingEntry.getSpatialReferenceKey();
				Filter filter = createFilterForSpatialUnitId(spatialReferenceKey);
				List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorValueMappingEntry
						.getValueMapping();
				
				// no existing feature was found for the current spatial ref key
				// hence add to newFeaturesToBeAdded;
				if(isNotInExistingFeatures(filter, existingFeatures)){
					SimpleFeatureType featureType = store.getSchema();
					
					SimpleFeatureBuilder sfBuilder = new SimpleFeatureBuilder(featureType);
					sfBuilder.set(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME, spatialReferenceKey);
					for (IndicatorPOSTInputTypeValueMapping valueMappingEntry : valueMapping) {
						Date dateColumn = DateTimeUtil.fromLocalDate(valueMappingEntry.getTimestamp());
						String dateColumnName = createDateStringForDbProperty(dateColumn);			
						sfBuilder.set(dateColumnName, valueMappingEntry.getIndicatorValue());
					}
					newFeaturesToBeAdded.add(sfBuilder.buildFeature(null));
				}
				else{
					List<String> columnNames = new ArrayList<String>(valueMapping.size());
					List<Object> columnValues = new ArrayList<Object>(valueMapping.size());
					
					for (IndicatorPOSTInputTypeValueMapping valueMappingEntry : valueMapping) {
						Date dateColumn = DateTimeUtil.fromLocalDate(valueMappingEntry.getTimestamp());
						String dateColumnName = createDateStringForDbProperty(dateColumn);
						
						columnNames.add(dateColumnName);
						columnValues.add(valueMappingEntry.getIndicatorValue());
//						store.modifyFeatures(dateColumnName, valueMappingEntry.getIndicatorValue(), filter);
					}
					
					String[] names = new String[columnNames.size()];
					names = columnNames.toArray(names);
					
					Object[] values = new Object[columnValues.size()];
					values = columnValues.toArray(values);
					
					store.modifyFeatures(names, values, filter);
				}	
			}
		}
		
		
		
		
		// add any new features id required
		if (newFeaturesToBeAdded.size() > 0){
			store.addFeatures(newFeaturesToBeAdded);
		}
	}	
	
	private static boolean isNotInExistingFeatures(Filter filter, SimpleFeatureCollection existingFeatures) {
		// TODO Auto-generated method stub
		return existingFeatures.subCollection(filter).isEmpty();
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
		 
		 features = DateTimeUtil.fixDateResonseTypes(features);
		 
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
		Filter startDateEqual = ff.equals(ff.property(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME),
				ff.literal(temporalInstant));

		Or endDateNullOrAfter = ff.or(endDateNull, endDateAfter);
		
		Or startDateEqualOrBefore = ff.or(startDateBefore, startDateEqual);

		And andFilter = ff.and(startDateEqualOrBefore, endDateNullOrAfter);

		SimpleFeatureCollection features = featureSource.getFeatures(andFilter);
		return features;
	}

	public static void deleteIndicatorValueTable(String dbViewName) throws IOException, SQLException {
		logger.info("Deleting indicator value table {}.", dbViewName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();
			
			String dropTableCommand = "drop view \"" + dbViewName + "\" CASCADE;";
			String valueTableName = getValueTableNameFromViewTableName(dbViewName);
			dropTableCommand += "drop table \"" + valueTableName + "\" CASCADE";
			
			// TODO check if works
			statement.executeUpdate(dropTableCommand);

			logger.info("Deletion of table {} and view {} was successful", valueTableName, dbViewName);
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}		
		
	}
	
	public static void deleteIndicatorTimeStamp(String indicatorDbViewName, BigDecimal year, BigDecimal month, BigDecimal day) throws Exception {
		/*
		 * delete column for the given timestamp
		 * 
		 */
		
		boolean foundExistingDateColumn = false;
		
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorDbViewName);
		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		SimpleFeatureSource featureSource = postGisStore.getFeatureSource(indicatorValueTableName);
		SimpleFeatureType schema = featureSource.getSchema();
		
		Date date = new GregorianCalendar(year.intValue(), month.intValue() - 1, day.intValue()).getTime();
		logger.info("parsing date from submitted date components. Submitted components were 'year: {}, month: {}, day: {}'. As Java time treats month 0-based, the follwing date will be used: 'year-month(-1)-day {}-{}-{}'", year, month, day, year, month.intValue()-1, day);
		String datePropertyName = createDateStringForDbProperty(date);
		
		if(schemaContainsDateProperty(schema, datePropertyName)){
			// add new Property
			logger.debug("Found matching date column for propert/column '{}' of table '{}'", datePropertyName, schema.getTypeName());
			foundExistingDateColumn = true;
		}
		
		postGisStore.dispose();
		
		// update schema in db to ensure all new columns are created
		if(foundExistingDateColumn){
			
			Connection jdbcConnection = null;
			Statement statement = null;
			
			try {
				// establish JDBC connection
				jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
				
				statement = jdbcConnection.createStatement();
				
				StringBuilder builder = new StringBuilder();
				
				builder.append("ALTER TABLE \"" + indicatorValueTableName + "\" ");

				String columnName = datePropertyName;

				// use dataType real, as only new timeseries will be added for
				// indicators
				builder.append("DROP COLUMN \"" + columnName + "\" CASCADE;");

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
			} finally{
				try {
					statement.close();
					jdbcConnection.close();
				} catch (Exception e2) {
					
				}
			}

		}
		
	}

	public static String getIndicatorFeatures(String featureViewTableName, String simplifyGeometries) throws Exception {

		logger.info("Fetch indicator features for table with name {}", featureViewTableName);
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();


		SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureViewTableName);

		FeatureCollection features = featureSource.getFeatures();
		
		features = DateTimeUtil.fixDateResonseTypes(features);
		
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

	public static String createOrReplaceIndicatorView_fromValueTableName(String indicatorValueTableName, String spatialUnitName) throws IOException, SQLException {
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
	
	public static String createOrReplaceIndicatorView_fromViewTableName(String indicatorViewTableName,
			String spatialUnitName) throws IOException, SQLException {
		String valueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		return createOrOverwriteIndicatorView(valueTableName, spatialUnitName);
	}

	public static List<Float> getAllIndicatorValues(String indicatorValueTableName, String datePropertyName) throws SQLException, IOException {
		
		List<Float> indicatorValues = new ArrayList<Float>();
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();
			
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
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}

		return indicatorValues;
	}

	public static FeatureCollection getValidFeaturesAsFeatureCollection(DataStore dataStore, String indicatorViewTableName,
			BigDecimal year, BigDecimal month, BigDecimal day) throws IOException, CQLException {
		logger.info("Fetch indicator features as FeatureCollection for table with name {} and timestamp '{}-{}-{}'", indicatorViewTableName, year, month, day);
		/*
		 * here all indicators for the requested spatial unit shall be retrieved. However, the timeseries shall be reduced
		 * to only contain the requested timestamp
		 */

		SimpleFeatureSource featureSource = dataStore.getFeatureSource(indicatorViewTableName);

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
			String indicatorViewTableName) throws SQLException, IOException {
		logger.info("Fetch indicator feature properties without geometry for table with name {}", indicatorViewTableName);

		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<IndicatorPropertiesWithoutGeomType>();
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();

			
			String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorViewTableName + "\";";
			
			logger.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}

		return indicatorFeaturePropertiesWithoutGeom;
	}

	public static List<IndicatorPropertiesWithoutGeomType> getValidFeaturePropertiesWithoutGeometries(
			String indicatorViewTableName, BigDecimal year, BigDecimal month, BigDecimal day) throws IOException, SQLException {
		logger.info("Fetch indicator feature properties without geometry for table with name {}", indicatorViewTableName);

		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<IndicatorPropertiesWithoutGeomType>();
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();
			
			Calendar cal = Calendar.getInstance();
			// -1 in month, as month is 0-based
			cal.set(year.intValue(), month.intValue() - 1, day.intValue());
			Date date = cal.getTime();
			
			String dateString = createDateStringForDbProperty(date);
			dateString = dateString.split(DATE_PREFIX)[1];
			
			String whereClause = "WHERE \"" + KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\"::DATE <= '" + dateString + "'::DATE AND (\"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" is NULL OR \"" + KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\"::DATE >= '" + dateString + "'::DATE)";
			
			String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorViewTableName + "\" " + whereClause + ";";
			
			logger.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}

		return indicatorFeaturePropertiesWithoutGeom;
	}

	private static void addPropertiesWithoutGeometry(
			List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom, ResultSet result)
			throws SQLException {
		int columnCount = result.getMetaData().getColumnCount();
		
		while(result.next()){
			
			IndicatorPropertiesWithoutGeomType featureProps = new IndicatorPropertiesWithoutGeomType();
			
//			if(result.)
//			featureProps.setId(result.getString(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME));
//			featureProps.setName(result.getString(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME));
//			featureProps.setValidStartDate(result.getString(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME));
//			featureProps.setValidEndDate(result.getString(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME));
			
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

	public static List<IndicatorPropertiesWithoutGeomType> getSingleIndicatorFeatureRecords(
			String indicatorViewTableName, String featureId) throws Exception {
		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<IndicatorPropertiesWithoutGeomType>();
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();

			
			String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorValueTableName + "\" WHERE \"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = '" + featureId + "';";
			
			logger.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}

		return indicatorFeaturePropertiesWithoutGeom;
	}

	public static List<IndicatorPropertiesWithoutGeomType> getSingleIndicatorFeatureRecord(
			String indicatorViewTableName, String featureId, String featureRecordId) throws Exception {
		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<IndicatorPropertiesWithoutGeomType>();
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();

			
			String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorValueTableName + "\" WHERE \"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = '" + featureId + " AND \"" + KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME + "\" = '" + featureRecordId + "';";
			
			logger.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}

		return indicatorFeaturePropertiesWithoutGeom;
	}

	public static void deleteSingleFeatureRecordsForFeatureId(String indicatorViewTableName, String featureId) throws Exception {
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("DELETE FROM \"" + indicatorValueTableName + "\" WHERE \"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = '" + featureId + "';");

			String deleteFromTableCommand = builder.toString();
			
			logger.info("Send following DELETE command to database: " + deleteFromTableCommand);
			
			// TODO check if works
			statement.executeUpdate(deleteFromTableCommand);
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}
		
	}

	public static void deleteSingleFeatureRecordForFeatureId(String indicatorViewTableName, String featureId,
			String featureRecordId) throws Exception {
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("DELETE FROM \"" + indicatorValueTableName + "\" WHERE \"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = '" + featureId + "' AND \"" + KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME + "\" = '" + featureRecordId + "';");

			String deleteFromTableCommand = builder.toString();
			
			logger.info("Send following DELETE command to database: " + deleteFromTableCommand);
			
			// TODO check if works
			statement.executeUpdate(deleteFromTableCommand);
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}
		
	}

	public static void updateSpatialResourceFeatureRecordByRecordId(
			IndicatorPropertiesWithoutGeomType indicatorFeatureRecordData, String indicatorViewTableName,
			String featureId, String featureRecordId) throws Exception {
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("UPDATE \"" + indicatorValueTableName + "\"  SET ");
			
			Set<Entry<String, String>> indicatorProperties = indicatorFeatureRecordData.entrySet();
			ArrayList<Entry<String, String>> indicatorPropertiesList = Lists.newArrayList(indicatorProperties);		
			for (int i = 0; i < indicatorPropertiesList.size(); i++) {
				Entry<String, String> entry = indicatorPropertiesList.get(i);
				builder.append(" \"" + entry.getKey() + "\" = ");
				
				if(entry.getValue() != null) {
					builder.append(" '" + entry.getValue() + "'");
				}
				else {
					builder.append(" null ");
				}
				
				
				if(i < indicatorPropertiesList.size() -1) {
					builder.append(", ");
				}			
			}
			
			builder.append(" WHERE \"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = '" + featureId + "' AND \"" + KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME + "\" = '" + featureRecordId + "';");

			String updateTableCommand = builder.toString();
			
			logger.info("Send following UPDATE TABLE command to database: " + updateTableCommand);
			
			// TODO check if works
			statement.executeUpdate(updateTableCommand);
		} catch (Exception e) {
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
			
			throw e;
		} finally{
			try {
				statement.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				
			}
		}
		
	}

}
