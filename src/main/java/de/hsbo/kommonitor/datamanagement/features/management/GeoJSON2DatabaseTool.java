package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.util.Date;

import org.geotools.data.DataStore;
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
		final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
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

	public static AvailablePeriodOfValidityType getAvailablePeriodOfValidity(String dbTableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getValidFeatures(Date date, String dbTableName) {
		// TODO Auto-generated method stub
		return null;
	}

}
