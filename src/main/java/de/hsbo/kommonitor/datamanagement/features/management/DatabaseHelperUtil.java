package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataSpatialUnitsEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;

public class DatabaseHelperUtil {
	
    private static Environment env;

//	private static Properties properties;
	
	private static SpatialUnitsMetadataRepository spatialUnitsRepo;
	
	private static GeoresourcesMetadataRepository georesourceRepo;
	
	private static IndicatorsMetadataRepository indicatorsRepo;
	
	public DatabaseHelperUtil(SpatialUnitsMetadataRepository spatialUnitsRepository, GeoresourcesMetadataRepository georesourcesRepository,
			IndicatorsMetadataRepository indicatorsRepository, Environment environment){
		spatialUnitsRepo = spatialUnitsRepository;
		georesourceRepo = georesourcesRepository;
		indicatorsRepo = indicatorsRepository;
		env = environment;
	}

	public static void updateResourceMetadataEntry(ResourceTypeEnum resource, String tableName,
			String correspondingMetadataDatasetId) {
		switch (resource) {
		case SPATIAL_UNIT:
			MetadataSpatialUnitsEntity spatialUnitsEntity = spatialUnitsRepo.findByDatasetId(correspondingMetadataDatasetId);
			spatialUnitsEntity.setDbTableName(tableName);
			spatialUnitsRepo.save(spatialUnitsEntity);
			break;
		case GEORESOURCE:
			MetadataGeoresourcesEntity georesourcesEntity = georesourceRepo.findByDatasetId(correspondingMetadataDatasetId);
			georesourcesEntity.setDbTableName(tableName);
			georesourceRepo.save(georesourcesEntity);
			break;
		case INDICATOR:
			MetadataIndicatorsEntity indicatorsEntity = indicatorsRepo.findByDatasetId(correspondingMetadataDatasetId);
			indicatorsEntity.setDbTableName(tableName);
			indicatorsRepo.save(indicatorsEntity);
			break;	
		}

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
		return resourceTypeName + "_" + numberSuffix;
	}

	public static DataStore getPostGisDataStore() throws IOException {

		/*
		 * TODO If environment variables are used for DB connection then change
		 * this FIXME If environment variables are used for DB connection then
		 * change this
		 */
//		if (properties == null){
//			properties = new Properties();
//			properties.load(SpatialFeatureDatabaseHandler.class.getResourceAsStream("/application-docker.properties"));
//		}
		
		System.out.println("DB HOST: " + env.getProperty("database.host"));
		System.out.println("DB URL COMPLETE: " + env.getProperty("spring.datasource.url"));
			

		Map<String, Object> params = new HashMap<>();
		params.put("dbtype", "postgis");
		params.put("host", env.getProperty("database.host"));
		params.put("port", env.getProperty("database.port"));
		params.put("schema", "public");
		params.put("database", env.getProperty("database.name"));
		params.put("user", env.getProperty("spring.datasource.username"));
		params.put("passwd", env.getProperty("spring.datasource.password"));

		DataStore dataStore = DataStoreFinder.getDataStore(params);

		return dataStore;
	}

	public static Connection getJdbcConnection() throws IOException, SQLException {

		/*
		 * TODO If environment variables are used for DB connection then change
		 * this FIXME If environment variables are used for DB connection then
		 * change this
		 */
//		if (properties == null){
//			properties = new Properties();
//			properties.load(SpatialFeatureDatabaseHandler.class.getResourceAsStream("/application-docker.properties"));
//		}

		String url = "jdbc:postgresql://" + env.getProperty("database.host") + "/"
				+ env.getProperty("database.name");
		Properties props = new Properties();
		props.setProperty("user", env.getProperty("spring.datasource.username"));
		props.setProperty("password", env.getProperty("spring.datasource.password"));
//		props.setProperty("ssl", "true");
		Connection conn = DriverManager.getConnection(url, props);

		return conn;
	}

//	public static void updateIndicatorMetadataEntry(ResourceTypeEnum indicator, String correspondingMetadataDatasetId,
//			String indicatorTableName, String viewTableName) {
//
//		MetadataIndicatorsEntity indicatorsEntity = indicatorsRepo.findByDatasetId(correspondingMetadataDatasetId);
//		indicatorsEntity.setDbTableName(indicatorTableName);
//		indicatorsEntity.setFeatureViewDbTableName(viewTableName);
//		indicatorsRepo.save(indicatorsEntity);
//	}

//	public static MetadataIndicatorsEntity getIndicatorMetadataEntity(String indicatorDatasetId) {
//		return indicatorsRepo.findByDatasetId(indicatorDatasetId);
//	}

	public static MetadataSpatialUnitsEntity getSpatialUnitMetadataEntityByName(String spatialUnitName) {
		return spatialUnitsRepo.findByDatasetName(spatialUnitName);
	}
	
	public static MetadataSpatialUnitsEntity getSpatialUnitMetadataEntityById(String spatialUnitId) {
		return spatialUnitsRepo.findByDatasetId(spatialUnitId);
	}

	public static MetadataIndicatorsEntity getIndicatorMetadataEntity(String requiredIndicatorId) {
		return indicatorsRepo.findByDatasetId(requiredIndicatorId);
	}

	public static MetadataGeoresourcesEntity getGeoresourceMetadataEntity(String requiredGeoresourceId) {
		return georesourceRepo.findByDatasetId(requiredGeoresourceId);
	}

}
