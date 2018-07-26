package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorReferenceRepository extends JpaRepository <IndicatorReferenceEntity, Long> {
	List<IndicatorReferenceEntity> findByIndicatorId(String indicatorId);

	boolean existsByIndicatorId(String indicatorId);


	void deleteByIndicatorId(String indicatorId);


	

}