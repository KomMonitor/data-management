package de.hsbo.kommonitor.datamanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.GeoserverManager;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.management.OGCWebServiceManager;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.features.management.IndicatorDatabaseHandler;

@Configuration
public class HttpConfig {
	
	@Autowired
	private SpatialUnitsMetadataRepository spatialUnitsRepo;
	
	@Autowired
	private GeoresourcesMetadataRepository georesourceRepo;
	
	@Autowired
	private IndicatorsMetadataRepository indicatorsRepo;

    @Bean
    public ObjectMapper provideObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public OGCWebServiceManager ogcServiceManager() {
        return new GeoserverManager();
    }
    
    @Bean
    public DatabaseHelperUtil databaseHelper(){
    	return new DatabaseHelperUtil(spatialUnitsRepo, georesourceRepo, indicatorsRepo);
    }
    
    @Bean
    public IndicatorDatabaseHandler indicatorDatabaseHandler(){
    	return new IndicatorDatabaseHandler();
    }
    
    
}
