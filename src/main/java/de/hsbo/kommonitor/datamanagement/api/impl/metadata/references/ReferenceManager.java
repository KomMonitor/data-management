package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.IndicatorPOSTInputTypeRefrencesToGeoresources;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.IndicatorPOSTInputTypeRefrencesToOtherIndicators;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.IndicatorReferenceType;

public class ReferenceManager {

	private static IndicatorReferenceRepository indicatorRefRepo;

	private static GeoresourceReferenceRepository georesourceRefRepo;

	private static IndicatorsMetadataRepository indicatorsMetadataRepo;

	private static GeoresourcesMetadataRepository georesourcesMetadataRepo;

	public ReferenceManager(IndicatorReferenceRepository indicatorRefRepository,
			GeoresourceReferenceRepository georesourceRefRepository, IndicatorsMetadataRepository indicatorsRepo, GeoresourcesMetadataRepository georesourceRepo) {
		indicatorRefRepo = indicatorRefRepository;
		georesourceRefRepo = georesourceRefRepository;
		
		indicatorsMetadataRepo = indicatorsRepo;
		georesourcesMetadataRepo = georesourceRepo;
	}

	public static void createReferences(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources,
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) throws Exception {
		
		/*
		 * first einsure that all referenced indicators and georesources exist!
		 * 
		 * else throw an error
		 */
		for (IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorRef : refrencesToOtherIndicators) {
			if (!indicatorsMetadataRepo.existsByDatasetId(indicatorRef.getIndicatorId()))
				throw new Exception("Indicator references another indicator, for which no metadata entry exists. The faulty indicatorId is: " + indicatorRef.getIndicatorId());
		}
		
		for (IndicatorPOSTInputTypeRefrencesToGeoresources georesourceRef : refrencesToGeoresources) {
			if (!georesourcesMetadataRepo.existsByDatasetId(georesourceRef.getGeoresourceId()))
				throw new Exception("Indicator references a georesource, for which no metadata entry exists. The faulty georsourceId is: " + georesourceRef.getGeoresourceId());
		}

		List<IndicatorReferenceEntity> indicatorRefEntities = IndicatorReferenceMapper
				.mapToEntities(refrencesToOtherIndicators, indicatorId);

		indicatorRefRepo.saveAll(indicatorRefEntities);

		List<GeoresourceReferenceEntity> georesourceRefEntities = GeoresourceReferenceMapper
				.mapToEntities(refrencesToGeoresources, indicatorId);

		georesourceRefRepo.saveAll(georesourceRefEntities);

	}

	public static void updateReferences(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources,
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) throws Exception {
		/*
		 * if resource already exists, then only update it
		 * 
		 * else save as new resource
		 */
		
		for (IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorRef : refrencesToOtherIndicators) {
			if (!indicatorsMetadataRepo.existsByDatasetId(indicatorRef.getIndicatorId()))
				throw new Exception("Indicator references another indicator, for which no metadata entry exists. The faulty indicatorId is: " + indicatorRef.getIndicatorId());
		}
		
		for (IndicatorPOSTInputTypeRefrencesToGeoresources georesourceRef : refrencesToGeoresources) {
			if (!georesourcesMetadataRepo.existsByDatasetId(georesourceRef.getGeoresourceId()))
				throw new Exception("Indicator references a georesource, for which no metadata entry exists. The faulty georsourceId is: " + georesourceRef.getGeoresourceId());
		}
		
		for (IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorReference : refrencesToOtherIndicators) {
			String referencedIndicatorId = indicatorReference.getIndicatorId();
			if (indicatorRefRepo.existsByIndicatorIdAndReferencedIndicatorId(indicatorId, referencedIndicatorId)) {
				// only update
				IndicatorReferenceEntity entity = indicatorRefRepo
						.findByIndicatorIdAndReferencedIndicatorId(indicatorId, referencedIndicatorId);
				entity.setIndicatorId(indicatorId);
				entity.setReferenceDescription(indicatorReference.getReferenceDescription());
				entity.setReferencedIndicatorId(indicatorReference.getIndicatorId());
				
				indicatorRefRepo.saveAndFlush(entity);
			} else {
				// save new resource
				indicatorRefRepo.saveAndFlush(IndicatorReferenceMapper.mapToEntity(indicatorId, indicatorReference));
			}
		}

		for (IndicatorPOSTInputTypeRefrencesToGeoresources georesourceReference : refrencesToGeoresources) {
			String referencedGeoresourceId = georesourceReference.getGeoresourceId();
			if (georesourceRefRepo.existsByMainIndicatorIdAndReferencedGeoresourceId(indicatorId,
					referencedGeoresourceId)) {
				// only update
				GeoresourceReferenceEntity entity = georesourceRefRepo
						.findByMainIndicatorIdAndReferencedGeoresourceId(indicatorId, referencedGeoresourceId);
				entity.setMainIndicatorId(indicatorId);
				entity.setReferenceDescription(georesourceReference.getReferenceDescription());
				entity.setReferencedGeoresourceId(georesourceReference.getGeoresourceId());
				
				georesourceRefRepo.saveAndFlush(entity);
			} else {
				// save new resource
				georesourceRefRepo.saveAndFlush(GeoresourceReferenceMapper.mapToEntity(georesourceReference, indicatorId));
			}
		}

	}

	public static List<IndicatorReferenceType> getIndicatorReferences(String indicatorId) {
		List<IndicatorReferenceEntity> entities = indicatorRefRepo.findByIndicatorId(indicatorId);

		return IndicatorReferenceMapper.mapToSwaggerModel(entities);
	}

	public static List<GeoresourceReferenceType> getGeoresourcesReferences(String indicatorId) {
		List<GeoresourceReferenceEntity> entities = georesourceRefRepo.findByMainIndicatorId(indicatorId);

		return GeoresourceReferenceMapper.mapToSwaggerModel(entities);
	}

	public static void removeReferences(String indicatorId) {
		if(indicatorRefRepo.existsByIndicatorId(indicatorId))
			indicatorRefRepo.deleteByIndicatorId(indicatorId);
		if(georesourceRefRepo.existsByMainIndicatorId(indicatorId))
			georesourceRefRepo.deleteByMainIndicatorId(indicatorId);

	}

	public static boolean removeReferencesByGeoresourceId(String georesourceId) {
		if(georesourceRefRepo.existsByReferencedGeoresourceId(georesourceId))
			georesourceRefRepo.deleteByReferencedGeoresourceId(georesourceId);
		
		return true;
	}

	public static List<IndicatorReferenceEntity> getAllIndicatorReferences() {
		List<IndicatorReferenceEntity> entities = indicatorRefRepo.findAll();		

		return entities;
	}
	
	public static List<GeoresourceReferenceEntity> getAllGeoresourceReferences() {
		List<GeoresourceReferenceEntity> entities = georesourceRefRepo.findAll();

		return entities;
	}
}
