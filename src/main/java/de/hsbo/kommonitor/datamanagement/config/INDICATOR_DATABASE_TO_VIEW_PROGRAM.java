package de.hsbo.kommonitor.datamanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.kommonitor.datamanagement.features.management.KomMonitorFeaturePropertyConstants;

public class INDICATOR_DATABASE_TO_VIEW_PROGRAM {

	static Logger logger = LoggerFactory.getLogger(INDICATOR_DATABASE_TO_VIEW_PROGRAM.class);

	public static void main(String[] args) {

		logger.info("Begin initial INDICATOR table to view conversion for KomMonitor.");

		try {

			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kommonitor_midterm", "postgres", "postgres");
//			Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

			Statement statement = connection.createStatement();

			// method:
			/*
			 * in table "indicatorspatialunits" the mapping for INDICATOR table
			 * ("indicatorvaluetablename") to spatialUnit ID ("spatialunitid")
			 * is embedded in table "metadataspatialunits" the "datasetid" of
			 * spatial unit is mappedn to "dbtablename" --> make a map from
			 * spatialunitID to its dbtablename
			 * 
			 * each table for pattern "INDICATOR_*" must be renamed to
			 * "INDICATOR_*_VALUES" and must drop all columns except DATE_* and
			 * fid 
			 * 
			 * also a VIEW for the new value table and associated spatial
			 * unit must be created "INDICTOR_*_VIEW" 
			 * 
			 * this view name must
			 * replace the entry in "indicatorspatialunits" 
			 * 
			 * also add view name
			 * entry in gt_pk_metadata table
			 */

			String command = "Select datasetid,dbtablename from metadataspatialunits";

			ResultSet rs = statement.executeQuery(command);
			
			Map<String, String> spatialUnitsMap = new HashMap<String, String>();
			
			while(rs.next())
			{
			    String spatialUnitId = rs.getString("datasetid");
			    String spatialUnitTableName = rs.getString("dbtablename");				
			    
			    spatialUnitsMap.put(spatialUnitId, spatialUnitTableName);
			}

			rs.close();
			statement.close();
			
			/*
			 * 
			 */
			
			statement = connection.createStatement();
			
			command = "Select indicatorvaluetablename,spatialunitid from indicatorspatialunits";
			rs = statement.executeQuery(command);
			
			while(rs.next())
			{
			    String spatialUnitId = rs.getString("spatialunitid");
			    String spatialUnitTableName = spatialUnitsMap.get(spatialUnitId);
			    
			    String indicatorTableName = rs.getString("indicatorvaluetablename");			    
			    String indicatorTableName_VALUES = indicatorTableName + "_VALUES";
			    String indicatorTableName_VIEW = indicatorTableName + "_VIEW";
			    
			    String gt_pk_metadata_tableName = "gt_pk_metadata";
			    String indicatorspatialunits_tableName = "indicatorspatialunits";
			   
			    if (! indicatorTableName.contains("_VIEW")){
			    	// rename table, drop columns, make view, register view in metadata table, register view in indicatorspatialunits table
			    	Statement stmt = connection.createStatement();
			    	
			    	String cmd = "";
			    	// drop columns		
			    	cmd += "alter table \"" + indicatorTableName + "\" drop column if exists geometry;";
			    	cmd += "alter table \"" + indicatorTableName + "\" drop column if exists unique_id;";
			    	cmd += "alter table \"" + indicatorTableName + "\" drop column if exists \"NAME\";";
			    	cmd += "alter table \"" + indicatorTableName + "\" drop column if exists \"validStartDate\";";
			    	cmd += "alter table \"" + indicatorTableName + "\" drop column if exists \"validEndDate\";";
			    	cmd += "alter table \"" + indicatorTableName + "\" drop column if exists \"spatialUnitFeatureId\";";
			    	cmd += "alter table \"" + indicatorTableName + "\" drop column if exists \"spatialUnitFeatureName\";";
			    
			    	// rename table
			    	cmd += "alter table \"" + indicatorTableName + "\" rename to \"" + indicatorTableName_VALUES + "\";";			    	
			    	
			    	// make view
			    	cmd += "create or replace view \"" + indicatorTableName_VIEW + "\" as select indicator.*, spatialunit." + 
				KomMonitorFeaturePropertyConstants.GEOMETRY_COLUMN_NAME + ", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_NAME_NAME + "\", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME + "\", spatialunit.\"" + 
				KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME + "\" from \"" + indicatorTableName_VALUES
				+ "\" indicator join \"" + spatialUnitTableName + "\" spatialunit on indicator.\"" 
				+ KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" = CAST(spatialunit.\"" + KomMonitorFeaturePropertyConstants.SPATIAL_UNIT_FEATURE_ID_NAME + "\" AS varchar);";
			    	
			    	// register view in gt_pk_metadata
			    	cmd += "CREATE TABLE IF NOT EXISTS public.gt_pk_metadata (" + 
			    			  "table_schema VARCHAR(32) NOT NULL," + 
			    			  "table_name VARCHAR(32) NOT NULL," + 
			    			  "pk_column VARCHAR(32) NOT NULL," +
			    			  "pk_column_idx INTEGER," + 
			    			  "pk_policy VARCHAR(32)," +
			    			  "pk_sequence VARCHAR(64)," +
			    			  "unique (table_schema, table_name, pk_column)," +
			    			  "check (pk_policy in ('sequence', 'assigned', 'autogenerated')))";
			    	
			    	cmd += "INSERT INTO gt_pk_metadata(table_schema, table_name, pk_column, pk_column_idx, pk_policy, pk_sequence)" +
				"VALUES ('public','" + indicatorTableName_VIEW + "','" +KomMonitorFeaturePropertyConstants.UNIQUE_FEATURE_ID_PRIMARYKEY_NAME + "',1,'autogenerated',null) " + 
				"ON CONFLICT (table_schema, table_name, pk_column) DO NOTHING;";
			    	
			    	// register view in indicatorspatialunits
			    	cmd += "update \"" + indicatorspatialunits_tableName + "\" set indicatorvaluetablename = '" + indicatorTableName_VIEW + "' where indicatorvaluetablename = '" + indicatorTableName + "';";
			    	
			    	stmt.executeUpdate(cmd);			    	
			    	
			    	stmt.close();
			    }
			}

			rs.close();
			statement.close();
			/*
			 * 
			 */
			
			connection.close();
			
			

			logger.info("Initial INDICATOR table to view conversion was succesful.");
		} catch (Exception e) {
			logger.info("Initial INDICATOR table to view conversion caused error.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

}
