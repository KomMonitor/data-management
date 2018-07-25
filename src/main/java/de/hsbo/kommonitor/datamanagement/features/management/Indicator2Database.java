package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeIndicatorValues;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeValueMapping;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPUTInputType;

public class Indicator2Database {

	private static Logger logger = LoggerFactory.getLogger(Indicator2Database.class);

	public static String writeIndicatorsToDatabase(List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues,
			String correspondingMetadataDatasetId) throws IOException, CQLException, SQLException {

		/*
		 * TODO implement
		 */

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

		/*
		 * TODO FIXME create view containing the geometry and indicatorValues
		 * for each indicator feature also set ViewName in Metadata --> create
		 * new field for that
		 */
		String indicatorTableName = featureType.getTypeName().toString();
		String viewTableName = createOrOverwriteView(ResourceTypeEnum.INDICATOR, indicatorTableName,
				correspondingMetadataDatasetId);

		logger.info(
				"Modifying the indicator metadata entry with id {} to set the name of the formerly created feature database table named {} and also the created featureViewTable with name {}.",
				correspondingMetadataDatasetId, indicatorTableName, viewTableName);
		DatabaseHelperUtil.updateIndicatorMetadataEntry(ResourceTypeEnum.INDICATOR, correspondingMetadataDatasetId,
				indicatorTableName, viewTableName);

		postGisStore.dispose();

		return featureType.getTypeName();
	}

	private static String createOrOverwriteView(ResourceTypeEnum indicator, String indicatorTableName,
			String correspondingMetadataDatasetId) throws IOException, SQLException {
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();

		String viewTableName = "VIEW_" + indicatorTableName;

		/*
		 * CREATE VIEW vw_combined AS SELECT * FROM TABLE1 t1 JOIN TABLE2 t2 ON
		 * t2.col = t1.col
		 */

		MetadataIndicatorsEntity indicatorEntity = DatabaseHelperUtil
				.getIndicatorMetadataEntity(correspondingMetadataDatasetId);
		String associatedSpatialUnitMetadataId = indicatorEntity.getAssociatedSpatialUnitMetadataId();
		MetadataSpatialUnitsEntity spatialUnitEntity = DatabaseHelperUtil
				.getSpatialUnitMetadataEntity(associatedSpatialUnitMetadataId);
		String spatialUnitsTable = spatialUnitEntity.getDbTableName();

		String indicatorColumnName = "spatialUnitId";
		/*
		 * TODOD FIXME bith tables should hav the same identical name. How to
		 * ensure this in the best way?
		 */
		String spatialUnitColumnName = "OBJECTID";

		String createViewCommand = "create or replace view " + viewTableName + " as select * from " + indicatorTableName
				+ " join " + spatialUnitsTable + " on " + indicatorTableName + "." + indicatorColumnName + " = "
				+ spatialUnitsTable + "." + spatialUnitColumnName;
		
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
		List<Date> availableDates = new ArrayList<>(indicatorValues.size());

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
		tb.add(KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_ID_NAME, String.class);

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

	public static void updateIndicatorFeatures(IndicatorPUTInputType indicatorData, String dbTableName) {
		// TODO Auto-generated method stub
		
	}

	public static String getValidFeatures(Date date, String dbTableName, String spatialUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getValidFeatures(String dbTableName, String spatialUnit) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void deleteFeatureTable(ResourceTypeEnum indicator, String dbTableName) {
		// TODO Auto-generated method stub
		
	}


}
