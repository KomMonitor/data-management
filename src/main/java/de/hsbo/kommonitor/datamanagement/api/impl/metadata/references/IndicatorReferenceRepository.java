package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatorReferenceRepository extends JpaRepository <IndicatorReferenceEntity, Long> {
	List<IndicatorReferenceEntity> findByIndicatorId(String indicatorId);
	
	IndicatorReferenceEntity findByIndicatorIdAndReferencedIndicatorId(String indicatorId,
			String referencedIndicatorId);

	boolean existsByIndicatorId(String indicatorId);

	boolean existsByIndicatorIdAndReferencedIndicatorId(String indicatorId, String referencedIndicatorId);

	void deleteByIndicatorId(String indicatorId);
}