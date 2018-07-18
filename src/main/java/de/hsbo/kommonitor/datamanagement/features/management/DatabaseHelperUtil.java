package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;

public class DatabaseHelperUtil {

	public static void updateResourceMetadataEntry(String tableName, String correspondingMetadataDatasetId) {
		// TODO FIXME update metadata entry: set name of associated dbTable

	}
	
	public static String createUniqueTableNameForResourceType(ResourceTypeEnum resourceType, DataStore dataStore)
			throws IOException {
		int numberSuffix = 0;
		String resourceTypeName = resourceType.name();

		String potentialDBTableName = createPotentialDBTableName(resourceTypeName, numberSuffix);

		SimpleFeatureSource featureSource = null;
		boolean uniqueTableNameFound = false;
		do {
			try {
				featureSource = dataStore.getFeatureSource(potentialDBTableName);
			} catch (Exception e) {
				// we assume that should the retrieval of featureSource have
				// failed, then
				// there is no database table for the potentialName
				uniqueTableNameFound = true;
				break;
			}

			if (featureSource == null) {
				uniqueTableNameFound = true;
				break;
			} else {
				// increment suffix and try again
				potentialDBTableName = createPotentialDBTableName(resourceTypeName, numberSuffix++);
			}
		} while (!uniqueTableNameFound);

		return potentialDBTableName;
	}

	private static String createPotentialDBTableName(String resourceTypeName, int numberSuffix) {
		// TODO Auto-generated method stub
		return resourceTypeName + "_" + numberSuffix;
	}

	public static DataStore getPostGisDataStore() throws IOException {

		Properties properties = new Properties();

		/*
		 * TODO If environment variables are used for DB connection then change
		 * this FIXME If environment variables are used for DB connection then
		 * change this
		 */
		properties.load(GeoJSON2DatabaseTool.class.getResourceAsStream("/application.properties"));

		Map<String, Object> params = new HashMap<>();
		params.put("dbtype", "postgis");
		params.put("host", properties.getProperty("database.host"));
		params.put("port", 5432);
		params.put("schema", "public");
		params.put("database", properties.getProperty("database.name"));
		params.put("user", properties.getProperty("spring.datasource.username"));
		params.put("passwd", properties.getProperty("spring.datasource.password"));

		DataStore dataStore = DataStoreFinder.getDataStore(params);

		return dataStore;
	}

	
}
