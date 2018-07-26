package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.ArrayList;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeRefrencesToGeoresources;

public class GeoresourceReferenceMapper {

	public static List<GeoresourceReferenceEntity> mapToEntities(
			List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources, String indicatorId) {
		List<GeoresourceReferenceEntity> entities = new ArrayList<>(refrencesToGeoresources.size());

		for (IndicatorPOSTInputTypeRefrencesToGeoresources georesourceRefInput : refrencesToGeoresources) {
			entities.add(mapToEntity(georesourceRefInput, indicatorId));
		}
		return entities;
	}

	private static GeoresourceReferenceEntity mapToEntity(
			IndicatorPOSTInputTypeRefrencesToGeoresources georesourceRefInput, String indicatorId) {
		GeoresourceReferenceEntity entity = new GeoresourceReferenceEntity();

		entity.setMainIndicatorId(indicatorId);
		entity.setReferenceDescription(georesourceRefInput.getReferenceDescription());
		entity.setReferencedGeoresourceId(georesourceRefInput.getGeoresourceId());
		
		return entity;
	}

}
