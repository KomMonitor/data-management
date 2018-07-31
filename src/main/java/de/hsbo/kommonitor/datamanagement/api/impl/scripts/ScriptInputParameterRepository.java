package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScriptInputParameterRepository extends JpaRepository<ScriptInputParameterEntity, Long> {

	ScriptInputParameterEntity findByInputParameterId(String inputParameterId);
	
	void deleteByInputParameterId(String inputParameterId);


}
