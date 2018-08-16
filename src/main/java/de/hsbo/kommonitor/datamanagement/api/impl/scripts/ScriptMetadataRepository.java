package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScriptMetadataRepository extends JpaRepository<ScriptMetadataEntity, Long> {

	boolean existsByName(String name);

	boolean existsByIndicatorId(String indicatorId);

	boolean existsByScriptId(String scriptId);

	ScriptMetadataEntity findByIndicatorId(String indicatorId);
	
	ScriptMetadataEntity findByScriptId(String scriptId);

	void deleteByIndicatorId(String indicatorId);

	void deleteByScriptId(String scriptId);


}
