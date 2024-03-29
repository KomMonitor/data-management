package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import de.hsbo.kommonitor.datamanagement.model.ProcessInputType.DataTypeEnum;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "ScriptInputParameters")
public class ScriptInputParameterEntity {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String inputParameterId = null;
	
	private String name = null;
	@Column(columnDefinition="text")
	private String description = null;
	private DataTypeEnum dataType = null;
	@Column(columnDefinition="text")
	private String defaultValue = null;
	private double maxParameterValueForNumericInputs;
	private double minParameterValueForNumericInputs;
	
//	@ManyToMany(mappedBy = "scriptInputParameters")
//    private Collection<ScriptMetadataEntity> scriptsMetadata;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataTypeEnum getDataType() {
		return dataType;
	}

	public void setDataType(DataTypeEnum dataType) {
		this.dataType = dataType;
	}

	public double getMaxParameterValueForNumericInputs() {
		return maxParameterValueForNumericInputs;
	}

	public void setMaxParameterValueForNumericInputs(double maxParameterValueForNumericInputs) {
		this.maxParameterValueForNumericInputs = maxParameterValueForNumericInputs;
	}

	public double getMinParameterValueForNumericInputs() {
		return minParameterValueForNumericInputs;
	}

	public void setMinParameterValueForNumericInputs(double minParameterValueForNumericInputs) {
		this.minParameterValueForNumericInputs = minParameterValueForNumericInputs;
	}

//	public Collection<ScriptMetadataEntity> getScriptsMetadata() {
//		return scriptsMetadata;
//	}
//
//	public void setScriptsMetadata(Collection<ScriptMetadataEntity> scriptsMetadata) {
//		this.scriptsMetadata = scriptsMetadata;
//	}

	public String getInputParameterId() {
		return inputParameterId;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
