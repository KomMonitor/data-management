package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoresourceReferenceRepository extends JpaRepository <GeoresourceReferenceEntity, Long> {
	List<GeoresourceReferenceEntity> findByMainIndicatorId(String mainIndicatorId);
	
	GeoresourceReferenceEntity findByMainIndicatorIdAndReferencedGeoresourceId(String indicatorId,
			String referencedGeoresourceId);

	boolean existsByMainIndicatorId(String mainIndicatorId);
	
	boolean existsByMainIndicatorIdAndReferencedGeoresourceIdId(String indicatorId, String referencedGeoresourceId);

	void deleteByMainIndicatorId(String mainIndicatorId);

}