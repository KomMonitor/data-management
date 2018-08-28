package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.features.management.DatabaseHelperUtil;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessInputType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptOverviewType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptPUTInputType;

@Transactional
@Repository
@Component
public class ScriptManager {

	private static Logger logger = LoggerFactory.getLogger(ScriptManager.class);
	
	@Autowired
	private ScriptInputParameterRepository inputParameterRepo;

	@Autowired
	private ScriptMetadataRepository scriptMetadataRepo;
	
	public String addScript(ProcessScriptPOSTInputType processScriptData) throws Exception{
		String scriptName = processScriptData.getName();
		logger.info("Trying to persist script data with '{}'", scriptName);
		/*
		 * analyse input type
		 * 
		 * make instances of ScriptMetadataENtitz and ScriptInputParameterEntity
		 * 
		 * save instances to db
		 * 
		 * return id
		 */

		if (scriptMetadataRepo.existsByName(scriptName)) {
			logger.error("The script with name '{}' already exists. Thus aborting add script request.", scriptName);
			throw new Exception("script already exists. Aborting add script request.");
		}

		ScriptMetadataEntity scriptMetadata = new ScriptMetadataEntity();
		scriptMetadata.setDescription(processScriptData.getDescription());
		scriptMetadata.setIndicatorId(processScriptData.getAssociatedIndicatorId());
		scriptMetadata.setName(scriptName);
		scriptMetadata.setScriptCode(processScriptData.getScriptCode());
		
		/*
		 * deal with requiredIndicators and requiredGeoresources
		 */
		List<String> requiredIndicatorIds = processScriptData.getRequiredIndicatorIds();
		for (String requiredIndicatorId : requiredIndicatorIds) {
			MetadataIndicatorsEntity metadataIndicator = DatabaseHelperUtil.getIndicatorMetadataEntity(requiredIndicatorId);
			
			if(metadataIndicator == null){
				logger.error("required indicator with id '{}' does not exist.", requiredIndicatorId);
				throw new Exception("required indicator with id " + requiredIndicatorId + " does not exist.");
			}
			scriptMetadata.addRequiredIndicator(metadataIndicator);
		}
		
		List<String> requiredGeoresourceIds = processScriptData.getRequiredGeoresourceIds();
		for (String requiredGeoresourceId : requiredGeoresourceIds) {
			MetadataGeoresourcesEntity metadataGeoresource = DatabaseHelperUtil.getGeoresourceMetadataEntity(requiredGeoresourceId);
			
			if(metadataGeoresource == null){
				logger.error("required georesource with id '{}' does not exist.", requiredGeoresourceId);
				throw new Exception("required georesource with id " + requiredGeoresourceId + " does not exist.");
			}
			scriptMetadata.addRequiredGeoresources(metadataGeoresource);
		}
		
		
		/*
		 * deal with processInputs
		 */
		List<ScriptInputParameterEntity> scriptInputParameters = persistAndGetInputParameters(processScriptData.getVariableProcessParameters());
		scriptMetadata.setScriptInputParameters(scriptInputParameters);
		scriptMetadata.setLastUpdate(new Date());

		/*
		 * ID will be autogenerated from JPA / Hibernate
		 */

		scriptMetadataRepo.saveAndFlush(scriptMetadata);

		return scriptMetadata.getScriptId();
	}

	private List<ScriptInputParameterEntity> persistAndGetInputParameters(
			List<ProcessInputType> processParameters) {
		List<ScriptInputParameterEntity> scriptInputParameters = new ArrayList<ScriptInputParameterEntity>(processParameters.size());
	
		for (ProcessInputType processInputParameter : processParameters) {
			ScriptInputParameterEntity inputParameterEntity = ScriptMapper.mapToScriptInputParameterEntity(processInputParameter);
			
			inputParameterRepo.saveAndFlush(inputParameterEntity);
			scriptInputParameters.add(inputParameterEntity);
		}
		return scriptInputParameters;
	}

	public boolean deleteScriptByIndicatorId(String indicatorId) throws ResourceNotFoundException{
		logger.info("Trying to delete script dataset with indicatorId '{}'", indicatorId);
		if (scriptMetadataRepo.existsByIndicatorId(indicatorId)) {
			ScriptMetadataEntity scriptMetadata = scriptMetadataRepo.findByIndicatorId(indicatorId);
			/*
			 * also remove all corresponding processInputParameters
			 */
			deleteAssociatedScriptInputParameters(scriptMetadata);
			
			// now delete script metadata entity
			scriptMetadataRepo.deleteByIndicatorId(indicatorId);
			
			return true;
		} else {
			logger.error("No script with indicatorId '{}' was found in database. Delete request has no effect.", indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete script dataset, but no script metadata exists with indicatorId " + indicatorId);
		}
	}

	private void deleteAssociatedScriptInputParameters(ScriptMetadataEntity scriptMetadata) {
		Collection<ScriptInputParameterEntity> scriptInputParameters = scriptMetadata.getScriptInputParameters();
		for (ScriptInputParameterEntity scriptInputParameterEntity : scriptInputParameters) {
			inputParameterRepo.deleteByInputParameterId(scriptInputParameterEntity.getInputParameterId());
		}
	}

	public List<ProcessScriptOverviewType> getAllScriptsMetadata() {
		logger.info("Retrieving all script metadata from db");

		List<ScriptMetadataEntity> scriptEntities = scriptMetadataRepo.findAll();
		List<ProcessScriptOverviewType> scriptsMetadata = ScriptMapper.mapToSwaggerScripts(scriptEntities);

		return scriptsMetadata;
	}

	public ProcessScriptOverviewType getScriptMetadataByIndicatorId(String indicatorId) throws ResourceNotFoundException {
		logger.info("Retrieving script metadata from db for indicatorId '{}'", indicatorId);
		
		if (!scriptMetadataRepo.existsByIndicatorId(indicatorId)){
			logger.error("No script with indicatorId '{}' was found in database. Get script metadata request has no effect.", indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get script metadata, but no script metadata exists with indicatorId " + indicatorId);
		}

		ScriptMetadataEntity scriptEntity = scriptMetadataRepo.findByIndicatorId(indicatorId);
		ProcessScriptOverviewType scriptMetadata = ScriptMapper.mapToSwaggerScript(scriptEntity);

		return scriptMetadata;
	}

	public String updateScriptForIndicatorId(ProcessScriptPUTInputType processScriptData, String indicatorId)
			throws Exception {
		logger.info("Trying to update script for associated indicatorId '{}'", indicatorId);
		if (scriptMetadataRepo.existsByIndicatorId(indicatorId)) {
			ScriptMetadataEntity scriptMetadataEntity = scriptMetadataRepo.findByIndicatorId(indicatorId);
			
			updateScriptMetadataEntity(processScriptData, scriptMetadataEntity);
			
			scriptMetadataRepo.saveAndFlush(scriptMetadataEntity);
			return indicatorId;
		} else {
			logger.error("No script for associated indicatorId id '{}' was found in database. Update request has no effect.", indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
			"Tried to update script, but no script exists for associated indicatorId " + indicatorId);
		}
	}

	private void updateScriptMetadataEntity(ProcessScriptPUTInputType processScriptData,
			ScriptMetadataEntity scriptMetadataEntity) throws Exception {
		//set all components according to input
		scriptMetadataEntity.setDescription(processScriptData.getDescription());
		scriptMetadataEntity.setName(processScriptData.getName());
		scriptMetadataEntity.setScriptCode(processScriptData.getScriptCode());
		
		/*
		 * deal with required indicators and georesource
		 * check if they are already there, if not create them
		 */
		List<String> requiredGeoresourceIdsFromInput = processScriptData.getRequiredGeoresourceIds();
		Collection<MetadataGeoresourcesEntity> requiredGeoresourcesFromEntity = scriptMetadataEntity.getRequiredGeoresources();
		boolean georesourceAlreadyContained = false;
		for (String inputGeoresourceId : requiredGeoresourceIdsFromInput) {
			georesourceAlreadyContained = false;
			for (MetadataGeoresourcesEntity metadataGeoresourcesEntity : requiredGeoresourcesFromEntity) {
				if (inputGeoresourceId.equalsIgnoreCase(metadataGeoresourcesEntity.getDatasetId())){
					// georesourceId was already there
					// so we do not have to add it
					georesourceAlreadyContained = true;
					break;
				}
			}
			if(!georesourceAlreadyContained){
				MetadataGeoresourcesEntity georesourceMetadataEntity = DatabaseHelperUtil.getGeoresourceMetadataEntity(inputGeoresourceId);
				
				if(georesourceMetadataEntity == null){
					logger.error("required georesource with id '{}' does not exist.", inputGeoresourceId);
					throw new Exception("required georesource with id " + inputGeoresourceId + " does not exist.");
				}
				
				requiredGeoresourcesFromEntity.add(georesourceMetadataEntity);
			}
		}
		scriptMetadataEntity.setRequiredGeoresources(requiredGeoresourcesFromEntity);
		
		List<String> requiredIndicatorIdsFromInput = processScriptData.getRequiredIndicatorIds();
		Collection<MetadataIndicatorsEntity> requiredIndicatorsFromEntity = scriptMetadataEntity.getRequiredIndicators();
		boolean indicatorAlreadyContained = false;
		for (String inputIndicatorId : requiredIndicatorIdsFromInput) {
			indicatorAlreadyContained = false;
			for (MetadataIndicatorsEntity metadataIndicatorsEntity : requiredIndicatorsFromEntity) {
				if (inputIndicatorId.equalsIgnoreCase(metadataIndicatorsEntity.getDatasetId())){
					// georesourceId was already there
					// so we do not have to add it
					indicatorAlreadyContained = true;
					break;
				}
			}
			if(!indicatorAlreadyContained){
				MetadataIndicatorsEntity indicatorMetadataEntity = DatabaseHelperUtil.getIndicatorMetadataEntity(inputIndicatorId);
				if(indicatorMetadataEntity == null){
					logger.error("required indicator with id '{}' does not exist.", inputIndicatorId);
					throw new Exception("required indicator with id " + inputIndicatorId + " does not exist.");
				}
				requiredIndicatorsFromEntity.add(indicatorMetadataEntity);
			}
		}
		scriptMetadataEntity.setRequiredIndicators(requiredIndicatorsFromEntity);
		
		//also manage inputParameters in their own REPO
		List<ProcessInputType> parametersFromInput = processScriptData.getVariableProcessParameters();
		Collection<ScriptInputParameterEntity> parametersFromEntity = scriptMetadataEntity.getScriptInputParameters();
		boolean parameterAlreadyContained = false;
		for (ProcessInputType parameterFromInput : parametersFromInput) {
			parameterAlreadyContained = false;
			for (ScriptInputParameterEntity scriptInputParameterEntity : parametersFromEntity) {
				if (parameterFromInput.getName().equalsIgnoreCase(scriptInputParameterEntity.getName())){
					parameterAlreadyContained = true;
					break;
				}
			}
			if(!parameterAlreadyContained){
				/*
				 * create entity, save it within repo and add to list of parameters
				 */
				ScriptInputParameterEntity scriptParameterEntity = ScriptMapper.mapToScriptInputParameterEntity(parameterFromInput);
				inputParameterRepo.saveAndFlush(scriptParameterEntity);
				parametersFromEntity.add(scriptParameterEntity);
			}
		}
		scriptMetadataEntity.setScriptInputParameters(parametersFromEntity);
		
		scriptMetadataEntity.setLastUpdate(new Date());
	}

	public String getScriptCodeForIndicatorId(String indicatorId) throws ResourceNotFoundException {
		if (!scriptMetadataRepo.existsByIndicatorId(indicatorId)){
			logger.error("No script with indicatorId '{}' was found in database. Get script code request has no effect.", indicatorId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get script code, but no script metadata exists with indicatorId " + indicatorId);
		}
		
		ScriptMetadataEntity scriptMetadataEntity = scriptMetadataRepo.findByIndicatorId(indicatorId);
		
		return scriptMetadataEntity.getScriptCode();
	}

	public boolean deleteScriptByScriptId(String scriptId) throws ResourceNotFoundException {
		logger.info("Trying to delete script dataset with scriptId '{}'", scriptId);
		if (scriptMetadataRepo.existsByScriptId(scriptId)) {
			ScriptMetadataEntity scriptMetadata = scriptMetadataRepo.findByScriptId(scriptId);
			/*
			 * also remove all corresponding processInputParameters
			 */
			deleteAssociatedScriptInputParameters(scriptMetadata);
			
			// now delete script metadata entity
			scriptMetadataRepo.deleteByScriptId(scriptId);
			
			return true;
		} else {
			logger.error("No script with scriptId '{}' was found in database. Delete request has no effect.", scriptId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to delete script dataset, but no script metadata exists with scriptId " + scriptId);
		}
	}

	public String getScriptCodeForScriptId(String scriptId) throws ResourceNotFoundException {
		if (!scriptMetadataRepo.existsByScriptId(scriptId)){
			logger.error("No script with scriptId '{}' was found in database. Get script code request has no effect.", scriptId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get script code, but no script metadata exists with scriptId " + scriptId);
		}
		
		ScriptMetadataEntity scriptMetadataEntity = scriptMetadataRepo.findByScriptId(scriptId);
		
		return scriptMetadataEntity.getScriptCode();
	}

	public ProcessScriptOverviewType getScriptMetadataByScriptId(String scriptId) throws ResourceNotFoundException {
		logger.info("Retrieving script metadata from db for scriptId '{}'", scriptId);
		
		if (!scriptMetadataRepo.existsByScriptId(scriptId)){
			logger.error("No script with scriptId '{}' was found in database. Get script metadata request has no effect.", scriptId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to get script metadata, but no script metadata exists with scriptId " + scriptId);
		}

		ScriptMetadataEntity scriptEntity = scriptMetadataRepo.findByScriptId(scriptId);
		ProcessScriptOverviewType scriptMetadata = ScriptMapper.mapToSwaggerScript(scriptEntity);

		return scriptMetadata;
	}

	public String updateScriptForScriptId(ProcessScriptPUTInputType processScriptData, String scriptId) throws Exception {
		logger.info("Trying to update script for associated scriptId '{}'", scriptId);
		if (scriptMetadataRepo.existsByScriptId(scriptId)) {
			ScriptMetadataEntity scriptMetadataEntity = scriptMetadataRepo.findByScriptId(scriptId);
			
			updateScriptMetadataEntity(processScriptData, scriptMetadataEntity);

			scriptMetadataRepo.saveAndFlush(scriptMetadataEntity);
			return scriptId;
		} else {
			logger.error(
					"No script for associated scriptId id '{}' was found in database. Update request has no effect.",
					scriptId);
			throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Tried to update script, but no script exists for associated scriptId " + scriptId);
		}
	}
	

	

}