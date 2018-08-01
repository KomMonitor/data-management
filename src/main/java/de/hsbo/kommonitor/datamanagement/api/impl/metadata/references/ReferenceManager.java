package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.List;

import de.hsbo.kommonitor.datamanagement.model.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeRefrencesToGeoresources;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeRefrencesToOtherIndicators;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorReferenceType;

public class ReferenceManager {

	private static IndicatorReferenceRepository indicatorRefRepo;

	private static GeoresourceReferenceRepository georesourceRefRepo;

	public ReferenceManager(IndicatorReferenceRepository indicatorRefRepository,
			GeoresourceReferenceRepository georesourceRefRepository) {
		indicatorRefRepo = indicatorRefRepository;
		georesourceRefRepo = georesourceRefRepository;
	}

	public static void createReferences(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources,
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) {

		List<IndicatorReferenceEntity> indicatorRefEntities = IndicatorReferenceMapper
				.mapToEntities(refrencesToOtherIndicators, indicatorId);

		indicatorRefRepo.saveAll(indicatorRefEntities);

		List<GeoresourceReferenceEntity> georesourceRefEntities = GeoresourceReferenceMapper
				.mapToEntities(refrencesToGeoresources, indicatorId);

		georesourceRefRepo.saveAll(georesourceRefEntities);

	}

	public static void updateReferences(List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources,
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) {
		/*
		 * if resource already exists, then only update it
		 * 
		 * else save as new resource
		 */
		for (IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorReference : refrencesToOtherIndicators) {
			String referencedIndicatorId = indicatorReference.getIndicatorId();
			if (indicatorRefRepo.existsByIndicatorIdAndReferencedIndicatorId(indicatorId, referencedIndicatorId)) {
				// only update
				IndicatorReferenceEntity entity = indicatorRefRepo
						.findByIndicatorIdAndReferencedIndicatorId(indicatorId, referencedIndicatorId);
				entity.setIndicatorId(indicatorId);
				entity.setReferenceDescription(indicatorReference.getReferenceDescription());
				entity.setReferencedIndicatorId(indicatorReference.getIndicatorId());
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
		indicatorRefRepo.deleteByIndicatorId(indicatorId);
		georesourceRefRepo.deleteByMainIndicatorId(indicatorId);

	}
}
