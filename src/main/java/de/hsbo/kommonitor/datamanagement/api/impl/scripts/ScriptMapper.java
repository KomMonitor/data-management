package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessInputType;
import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessScriptOverviewType;

public class ScriptMapper {

	public static ScriptInputParameterEntity mapToScriptInputParameterEntity(ProcessInputType processInputParameter) {
		ScriptInputParameterEntity inputParameterEntity = new ScriptInputParameterEntity();
		inputParameterEntity.setDataType(processInputParameter.getDataType());
		inputParameterEntity.setDescription(processInputParameter.getDescription());
		inputParameterEntity.setMaxParameterValueForNumericInputs(
				processInputParameter.getMaxParameterValueForNumericInputs().doubleValue());
		inputParameterEntity.setMinParameterValueForNumericInputs(
				processInputParameter.getMinParameterValueForNumericInputs().doubleValue());
		inputParameterEntity.setName(processInputParameter.getName());
		inputParameterEntity.setDefaultValue(processInputParameter.getDefaultValue());
		return inputParameterEntity;
	}

	public static ProcessInputType mapToProcessInputType(ScriptInputParameterEntity inputParameterEntity) {
		ProcessInputType processInput = new ProcessInputType();
		processInput.setDataType(inputParameterEntity.getDataType());
		processInput.setDefaultValue(inputParameterEntity.getDefaultValue());
		processInput.setDescription(inputParameterEntity.getDescription());
		processInput.setMaxParameterValueForNumericInputs(
				new BigDecimal(inputParameterEntity.getMaxParameterValueForNumericInputs()));
		processInput.setMinParameterValueForNumericInputs(
				new BigDecimal(inputParameterEntity.getMinParameterValueForNumericInputs()));
		processInput.setName(inputParameterEntity.getName());

		return processInput;
	}

	private static List<ProcessInputType> mapToProcessInputs(
			Collection<ScriptInputParameterEntity> scriptInputParameters) {
		List<ProcessInputType> processInputs = new ArrayList<ProcessInputType>();
		for (ScriptInputParameterEntity scriptInputParameterEntity : scriptInputParameters) {
			processInputs.add(mapToProcessInputType(scriptInputParameterEntity));
		}
		return processInputs;
	}

	public static List<ProcessScriptOverviewType> mapToSwaggerScripts(List<ScriptMetadataEntity> scriptEntities) {
		List<ProcessScriptOverviewType> scriptOverviewTypes = new ArrayList<ProcessScriptOverviewType>(
				scriptEntities.size());
		for (ScriptMetadataEntity scriptMetadataEntity : scriptEntities) {
			scriptOverviewTypes.add(mapToSwaggerScript(scriptMetadataEntity));
		}
		return scriptOverviewTypes;
	}

	public static ProcessScriptOverviewType mapToSwaggerScript(ScriptMetadataEntity scriptMetadataEntity) {
		ProcessScriptOverviewType scriptOverviewType = new ProcessScriptOverviewType();
		scriptOverviewType.setDescription(scriptMetadataEntity.getDescription());
		scriptOverviewType.setIndicatorId(scriptMetadataEntity.getIndicatorId());
		scriptOverviewType.setName(scriptMetadataEntity.getName());
		scriptOverviewType.setScriptId(scriptMetadataEntity.getScriptId());
		scriptOverviewType.setScriptType(scriptMetadataEntity.getScriptType());

		List<String> requiredGeoresourceIds = extractRequiredGeoresourceIds(scriptMetadataEntity);
		scriptOverviewType.setRequiredGeoresourceIds(requiredGeoresourceIds);

		List<String> requiredIndicatorIds = extractRequiredIndicatorIds(scriptMetadataEntity);
		scriptOverviewType.setRequiredIndicatorIds(requiredIndicatorIds);

		scriptOverviewType
				.setVariableProcessParameters(mapToProcessInputs(scriptMetadataEntity.getScriptInputParameters()));

		return scriptOverviewType;
	}

	private static List<String> extractRequiredIndicatorIds(ScriptMetadataEntity scriptMetadataEntity) {
		Collection<MetadataIndicatorsEntity> requiredIndicators = scriptMetadataEntity.getRequiredIndicators();
		List<String> requiredIndicatorIds = new ArrayList<String>();
		for (MetadataIndicatorsEntity metadataIndicatorEntity : requiredIndicators) {
			requiredIndicatorIds.add(metadataIndicatorEntity.getDatasetId());
		}
		return requiredIndicatorIds;
	}

	private static List<String> extractRequiredGeoresourceIds(ScriptMetadataEntity scriptMetadataEntity) {
		Collection<MetadataGeoresourcesEntity> requiredGeoresources = scriptMetadataEntity.getRequiredGeoresources();
		List<String> requiredGeoresourceIds = new ArrayList<String>();
		for (MetadataGeoresourcesEntity metadataGeoresourcesEntity : requiredGeoresources) {
			requiredGeoresourceIds.add(metadataGeoresourcesEntity.getDatasetId());
		}
		return requiredGeoresourceIds;
	}

}
