package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.hsbo.kommonitor.datamanagement.model.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeRefrencesToGeoresources;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeRefrencesToOtherIndicators;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorReferenceType;

public class ReferenceManager {
	
	@Autowired
	private static IndicatorReferenceRepository indicatorRefRepo;
	@Autowired
	private static GeoresourceReferenceRepository georesourceRefRepo;

	public static void createReferences(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources,
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) {
		
		List<IndicatorReferenceEntity> indicatorRefEntities =  IndicatorReferenceMapper.mapToEntities(refrencesToOtherIndicators, indicatorId);
		
		indicatorRefRepo.saveAll(indicatorRefEntities);
		
		List<GeoresourceReferenceEntity> georesourceRefEntities =  GeoresourceReferenceMapper.mapToEntities(refrencesToGeoresources, indicatorId);
		
		georesourceRefRepo.saveAll(georesourceRefEntities);
		
	}

	public static void updateReferences(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources,
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) {
		// TODO Auto-generated method stub
		
	}

	public static List<IndicatorReferenceType> getIndicatorReferences(String datasetId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<GeoresourceReferenceType> getGeoresourcesReferences(String datasetId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void removeReferences(String indicatorId) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
//TODO all
}
