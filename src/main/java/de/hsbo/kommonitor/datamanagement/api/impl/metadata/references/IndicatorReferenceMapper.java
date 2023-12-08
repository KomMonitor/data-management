package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsMetadataRepository;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.IndicatorPOSTInputTypeRefrencesToOtherIndicators;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.IndicatorReferenceType;

public class IndicatorReferenceMapper {

	private static IndicatorsMetadataRepository indicatorsMetadataRepo;

	public IndicatorReferenceMapper(IndicatorsMetadataRepository indicatorsRepo) {
		indicatorsMetadataRepo = indicatorsRepo;
	}

	public static List<IndicatorReferenceEntity> mapToEntities(
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) {
		List<IndicatorReferenceEntity> entities = new ArrayList<>(refrencesToOtherIndicators.size());

		for (IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorReferenceInput : refrencesToOtherIndicators) {
			entities.add(mapToEntity(indicatorId, indicatorReferenceInput));
		}

		return entities;
	}

	public static IndicatorReferenceEntity mapToEntity(String indicatorId,
			IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorReferenceInput) {
		IndicatorReferenceEntity entity = new IndicatorReferenceEntity();

		entity.setIndicatorId(indicatorId);
		entity.setReferencedIndicatorId(indicatorReferenceInput.getIndicatorId());
		entity.setReferenceDescription(indicatorReferenceInput.getReferenceDescription());

		return entity;
	}

	public static List<IndicatorReferenceType> mapToSwaggerModel(List<IndicatorReferenceEntity> entities) {
		List<IndicatorReferenceType> references = new ArrayList<IndicatorReferenceType>(entities.size());

		for (IndicatorReferenceEntity indicatorReferenceEntity : entities) {
			IndicatorReferenceType swaggerReference = mapToSwaggerModel(indicatorReferenceEntity);
			if(swaggerReference != null) {
				references.add(swaggerReference);
			}	
		}
		
		references.sort(Comparator.comparing(IndicatorReferenceType::getReferencedIndicatorName));

		return references;
	}

	public static IndicatorReferenceType mapToSwaggerModel(IndicatorReferenceEntity indicatorReferenceEntity) {
		if (indicatorsMetadataRepo
				.existsByDatasetId(indicatorReferenceEntity.getReferencedIndicatorId())) {
			IndicatorReferenceType reference = new IndicatorReferenceType();
			reference.setReferencedIndicatorDescription(indicatorReferenceEntity.getReferenceDescription());
			reference.setReferencedIndicatorId(indicatorReferenceEntity.getReferencedIndicatorId());
			/*
			 * set name with the datasetName from associated metadata entry
			 */
			reference.setReferencedIndicatorName(indicatorsMetadataRepo
					.findByDatasetId(indicatorReferenceEntity.getReferencedIndicatorId()).getDatasetName());
			return reference;
		}	
		return null;
	}

}
