package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

@Entity(name = "MetadataIndicators")
public class MetadataIndicatorsEntity extends AbstractMetadata {

	private String processDescrition = null;
	private String unit = null;
	private CreationTypeEnum creationType = null;
	private String featureViewDbTableName = null;
	
	/*
	 * references to other indicators are mapped by hand
	 * within the entity "IndicatorReferenceEntity"
	 */
	
	/*
	 * references to other georesources are mapped by hand
	 * within the entity "GeoresourceReferenceEntity"
	 */

	@ManyToMany
	@JoinTable(name = "metadataIndicators_topics", 
	joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), 
	inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "topicid"))
	private Collection<TopicsEntity> indicatorTopics;
	
	public String getProcessDescrition() {
		return processDescrition;
	}

	public void setProcessDescrition(String processDescrition) {
		this.processDescrition = processDescrition;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public CreationTypeEnum getCreationType() {
		return creationType;
	}

	public void setCreationType(CreationTypeEnum creationType) {
		this.creationType = creationType;
	}

	public Collection<TopicsEntity> getIndicatorTopics() {
		return indicatorTopics;
	}

	public void setIndicatorTopics(Collection<TopicsEntity> indicatorsTopics) {
		this.indicatorTopics = indicatorsTopics;
	}

	public String getFeatureViewDbTableName() {
		return featureViewDbTableName;
	}

	public void setFeatureViewDbTableName(String featureViewDbTableName) {
		this.featureViewDbTableName = featureViewDbTableName;
	}

}
