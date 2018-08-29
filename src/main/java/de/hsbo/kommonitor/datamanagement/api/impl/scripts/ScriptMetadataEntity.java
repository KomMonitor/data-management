package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;

@Entity(name = "ScriptMetadata")
public class ScriptMetadataEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String scriptId = null;

	private String name = null;
	private String description = null;
	private String indicatorId = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate = null;

	@Column(columnDefinition="text", length=10485760)
	private String scriptCode = null;

	@ManyToMany
	@JoinTable(name = "scripts_inputparameters", joinColumns = @JoinColumn(name = "script_id", referencedColumnName = "scriptid"), inverseJoinColumns = @JoinColumn(name = "inputparameter_id", referencedColumnName = "inputparameterid"))
	private Collection<ScriptInputParameterEntity> scriptInputParameters;

	@ManyToMany
	@JoinTable(name = "scripts_requiredindicators", joinColumns = @JoinColumn(name = "script_id", referencedColumnName = "scriptid"), inverseJoinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
	private Collection<MetadataIndicatorsEntity> requiredIndicators;

	@ManyToMany
	@JoinTable(name = "scripts_requiredgeoresources", joinColumns = @JoinColumn(name = "script_id", referencedColumnName = "scriptid"), inverseJoinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
	private Collection<MetadataGeoresourcesEntity> requiredGeoresources;

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

	public String getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	public Collection<ScriptInputParameterEntity> getScriptInputParameters() {
		return scriptInputParameters;
	}

	public void setScriptInputParameters(Collection<ScriptInputParameterEntity> scriptInputParameters) {
		this.scriptInputParameters = scriptInputParameters;
	}

	public void addScriptInputParameter(ScriptInputParameterEntity scriptInputParameter) {
		if (this.scriptInputParameters == null) {
			this.scriptInputParameters = new ArrayList<ScriptInputParameterEntity>();
		}
		this.scriptInputParameters.add(scriptInputParameter);
	}

	public Collection<MetadataIndicatorsEntity> getRequiredIndicators() {
		return requiredIndicators;
	}

	public void setRequiredIndicators(Collection<MetadataIndicatorsEntity> requiredIndicators) {
		this.requiredIndicators = requiredIndicators;
	}

	public void addRequiredIndicator(MetadataIndicatorsEntity requiredIndicator) {
		if (this.requiredIndicators == null) {
			this.requiredIndicators = new ArrayList<MetadataIndicatorsEntity>();
		}
		this.requiredIndicators.add(requiredIndicator);
	}

	public Collection<MetadataGeoresourcesEntity> getRequiredGeoresources() {
		return requiredGeoresources;
	}

	public void setRequiredGeoresources(Collection<MetadataGeoresourcesEntity> requiredGeoresources) {
		this.requiredGeoresources = requiredGeoresources;
	}

	public void addRequiredGeoresources(MetadataGeoresourcesEntity requiredGeoressource) {
		if (this.requiredGeoresources == null) {
			this.requiredGeoresources = new ArrayList<MetadataGeoresourcesEntity>();
		}
		this.requiredGeoresources.add(requiredGeoressource);
	}

	public String getScriptId() {
		return scriptId;
	}

}
