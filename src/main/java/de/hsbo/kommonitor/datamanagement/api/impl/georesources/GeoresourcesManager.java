package de.hsbo.kommonitor.datamanagement.api.impl.georesources;



import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsRepository;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourceOverviewType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePATCHInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.georesources.GeoresourcePUTInputType;

@Transactional
@Repository
@Component
public class GeoresourcesManager {
	
	
	private static Logger logger = LoggerFactory.getLogger(GeoresourcesManager.class);

	/**
	*
	*/
//	@PersistenceContext
//	EntityManager em;
	
	@Autowired
	GeoresourcesMetadataRepository georesourcesRepo;

	public String addGeoresource(GeoresourcePOSTInputType featureData) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteGeoresourceDatasetById(String georesourceId) throws ResourceNotFoundException, IOException{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteGeoresourceFeaturesByIdAndDate(String georesourceId, BigDecimal year, BigDecimal month,
			BigDecimal day) throws ResourceNotFoundException, IOException{
		// TODO Auto-generated method stub
		return false;
	}

	public GeoresourceOverviewType getGeoresourceByDatasetId(String georesourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValidGeoresourceFeatures(String georesourceId, BigDecimal year, BigDecimal month, BigDecimal day) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getJsonSchemaForDatasetName(String georesourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateFeatures(GeoresourcePUTInputType featureData, String georesourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateMetadata(GeoresourcePATCHInputType metadata, String georesourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<GeoresourceOverviewType> getAllGeoresourcesMetadata(String topic) {
		// TODO Auto-generated method stub
		/*
		 * topic is an optional parameter and thus might be null!
		 * then get all datasets!
		 */
		return null;
	}
	
	
	//TODO: Methoden zum handling der Georesourcen
	
}
