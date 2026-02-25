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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
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
import jakarta.validation.Valid;

public class IndicatorDatabaseHandler {

	private static final Logger LOG = LoggerFactory.getLogger(IndicatorDatabaseHandler.class);

	private static final String VALUE_SUFFIX = "_VALUES";
	private static final String VIEW_SUFFIX = "_VIEW";
	public static final String DATE_PREFIX = "DATE_";

	private static boolean ADDITIONAL_PROPERTIES_WERE_SET = false;
	
	private static FeatureJSON instantiateFeatureJSON() {
		GeometryJSON geometryJSON = new GeometryJSON(KomMonitorFeaturePropertyConstants.NUMBER_OF_DECIMALS_FOR_GEOJSON_OUTPUT);
		
		return new FeatureJSON(geometryJSON);
	}
	
	public static String createIndicatorValueTable(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) throws IOException {

		DataStore postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		
		List<Date> availableDatesForIndicator = collectIndicatorDates(indicatorValues);
		
		// sort availableDates
		availableDatesForIndicator.sort(Comparator.comparing(date -> date));
		
		/*
		 * 
		 * when a new simple feature type is created, thenth order of feature attributes is used in the exact same order
		 * when creating the simple features themselves.
		 * 
		 * hence we must make sure that for indicator dates we must inspect which feature has which date and set NULL values if any date is missing
		 * also we should sort incoming features indicatorValues array by timestamp ascending to make sure table columns are built in ascending order.
		 * 
		 */
		
		// Create a new list using the Stream API		
		
		indicatorValues = sortIndicatorValuesByAvailableDates_ascending(indicatorValues, availableDatesForIndicator);

		LOG.info("Create SimpleFeatureType for indicator");

		SimpleFeatureType featureType = createSimpleFeatureTypeForIndicators(postGisStore,
				availableDatesForIndicator);

		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);

		LOG.info("build features according to IndicatorValueMapping");

		/*
		 * A list to collect features as we create them.
		 */
		List<SimpleFeature> features = constructSimpleFeatures(indicatorValues, builder);

		LOG.info("create new Table from featureSchema using table name {}", featureType.getTypeName());
		postGisStore.createSchema(featureType);

		LOG.info("Start to add the actual features to table with name {}", featureType.getTypeName());

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

	private static List<IndicatorPOSTInputTypeIndicatorValues> sortIndicatorValuesByAvailableDates_ascending(
			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues, List<Date> availableDatesForIndicator) {
		for (IndicatorPOSTInputTypeIndicatorValues indicatorPOSTInputTypeIndicatorValueEntry : indicatorValues) {
			IndicatorPOSTInputTypeIndicatorValues sortedElement = new IndicatorPOSTInputTypeIndicatorValues();
			sortedElement.setSpatialReferenceKey(indicatorPOSTInputTypeIndicatorValueEntry.getSpatialReferenceKey());
			
			// now make sure that for each availableDateForIndicator (in that order!) an indicatorValue is present (maybe NULL)
			Map<Date, Float> availableDatesForIndicatorMap = new HashMap<Date, Float>();
			// init map with NULL values
			for (Date availableDate : availableDatesForIndicator) {
				availableDatesForIndicatorMap.put(availableDate, null);
			}
			
			// now fill actual indicator values for respective indicator dates (for each feature certain dates might be missing)
			List<@Valid IndicatorPOSTInputTypeValueMapping> valueMapping_original = indicatorPOSTInputTypeIndicatorValueEntry.getValueMapping();
			for (IndicatorPOSTInputTypeValueMapping valueMappingEntry_original : valueMapping_original) {
				availableDatesForIndicatorMap.put(DateTimeUtil.fromLocalDate(valueMappingEntry_original.getTimestamp()), valueMappingEntry_original.getIndicatorValue());
			}
			
			
			// create sorted valueMapping array
			List<IndicatorPOSTInputTypeValueMapping> valueMapping_sorted = new ArrayList<IndicatorPOSTInputTypeValueMapping>();
			// preserve correct date order
			for (Date availableDate : availableDatesForIndicator) {
				IndicatorPOSTInputTypeValueMapping entry = new IndicatorPOSTInputTypeValueMapping();
				entry.setTimestamp(DateTimeUtil.toLocalDate(availableDate));
				entry.setIndicatorValue(availableDatesForIndicatorMap.get(availableDate));	
				
				valueMapping_sorted.add(entry);
			}
			
			// set sorted valueMapping array for current IndicatorPOSTInputTypeIndicatorValues object, thus modifying it for further actions
			indicatorPOSTInputTypeIndicatorValueEntry.setValueMapping(valueMapping_sorted);
		}
		
		return indicatorValues;
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

			
			LOG.info("Created the following SQL command to create or update indicator table: '{}'", createViewCommand);
			
			// TODO check if works
			statement.execute(createViewCommand);
		} catch (Exception e) {
			try {
				if (statement != null) {
					statement.close();
				}
				if (jdbcConnection != null) {
					jdbcConnection.close();
				}
			} catch (Exception e2) {
				LOG.error("Closing DB connection failed.", e2);
			}
			
			throw e;
		} finally{
			try {
				if (statement != null) {
					statement.close();
				}
				if (jdbcConnection != null) {
					jdbcConnection.close();
				}
			} catch (Exception e2) {
				LOG.error("Closing DB connection failed.", e2);
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
				LOG.error("Error while adding features to database.", eek);
				throw eek;
			}

			transaction.close();

			LOG.info("Features should have been added to table with name {}", featureType.getTypeName());
	}

	private static List<SimpleFeature> constructSimpleFeatures(
			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues, SimpleFeatureBuilder builder) {
		List<SimpleFeature> features = new ArrayList<>();

		if(indicatorValues != null && !indicatorValues.isEmpty()){
			for (IndicatorPOSTInputTypeIndicatorValues indicatorEntry : indicatorValues) {
				/*
				 * type has attributes 1. spatialUnitId 2. one attribute for each
				 * timeStamp
				 */
				String spatialReferenceKey = indicatorEntry.getSpatialReferenceKey();

				builder.add(spatialReferenceKey);

				List<IndicatorPOSTInputTypeValueMapping> valueMapping = indicatorEntry.getValueMapping();
				for (IndicatorPOSTInputTypeValueMapping mappingEntry : valueMapping) {
					try {
						builder.add(mappingEntry.getIndicatorValue());
					} catch (Exception ex) {
						LOG.error("Error while building feature.", ex);
					}

				}

				features.add(builder.buildFeature(spatialReferenceKey));
			}
		}
		
		return features;
	}

	private static List<Date> collectIndicatorDates(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
		List<Date> availableDates = new ArrayList<>();

		if(indicatorValues == null || indicatorValues.isEmpty()){
			LOG.info("submitted post body included null or empty list of indicatorValues. Hence no timestamp values can be created.");
		}
		else{
			
			for (IndicatorPOSTInputTypeIndicatorValues indicatorValuesEntry : indicatorValues) {
				for (IndicatorPOSTInputTypeValueMapping indicatorValueMappingEntry : indicatorValuesEntry.getValueMapping()) {
					if (indicatorValueMappingEntry.getTimestamp() != null) {
						Date date = DateTimeUtil.fromLocalDate(indicatorValueMappingEntry.getTimestamp());

						if(!availableDates.contains(date)){
							// add new Candidate
							availableDates.add(date);
						}
					}
				}
			}
		}
		

		return availableDates;
	}

	private static SimpleFeatureType createSimpleFeatureTypeForIndicators(DataStore dataStore,
																		  List<Date> availableDatesForIndicator) throws IOException {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(DatabaseHelperUtil.createUniqueTableNameForResourceType(ResourceTypeEnum.INDICATOR, dataStore, VALUE_SUFFIX));
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

        return DATE_PREFIX + year + "-" + monthString + "-" + dayString;
	}
	
	public static List<String> getAvailableDates(String viewTableName) throws IOException {
		List<String> availableDates = new ArrayList<>();
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

		return sortDatesAscending(availableDates);
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
		
		List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = indicatorData.getIndicatorValues();
		if(indicatorValues == null || indicatorValues.isEmpty()){
			LOG.info("submitted put body included null or empty list of indicatorValues. Hence no changes can be applied.");
			throw new Exception("submitted put body included null or empty list of indicatorValues");
		}

		/*
		 * identify all available timestamps within delivered indicatorValues list
		 * by iterating over all elements 
		 */
		List<String> additionalPropertyNamesToAddAsFloatColumns = identifyNewProperties(schema, indicatorValues);

		postGisStore.dispose();

		// update schema in db to ensure all new columns are created
		if(ADDITIONAL_PROPERTIES_WERE_SET){
			addNewColumnsToTable(indicatorValueTableName, additionalPropertyNamesToAddAsFloatColumns);
		}

		addDataToTable(indicatorValueTableName, indicatorValues);

		// RUN VACUUM ANALYSE
		DatabaseHelperUtil.runVacuumAnalyse(indicatorValueTableName);
	}

	private static void addDataToTable(String indicatorValueTableName,
			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) throws Exception {
		DataStore postGisStore;
		SimpleFeatureSource featureSource;
		/*
		 * refetch schema of database table due to updated columns!
		 */
		postGisStore = DatabaseHelperUtil.getPostGisDataStore();
		featureSource = postGisStore.getFeatureSource(indicatorValueTableName);
		
		SimpleFeatureStore store = (SimpleFeatureStore) featureSource; // write
																		// access!
		Transaction transaction = new DefaultTransaction("Update features in Table " + indicatorValueTableName);

		try {
			applyModificationStatements(indicatorValues, store);
			transaction.commit(); // actually writes out the features in one
			// go
		} catch (Exception eek) {
			transaction.rollback();
			LOG.error("Error while writing modification timestamp to database.", eek);
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
			
			LOG.info("Adding new DATABASE COLUMNS...");
			alterTableStmt.executeBatch();

		} catch (Exception e) {
			handleClose(alterTableStmt, jdbcConnection);
			throw e;
		} finally{
			handleClose(alterTableStmt, jdbcConnection);
		}
	}

	private static void handleClose(Statement stmt, Connection conn) {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			LOG.error("Error while closing database resource.", e);
		}
	}

	private static String getValueTableNameFromViewTableName(String indicatorDbViewName) {
		return indicatorDbViewName.split(VIEW_SUFFIX)[0] + VALUE_SUFFIX;
	}

	private static List<String> identifyNewProperties(SimpleFeatureType schema,
			List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
		List<String> newPropertyNames = new ArrayList<>();
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
		
		List<Date> indicatorDateCandidates = collectIndicatorDates(indicatorValues);
		
		// now compare candidates to existing database schema to identify new date properties/columns
		for (Date candidateDate : indicatorDateCandidates) {
			String datePropertyName = createDateStringForDbProperty(candidateDate);
			if(!schemaContainsDateProperty(schema, datePropertyName)){
				// add new Property
				LOG.debug("Add new property/column '{}' to table '{}'", datePropertyName, schema.getTypeName());
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
		
		if(indicatorValues != null && !indicatorValues.isEmpty()){
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
					List<String> columnNames = new ArrayList<>(valueMapping.size());
					List<Object> columnValues = new ArrayList<>(valueMapping.size());

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
		if (!newFeaturesToBeAdded.isEmpty()){
			store.addFeatures(newFeaturesToBeAdded);
		}
	}	
	
	private static boolean isNotInExistingFeatures(Filter filter, SimpleFeatureCollection existingFeatures) {
		// TODO Auto-generated method stub
		return existingFeatures.subCollection(filter).isEmpty();
	}

	private static Filter createFilterForSpatialUnitId(String spatialUnitId) throws CQLException {
		return CQL.toFilter(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + " = '" + spatialUnitId + "'");
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

	public static FeatureCollection getValidIndicatorFeatures(String featureViewName, DataStore dataStore, BigDecimal year, BigDecimal month, BigDecimal day, String simplifyGeometries) throws IOException, CQLException {
		SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureViewName);
		Calendar cal = Calendar.getInstance();
		// -1 in month, as month is 0-based
		cal.set(year.intValue(), month.intValue() - 1, day.intValue());
		Date date = cal.getTime();

		FeatureCollection features = fetchFeaturesForDate(featureSource, date);

		features = DateTimeUtil.fixDateResonseTypes(features);
		features = GeometrySimplifierUtil.simplifyGeometriesAccordingToParameter(features, simplifyGeometries);

		return features;
	}

	public static String getValidFeatures(String featureViewName, BigDecimal year, BigDecimal month, BigDecimal day, String simplifyGeometries) throws Exception {
		LOG.info("Fetch indicator features for table with name {} and timestamp '{}-{}-{}' and simplificationType '{}'", featureViewName, year, month, day, simplifyGeometries);
		/*
		 * here all indicators for the requested spatial unit shall be retrieved. However, the timeseries shall be reduced
		 * to only contain the requested timestamp
		 */
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();
		FeatureCollection features = getValidIndicatorFeatures(featureViewName, dataStore, year, month, day, simplifyGeometries);

		int indicatorFeaturesSize = features.size();
		LOG.info("Transform {} found indicator features to GeoJSON", indicatorFeaturesSize);

		String geoJson;

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

		return featureSource.getFeatures(andFilter);
	}

	public static void deleteIndicatorValueTable(String dbViewName) throws IOException, SQLException {
		LOG.info("Deleting indicator value table {}.", dbViewName);
		
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

			LOG.info("Deletion of table {} and view {} was successful", valueTableName, dbViewName);
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);

			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
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
		LOG.info("parsing date from submitted date components. Submitted components were 'year: {}, month: {}, day: {}'. As Java time treats month 0-based, the following date will be used: 'year-month(-1)-day {}-{}-{}'", year, month, day, year, month.intValue()-1, day);
		String datePropertyName = createDateStringForDbProperty(date);
		
		if(schemaContainsDateProperty(schema, datePropertyName)){
			// add new Property
			LOG.debug("Found matching date column for property/column '{}' of table '{}'", datePropertyName, schema.getTypeName());
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

				// use dataType real, as only new timeseries will be added for
				// indicators
				builder.append("DROP COLUMN \"" + datePropertyName + "\" CASCADE;");

				String alterTableCommand = builder.toString();
				
				LOG.info("Send following ALTER TABLE command to database: {}", alterTableCommand);
				
				// TODO check if works
				statement.executeUpdate(alterTableCommand);
			} catch (Exception e) {
				handleClose(statement, jdbcConnection);
				throw e;
			} finally{
				handleClose(statement, jdbcConnection);
			}

		}
	}

	public static String getIndicatorFeatures(String featureViewTableName, String simplifyGeometries) throws Exception {
		DataStore dataStore = DatabaseHelperUtil.getPostGisDataStore();

		SimpleFeatureCollection  features = (SimpleFeatureCollection) getIndicatorsFeatures(featureViewTableName, dataStore, simplifyGeometries);
		
		int indicatorFeaturesSize = features.size();
		LOG.info("Transform {} found indicator features to GeoJSON", indicatorFeaturesSize);

		String geoJson;

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

	public static FeatureCollection getIndicatorsFeatures(String featureViewTableName, DataStore dataStore, String simplifyGeometries) throws Exception {
		LOG.info("Fetch indicator features for table with name {}", featureViewTableName);
		SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureViewTableName);
		FeatureCollection features = featureSource.getFeatures();

		features = DateTimeUtil.fixDateResonseTypes(features);
		features = GeometrySimplifierUtil.simplifyGeometriesAccordingToParameter(features, simplifyGeometries);

		return features;
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
		return createOrOverwriteIndicatorView(indicatorValueTableName, spatialUnitName);
	}
	
	public static String createOrReplaceIndicatorView_fromViewTableName(String indicatorViewTableName,
			String spatialUnitName) throws IOException, SQLException {
		String valueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		return createOrOverwriteIndicatorView(valueTableName, spatialUnitName);
	}

	public static List<Float> getAllIndicatorValues(String indicatorValueTableName, String datePropertyName) throws SQLException, IOException {
		
		List<Float> indicatorValues = new ArrayList<>();
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();
			
			if(!datePropertyName.startsWith(DATE_PREFIX)){
				datePropertyName = DATE_PREFIX + datePropertyName;
			}
			
			String createTableCommand = "SELECT \"" + datePropertyName + "\" FROM \"" + indicatorValueTableName + "\";";
			
			LOG.info("Created the following SQL command to create or update indicator table: '{}'", createTableCommand);
			
			ResultSet result = statement.executeQuery(createTableCommand);
			
			while(result.next()){
				indicatorValues.add(result.getFloat(datePropertyName));
			}
			result.close();
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
		}

		return indicatorValues;
	}

	public static FeatureCollection getValidFeaturesAsFeatureCollection(DataStore dataStore, String indicatorViewTableName,
			BigDecimal year, BigDecimal month, BigDecimal day) throws IOException, CQLException {
		LOG.info("Fetch indicator features as FeatureCollection for table with name {} and timestamp '{}-{}-{}'", indicatorViewTableName, year, month, day);
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
		
		 return fetchFeaturesForDate(featureSource, date);
	}

	public static List<IndicatorPropertiesWithoutGeomType> getIndicatorFeaturePropertiesWithoutGeometries(
			String indicatorViewTableName) throws SQLException, IOException {
		LOG.info("Fetch indicator feature properties without geometry for table with name {}", indicatorViewTableName);

		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<>();
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();

			
			String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorViewTableName + "\";";
			
			LOG.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
		}

		return indicatorFeaturePropertiesWithoutGeom;
	}

	public static List<IndicatorPropertiesWithoutGeomType> getValidFeaturePropertiesWithoutGeometries(
			String indicatorViewTableName, BigDecimal year, BigDecimal month, BigDecimal day) throws IOException, SQLException {
		LOG.info("Fetch indicator feature properties without geometry for table with name {}", indicatorViewTableName);

		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<>();
		
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
			
			LOG.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
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
					// ignore and do nothing
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
		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<>();
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();

			
			String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorValueTableName + "\" WHERE \"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = '" + featureId + "';";
			
			LOG.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
		}
		return indicatorFeaturePropertiesWithoutGeom;
	}

	public static List<IndicatorPropertiesWithoutGeomType> getSingleIndicatorFeatureRecord(
			String indicatorViewTableName, String featureId, String featureRecordId) throws Exception {
		List<IndicatorPropertiesWithoutGeomType> indicatorFeaturePropertiesWithoutGeom = new ArrayList<>();
		String indicatorValueTableName = getValueTableNameFromViewTableName(indicatorViewTableName);
		
		Connection jdbcConnection = null;
		Statement statement = null;
		
		try {
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			statement = jdbcConnection.createStatement();

			
			String getFeaturePropertiesCommand = "SELECT * FROM \"" + indicatorValueTableName + "\" WHERE \"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = '" + featureId + " AND \"" + KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME + "\" = '" + featureRecordId + "';";
			
			LOG.info("Created the following SQL command to retrieve indicator properties from indicator table: '{}'", getFeaturePropertiesCommand);
			
			ResultSet result = statement.executeQuery(getFeaturePropertiesCommand);
			
			addPropertiesWithoutGeometry(indicatorFeaturePropertiesWithoutGeom, result);
			
			result.close();
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
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
			
			LOG.info("Send following DELETE command to database: {}", deleteFromTableCommand);
			
			// TODO check if works
			statement.executeUpdate(deleteFromTableCommand);
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
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
			
			LOG.info("Send following DELETE command to database: {}", deleteFromTableCommand);
			
			// TODO check if works
			statement.executeUpdate(deleteFromTableCommand);
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
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
			
			LOG.info("Send following UPDATE TABLE command to database: {}", updateTableCommand);
			
			// TODO check if works
			statement.executeUpdate(updateTableCommand);
		} catch (Exception e) {
			handleClose(statement, jdbcConnection);
			
			throw e;
		} finally{
			handleClose(statement, jdbcConnection);
		}
		
	}

}
