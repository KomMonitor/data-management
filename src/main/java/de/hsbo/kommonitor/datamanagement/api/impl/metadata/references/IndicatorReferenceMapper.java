package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeRefrencesToOtherIndicators;

public class IndicatorReferenceMapper {

	public static List<IndicatorReferenceEntity> mapToEntities(
			List<IndicatorPOSTInputTypeRefrencesToOtherIndicators> refrencesToOtherIndicators, String indicatorId) {
		List<IndicatorReferenceEntity> entities = new ArrayList<>(refrencesToOtherIndicators.size());
		
		for (IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorReferenceInput : refrencesToOtherIndicators) {
			entities.add(mapToEntity(indicatorId, indicatorReferenceInput));
		}
		
		return entities;
	}

	private static IndicatorReferenceEntity mapToEntity(String indicatorId,
			IndicatorPOSTInputTypeRefrencesToOtherIndicators indicatorReferenceInput) {
		IndicatorReferenceEntity entity = new IndicatorReferenceEntity();
		
		entity.setIndicatorId(indicatorId);
		entity.setReferencedIndicatorId(indicatorReferenceInput.getGeoresourceId());
		entity.setReferenceDescription(indicatorReferenceInput.getReferenceDescription());
		
		return entity;
	}

}
