package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.hsbo.kommonitor.datamanagement.api.impl.georesources.GeoresourcesMetadataRepository;
import de.hsbo.kommonitor.datamanagement.model.indicators.GeoresourceReferenceType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeRefrencesToGeoresources;

public class GeoresourceReferenceMapper {

	@Autowired
	private static GeoresourcesMetadataRepository georesourcesMetadataRepo;

	public static List<GeoresourceReferenceEntity> mapToEntities(
			List<IndicatorPOSTInputTypeRefrencesToGeoresources> refrencesToGeoresources, String indicatorId) {
		List<GeoresourceReferenceEntity> entities = new ArrayList<>(refrencesToGeoresources.size());

		for (IndicatorPOSTInputTypeRefrencesToGeoresources georesourceRefInput : refrencesToGeoresources) {
			entities.add(mapToEntity(georesourceRefInput, indicatorId));
		}
		return entities;
	}

	public static GeoresourceReferenceEntity mapToEntity(
			IndicatorPOSTInputTypeRefrencesToGeoresources georesourceRefInput, String indicatorId) {
		GeoresourceReferenceEntity entity = new GeoresourceReferenceEntity();

		entity.setMainIndicatorId(indicatorId);
		entity.setReferenceDescription(georesourceRefInput.getReferenceDescription());
		entity.setReferencedGeoresourceId(georesourceRefInput.getGeoresourceId());

		return entity;
	}

	public static List<GeoresourceReferenceType> mapToSwaggerModel(List<GeoresourceReferenceEntity> entities) {
		List<GeoresourceReferenceType> references = new ArrayList<GeoresourceReferenceType>(entities.size());

		for (GeoresourceReferenceEntity georesourceReferenceEntity : entities) {
			references.add(mapToSwaggerModel(georesourceReferenceEntity));
		}
		return references;
	}

	public static GeoresourceReferenceType mapToSwaggerModel(GeoresourceReferenceEntity georesourceReferenceEntity) {
		GeoresourceReferenceType reference = new GeoresourceReferenceType();
		reference.setReferencedGeoresourceDescription(georesourceReferenceEntity.getReferenceDescription());
		reference.setReferencedGeoresourceId(georesourceReferenceEntity.getReferencedGeoresourceId());
		/*
		 * set name with the datasetName from associated metadata entry
		 */
		reference.setReferencedGeoresourceName(georesourcesMetadataRepo
				.findByDatasetId(georesourceReferenceEntity.getReferencedGeoresourceId()).getDatasetName());
		return reference;
	}

}
