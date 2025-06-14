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

@Deprecated
public class Initial_GT_METADATA_PK_TABLE_Setup implements ApplicationListener<ContextRefreshedEvent>, Ordered {

	Logger logger = LoggerFactory.getLogger(Initial_GT_METADATA_PK_TABLE_Setup.class);

	boolean alreadySetup = false;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		logger.info("Begin initial setup of special gt_metadata_pk database table for KomMonitor.");

		if (alreadySetup)
			return;

		try {
			
		
		Connection jdbcConnection = DatabaseHelperUtil.getJdbcConnection();

		Statement statement = jdbcConnection.createStatement();
		
		String command = "CREATE TABLE IF NOT EXISTS public.gt_pk_metadata (" + 
		  "table_schema VARCHAR(32) NOT NULL," + 
		  "table_name VARCHAR(32) NOT NULL," + 
		  "pk_column VARCHAR(32) NOT NULL," +
		  "pk_column_idx INTEGER," + 
		  "pk_policy VARCHAR(32)," +
		  "pk_sequence VARCHAR(64)," +
		  "unique (table_schema, table_name, pk_column)," +
		  "check (pk_policy in ('sequence', 'assigned', 'autogenerated')))";
		
		statement.executeUpdate(command);

		statement.close();
		jdbcConnection.close();

		alreadySetup = true;

		logger.info("Initial setup of gt_metadata_pk table was succesful.");
		} catch (Exception e) {
			logger.info("Initial setup of gt_metadata_pk table caused error.");
			logger.error(e.getMessage());
			e.printStackTrace();
		}	
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
