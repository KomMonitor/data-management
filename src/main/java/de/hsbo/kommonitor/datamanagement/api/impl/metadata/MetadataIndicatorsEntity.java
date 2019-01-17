package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsHelper;
import de.hsbo.kommonitor.datamanagement.model.indicators.CreationTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.indicators.DefaultClassificationMappingItemType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

@Entity(name = "MetadataIndicators")
public class MetadataIndicatorsEntity extends AbstractMetadata {

	@Column(columnDefinition="text")
	private String processDescription = null;
	private String unit = null;
	private CreationTypeEnum creationType = null;
	private IndicatorTypeEnum indicatorType = null;
	private String lowestSpatialUnitForComputation = null;
	
	private String colorBrewerSchemeName;
	
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
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "metadataIndicators_defaultClassificationMapping", 
	joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), 
	inverseJoinColumns = @JoinColumn(name = "mapping_id", referencedColumnName = "mappingid"))
	private Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems;
	
	public Collection<DefaultClassificationMappingItemType> getDefaultClassificationMappingItems() {
		return defaultClassificationMappingItems;
	}

	public void setDefaultClassificationMappingItems(
			Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems) {
		
//		List<DefaultClassificationMappingItemType> list = new ArrayList<>(defaultClassificationMappingItems);
//		
//		Collections.sort(list);
		
		this.defaultClassificationMappingItems = defaultClassificationMappingItems;
	}

	public String getProcessDescription() {
		return processDescription;
	}

	public void setProcessDescription(String processDescrition) {
		this.processDescription = processDescrition;
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

	public void addTopicsIfNotExist(List<String> applicableTopics)throws Exception {
		if (this.indicatorTopics == null)
			this.indicatorTopics = new ArrayList<>();

		for (String topic : applicableTopics) {
			/*
			 * add topic if not exists
			 */
			if (!topicAlreadyInTopicReferences(topic, applicableTopics))
				this.indicatorTopics.add(TopicsHelper.getTopicByName(topic));
		}
	}
	
	private boolean topicAlreadyInTopicReferences(String topic, List<String> applicableTopics) throws Exception {
		TopicsEntity topicEntity = TopicsHelper.getTopicByName(topic);
		if (applicableTopics.contains(topicEntity))
			return true;
		// if code reaches this line, then the topic is not within the list
		return false;
	}

	public String getColorBrewerSchemeName() {
		return colorBrewerSchemeName;
	}

	public void setColorBrewerSchemeName(String colorBrewerSchemeName) {
		this.colorBrewerSchemeName = colorBrewerSchemeName;
	}

	public IndicatorTypeEnum getIndicatorType() {
		return indicatorType;
	}

	public void setIndicatorType(IndicatorTypeEnum indicatorType) {
		this.indicatorType = indicatorType;
	}

	public String getLowestSpatialUnitForComputation() {
		return lowestSpatialUnitForComputation;
	}

	public void setLowestSpatialUnitForComputation(String lowestSpatialUnitForComputation) {
		this.lowestSpatialUnitForComputation = lowestSpatialUnitForComputation;
	}


}
