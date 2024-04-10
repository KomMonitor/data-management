package de.hsbo.kommonitor.datamanagement.config;

import java.sql.Connection;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;

@Component
public class DatamodelUpdatesSetup implements ApplicationListener<ContextRefreshedEvent>, Ordered {

	Logger logger = LoggerFactory.getLogger(DatamodelUpdatesSetup.class);

	boolean alreadySetup = false;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		logger.info("Begin initial setup of potentially missing database table properties due to data model updates within KomMonitor.");

		Connection jdbcConnection = null;
		Statement alterTableStmt = null;
		
		try {
			// establish JDBC connection
			jdbcConnection = DatabaseHelperUtil.getJdbcConnection();
			alterTableStmt = jdbcConnection.createStatement();
			
			alterTableStmt.addBatch("ALTER TABLE \"scriptmetadata\" ADD COLUMN IF NOT EXISTS \"scripttype\" text");
			
			alterTableStmt.addBatch("ALTER TABLE \"metadataindicators\" ADD COLUMN IF NOT EXISTS \"referencedatenote\" text");
			
			alterTableStmt.addBatch("ALTER TABLE \"metadataindicators\" ADD COLUMN IF NOT EXISTS \"displayorder\" integer DEFAULT 0");
			
			alterTableStmt.addBatch("ALTER TABLE \"roles\" ADD COLUMN IF NOT EXISTS \"permissionlevel\" integer DEFAULT 3");
			
			alterTableStmt.addBatch("ALTER TABLE \"roles\" ADD COLUMN IF NOT EXISTS \"organizationalunit\" varchar(255)");
			
//			in order to automatically upgrade old datamodel roles to new accesscontrol model we let rolename survive for now
			// can be deleted when all old KomMonitor instances are upgraded
//			alterTableStmt.addBatch("ALTER TABLE \"roles\" DROP COLUMN IF EXISTS \"rolename\"");
			
			alterTableStmt.addBatch("ALTER TABLE \"lastmodification\" ADD COLUMN IF NOT EXISTS \"accesscontrol\" timestamp with time zone");
			
			// UPDATE SPATIAL UNIT isOutlineLayer property
			alterTableStmt.addBatch("ALTER TABLE \"metadataspatialunits\" ADD COLUMN IF NOT EXISTS \"isoutlinelayer\" boolean DEFAULT false");
			alterTableStmt.addBatch("ALTER TABLE \"metadataspatialunits\" ADD COLUMN IF NOT EXISTS \"outlinecolor\" varchar(255)");
			alterTableStmt.addBatch("ALTER TABLE \"metadataspatialunits\" ADD COLUMN IF NOT EXISTS \"outlinewidth\" integer DEFAULT 3");
			alterTableStmt.addBatch("ALTER TABLE \"metadataspatialunits\" ADD COLUMN IF NOT EXISTS \"outlinedasharraystring\" varchar(255) DEFAULT '20'");
			
			// default classification for indicators
			alterTableStmt.addBatch("ALTER TABLE \"metadataindicators\" ADD COLUMN IF NOT EXISTS \"numclasses\" integer default 5");
			alterTableStmt.addBatch("ALTER TABLE \"metadataindicators\" ADD COLUMN IF NOT EXISTS \"classificationmethod\" integer default 2");
			
			// remove relic tables as they were never used
			alterTableStmt.addBatch("DROP TABLE IF EXISTS \"users_roles\" CASCADE");
			alterTableStmt.addBatch("DROP TABLE IF EXISTS \"roles_privileges\" CASCADE");
			alterTableStmt.addBatch("DROP TABLE IF EXISTS \"users\" CASCADE");			
			alterTableStmt.addBatch("DROP TABLE IF EXISTS \"privileges\" CASCADE");
			alterTableStmt.addBatch("DROP TABLE IF EXISTS \"metadataindicators_defaultclassificationmapping\" CASCADE");
			
			
			logger.info("Adding new DATABASE COLUMNS if they do not exist...");
			alterTableStmt.executeBatch();

		} catch (Exception e) {
			try {
				alterTableStmt.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		} finally{
			try {
				alterTableStmt.close();
				jdbcConnection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		logger.info("Initial setup of potentially missing database table properties due to data model updates within KomMonitor finished.");

	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}
}
