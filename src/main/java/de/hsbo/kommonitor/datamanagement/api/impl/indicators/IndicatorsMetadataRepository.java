package de.hsbo.kommonitor.datamanagement.api.impl.indicators;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.model.legacy.indicators.IndicatorTypeEnum;

public interface IndicatorsMetadataRepository extends JpaRepository<MetadataIndicatorsEntity, Long> {
	MetadataIndicatorsEntity findByDatasetId(String datasetId);
	
	MetadataIndicatorsEntity findByDatasetName(String datasetName);

	boolean existsByDatasetId(String datasetId);

	boolean existsByDatasetName(String datasetName);
	
	boolean existsByDatasetNameAndCharacteristicValueAndIndicatorType(String datasetName, String characteristicValue, IndicatorTypeEnum indicatorType);

	void deleteByDatasetName(String datasetName);

	void deleteByDatasetId(String indicatorId);


	

}
