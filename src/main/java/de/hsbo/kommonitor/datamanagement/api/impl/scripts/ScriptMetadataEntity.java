package de.hsbo.kommonitor.datamanagement.api.impl.scripts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.*;

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
	@Column(columnDefinition="text")
	private String description = null;
	private String indicatorId = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate = null;

	private byte[] scriptCode = null;

	@ManyToMany
	@JoinTable(name = "scripts_inputparameters", joinColumns = @JoinColumn(name = "script_id", referencedColumnName = "scriptid"), inverseJoinColumns = @JoinColumn(name = "inputparameter_id", referencedColumnName = "inputparameterid"))
	private Collection<ScriptInputParameterEntity> scriptInputParameters;

	@ManyToMany
	@JoinTable(name = "scripts_requiredindicators", joinColumns = @JoinColumn(name = "script_id", referencedColumnName = "scriptid"), inverseJoinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
	private Collection<MetadataIndicatorsEntity> requiredIndicators;

	@ManyToMany
	@JoinTable(name = "scripts_requiredgeoresources", joinColumns = @JoinColumn(name = "script_id", referencedColumnName = "scriptid"), inverseJoinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
	private Collection<MetadataGeoresourcesEntity> requiredGeoresources;

	@ManyToOne(optional = false)
	@JoinColumn(name = "indicatorid", updatable = false, insertable = false)
	private MetadataIndicatorsEntity metadataIndicatorsEntity;

	public MetadataIndicatorsEntity getMetadataIndicatorsEntity() {
		return metadataIndicatorsEntity;
	}

	public void setMetadataIndicatorsEntity(MetadataIndicatorsEntity metadataIndicatorsEntity) {
		this.metadataIndicatorsEntity = metadataIndicatorsEntity;
	}

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

	public byte[] getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(byte[] scriptCode) {
		this.scriptCode = scriptCode;
	}

	public HashSet<ScriptInputParameterEntity> getScriptInputParameters() {
		return new HashSet<ScriptInputParameterEntity>(scriptInputParameters);
	}

	public void setScriptInputParameters(Collection<ScriptInputParameterEntity> scriptInputParameters) {
		this.scriptInputParameters = new HashSet<ScriptInputParameterEntity>(scriptInputParameters);
	}

	public void addScriptInputParameter(ScriptInputParameterEntity scriptInputParameter) {
		if (this.scriptInputParameters == null) {
			this.scriptInputParameters = new HashSet<ScriptInputParameterEntity>();
		}
		this.scriptInputParameters.add(scriptInputParameter);
	}

	public HashSet<MetadataIndicatorsEntity> getRequiredIndicators() {
		return new HashSet<MetadataIndicatorsEntity>(requiredIndicators);
	}

	public void setRequiredIndicators(Collection<MetadataIndicatorsEntity> requiredIndicators) {
		this.requiredIndicators = new HashSet<MetadataIndicatorsEntity>(requiredIndicators);
	}

	public void addRequiredIndicator(MetadataIndicatorsEntity requiredIndicator) {
		if (this.requiredIndicators == null) {
			this.requiredIndicators = new HashSet<MetadataIndicatorsEntity>();
		}
		this.requiredIndicators.add(requiredIndicator);
	}

	public HashSet<MetadataGeoresourcesEntity> getRequiredGeoresources() {
		return new HashSet<MetadataGeoresourcesEntity>(requiredGeoresources);
	}

	public void setRequiredGeoresources(Collection<MetadataGeoresourcesEntity> requiredGeoresources) {
		this.requiredGeoresources = new HashSet<MetadataGeoresourcesEntity>(requiredGeoresources);
	}

	public void addRequiredGeoresources(MetadataGeoresourcesEntity requiredGeoressource) {
		if (this.requiredGeoresources == null) {
			this.requiredGeoresources = new HashSet<MetadataGeoresourcesEntity>();
		}
		this.requiredGeoresources.add(requiredGeoressource);
	}

	public String getScriptId() {
		return scriptId;
	}

}
