package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorReferenceRepository extends JpaRepository <IndicatorReferenceEntity, Long> {
	IndicatorReferenceEntity findByIndicatorId(String indicatorId);

	boolean existsByIndicatorId(String indicatorId);


	void deleteByIndicatorId(String indicatorId);


	

}