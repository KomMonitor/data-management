package de.hsbo.kommonitor.datamanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits.IndicatorSpatialUnitsRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.GeoresourcesPeriodsOfValidityRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.SpatialUnitsPeriodsOfValidityRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.GeoresourceReferenceMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.GeoresourceReferenceRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.IndicatorReferenceMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.IndicatorReferenceRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.references.ReferenceManager;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMapper;
import de.hsbo.kommonitor.datamanagement.api.impl.spatialunits.SpatialUnitsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsHelper;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsRepository;
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
	private GeoresourcesPeriodsOfValidityRepository georesourcePeriodsOfValidityRepo;
	
	@Autowired
	private SpatialUnitsPeriodsOfValidityRepository spatialUnitsPeriodsOfValidityRepo;
	
	@Autowired
	private IndicatorsMetadataRepository indicatorsRepo;
	
	@Autowired
	private IndicatorSpatialUnitsRepository indicatorSpatialUnitsRepo;
	
	@Autowired
	private IndicatorReferenceRepository indicatorRefRepo;
	
	@Autowired
	private GeoresourceReferenceRepository georesourceRefRepo;
	
	@Autowired
	private TopicsRepository topicsRepo;
	
	@Autowired
	private Environment environment;

    @Bean
    public ObjectMapper provideObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public OGCWebServiceManager ogcServiceManager() {
        return new GeoserverManager(environment);
    }
    
    @Bean
    public DatabaseHelperUtil databaseHelper(){
    	return new DatabaseHelperUtil(spatialUnitsRepo, georesourceRepo, indicatorsRepo, environment);
    }
    
    @Bean
    public IndicatorDatabaseHandler indicatorDatabaseHandler(){
    	return new IndicatorDatabaseHandler();
    }
    
    @Bean
    public IndicatorsMapper indicatorMapper(){
    	return new IndicatorsMapper(indicatorSpatialUnitsRepo, indicatorsRepo);
    }
    
    @Bean
    public GeoresourcesMapper georesourcesMapper(){
    	return new GeoresourcesMapper(georesourceRepo, georesourcePeriodsOfValidityRepo);
    }
    
    @Bean
    public SpatialUnitsMapper spatialUnitsMapper(){
    	return new SpatialUnitsMapper(spatialUnitsRepo, spatialUnitsPeriodsOfValidityRepo);
    }
    
    @Bean
    public GeoresourceReferenceMapper georesourceReferenceMapper(){
    	return new GeoresourceReferenceMapper(georesourceRepo);
    }
    
    @Bean
    public IndicatorReferenceMapper indicatorReferenceMapper(){
    	return new IndicatorReferenceMapper(indicatorsRepo);
    }
    
    @Bean
    public ReferenceManager referenceManager(){
    	return new ReferenceManager(indicatorRefRepo, georesourceRefRepo, indicatorsRepo, georesourceRepo);
    }
    
    @Bean
    public TopicsHelper topicsHelper(){
    	return new TopicsHelper(topicsRepo);
    }
}
