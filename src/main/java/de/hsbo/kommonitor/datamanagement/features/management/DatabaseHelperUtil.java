package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.springframework.core.env.Environment;

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

	public static String createUniqueTableNameForResourceType(ResourceTypeEnum resourceType, DataStore dataStore,  String indicator_value_suffix)
			throws IOException {
		int numberSuffix = 0;
		String resourceTypeName = resourceType.name();

		String potentialDBTableName = createPotentialDBTableName(resourceTypeName, numberSuffix, indicator_value_suffix);

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
				potentialDBTableName = createPotentialDBTableName(resourceTypeName, numberSuffix++,  indicator_value_suffix);
			}
		} while (!uniqueTableNameFound);

		return potentialDBTableName;
	}

	private static String createPotentialDBTableName(String resourceTypeName, int numberSuffix, String indicator_value_suffix) {
		return resourceTypeName + "_" + numberSuffix + indicator_value_suffix;
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
		
//		System.out.println("DB HOST: " + env.getProperty("database.host"));
//		System.out.println("DB URL COMPLETE: " + env.getProperty("spring.datasource.url"));
			

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

		String url = "jdbc:postgresql://" + env.getProperty("database.host") + ":"
                + env.getProperty("database.port") +"/"
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

	public static void disposePostGisDataStore(DataStore dataStore) {
		dataStore.dispose();
		
	}

	public static Map<String, String> getExistingIndexDefinitions(String indicatorValueTableName) throws Exception {
		Connection jdbcConnection = null;
		Statement statement = null;
		
		Map<String, String> indexMap = new HashMap<String, String>();
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("SELECT indexname, indexdef FROM  pg_indexes WHERE tablename = '" + indicatorValueTableName + "' ");
			
			String selectCommand = builder.toString();
			
			// TODO check if works
			ResultSet resultSet = statement.executeQuery(selectCommand);
			
			while(resultSet.next()) {
				
				indexMap.put(resultSet.getString("indexname"), resultSet.getString("indexdef"));

			}
			
			resultSet.close();			
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
		
		return indexMap;

	}

	public static Map<String, String> getExistingConstraintDefinitions(String indicatorValueTableName) throws Exception {
		Connection jdbcConnection = null;
		Statement statement = null;
		
		Map<String, String> constraintsMap = new HashMap<String, String>();
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("SELECT conrelid::regclass AS table_from ,conname,pg_get_constraintdef(c.oid) FROM   pg_constraint c JOIN   pg_namespace n ON n.oid = c.connamespace WHERE conname LIKE '%" + 
					indicatorValueTableName + "%' AND contype IN ('f', 'p ') AND  n.nspname = 'public';");
			
			String selectCommand = builder.toString();
			
			// TODO check if works
			ResultSet resultSet = statement.executeQuery(selectCommand);
			
			while(resultSet.next()) {
				
				constraintsMap.put(resultSet.getString("conname"), resultSet.getString("pg_get_constraintdef"));

			}
			
			resultSet.close();			
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
		
		return constraintsMap;
	}

	public static void dropIndices(Map<String, String> existingIndexDefinitionsForTable, String indicatorValueTableName) throws Exception {
		Connection jdbcConnection = null;
		Statement statement = null;		
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			Set<String> keySet = existingIndexDefinitionsForTable.keySet();
			
			for (String indexName : keySet) {
				builder.append("DROP INDEX IF EXISTS \"" + indexName +  "\";");
			}
			
			String dropIndexCommand = builder.toString();			
			statement.executeUpdate(dropIndexCommand);		
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

	public static void dropConstraints(Map<String, String> existingConstraintsDefinitionsForTable, String indicatorValueTableName) throws Exception {
		Connection jdbcConnection = null;
		Statement statement = null;		
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			Set<String> keySet = existingConstraintsDefinitionsForTable.keySet();	
			Iterator<String> iterator = keySet.iterator();
			
			builder.append("ALTER TABLE \"" + indicatorValueTableName + "\" ");
			
			
			while(iterator.hasNext()) {				
				String constraintName = iterator.next();
				builder.append("DROP CONSTRAINT IF EXISTS \"" + constraintName +  "\"");
				
				if(iterator.hasNext()){
					builder.append(", ");
				}
				else{
					builder.append(";");
				}
			}
			
			String dropConstraintCommand = builder.toString();			
			statement.executeUpdate(dropConstraintCommand);		
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

	public static void reinsertConstraints(String indicatorValueTableName,
			Map<String, String> constraintsDefinitionsForTable) throws Exception {
		
		Connection jdbcConnection = null;
		Statement statement = null;		
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			Set<String> keySet = constraintsDefinitionsForTable.keySet();	
			Iterator<String> iterator = keySet.iterator();
			
			builder.append("ALTER TABLE \"" + indicatorValueTableName + "\" ");
			
			
			while(iterator.hasNext()) {				
				String constraintName = iterator.next();
				String random = String.valueOf(Math.random());
				builder.append("ADD CONSTRAINT \"" + indicatorValueTableName + "_" + random + "\" " + constraintsDefinitionsForTable.get(constraintName) +  "");
				
				if(iterator.hasNext()){
					builder.append(", ");
				}
				else{
					builder.append(";");
				}
			}
			
			String addConstraintCommand = builder.toString();			
			statement.executeUpdate(addConstraintCommand);		
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

	public static void reinsertIndices(String indicatorValueTableName,
			Map<String, String> indexDefinitionsForTable) throws Exception {
		Connection jdbcConnection = null;
		Statement statement = null;		
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();
			
			Set<String> keySet = indexDefinitionsForTable.keySet();
			
			for (String indexName : keySet) {
				builder.append( indexDefinitionsForTable.get(indexName) + ";");
			}
			
			String insertIndexCommand = builder.toString();			
			statement.executeUpdate(insertIndexCommand);		
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

	public static void runVacuumAnalyse(String indicatorValueTableName) throws Exception {
		Connection jdbcConnection = null;
		Statement statement = null;		
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			
			statement = jdbcConnection.createStatement();
			
			StringBuilder builder = new StringBuilder();

			builder.append("VACUUM ANALYZE \"" + indicatorValueTableName + "\";");
			
			String insertIndexCommand = builder.toString();			
			statement.executeUpdate(insertIndexCommand);		
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
