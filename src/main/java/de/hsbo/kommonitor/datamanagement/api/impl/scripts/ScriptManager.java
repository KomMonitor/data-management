package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptOverviewType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPUTInputType;

@Transactional
@Repository
@Component
public class ScriptManager {

	private static Logger logger = LoggerFactory.getLogger(ScriptManager.class);

	public String addScript(ProcessScriptPOSTInputType processScriptData) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteScriptByIndicatorId(String indicatorId) throws ResourceNotFoundException{
		// TODO Auto-generated method stub
		return false;
	}

	public List<ProcessScriptOverviewType> getAllScriptsMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ProcessScriptOverviewType> getScriptMetadataByIndicatorId(String indicatorId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateScriptForIndicatorId(ProcessScriptPUTInputType processScriptData, String indicatorId) {
		// TODO Auto-generated method stub
		return null;
	}
	

	

}
