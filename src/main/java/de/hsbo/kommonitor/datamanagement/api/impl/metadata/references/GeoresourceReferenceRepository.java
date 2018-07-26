package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoresourceReferenceRepository extends JpaRepository <GeoresourceReferenceEntity, Long> {
	List<GeoresourceReferenceEntity> findByMainIndicatorId(String mainIndicatorId);

	boolean existsByMainIndicatorId(String mainIndicatorId);

	void deleteByMainIndicatorId(String mainIndicatorId);


	

}