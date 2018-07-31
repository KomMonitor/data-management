package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScriptMetadataRepository extends JpaRepository<ScriptMetadataEntity, Long> {

	boolean existsByName(String name);

	boolean existsByIndicatorId(String indicatorId);

	ScriptMetadataEntity findByIndicatorId(String indicatorId);

	void deleteByIndicatorId(String indicatorId);


}
