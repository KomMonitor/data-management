package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
	private String abbreviation = null;
	private boolean isHeadlineIndicator = false;
	private String interpretation = null;
	
	@ElementCollection
    @CollectionTable(name = "indicator_timestamps", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
    @Column(name = "timestamp")
    private List<String> availableTimestamps;
	
	@ElementCollection
    @CollectionTable(name = "indicator_tags", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
    @Column(name = "tag")
    private List<String> tags;
	
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

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public boolean isHeadlineIndicator() {
		return isHeadlineIndicator;
	}

	public void setHeadlineIndicator(boolean isHeadlineIndicator) {
		this.isHeadlineIndicator = isHeadlineIndicator;
	}

	public String getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getAvailableTimestamps() {
		return availableTimestamps;
	}

	public void setAvailableTimestamps(List<String> availableTimestamps) {
		this.availableTimestamps = availableTimestamps;
	}
	
	public void addTimestampIfNotExist(String timestamp)throws Exception {
		if (this.availableTimestamps == null)
			this.availableTimestamps = new ArrayList<>();

		if (!timestampAlreadyInTimestampReferences(timestamp, this.availableTimestamps))
			this.availableTimestamps.add(timestamp);
	}
	
	public void addTimestampsIfNotExist(List<String> timestamps)throws Exception {
		if (this.availableTimestamps == null)
			this.availableTimestamps = new ArrayList<>();

		for (String timestamp : timestamps) {
			/*
			 * add timestamp if not exists
			 */
			if (!timestampAlreadyInTimestampReferences(timestamp, this.availableTimestamps))
				this.availableTimestamps.add(timestamp);
		}
	}

	private boolean timestampAlreadyInTimestampReferences(String timestamp, List<String> availableTimestamps) {
		if (availableTimestamps.contains(timestamp))
			return true;
		// if code reaches this line, then the topic is not within the list
		return false;
	}
	
	public void removeTimestampIfExists(String timestamp)throws Exception {
		if (this.availableTimestamps == null)
			this.availableTimestamps = new ArrayList<>();
		
		if (this.availableTimestamps.contains(timestamp)){
			this.availableTimestamps.remove(timestamp);
		}
	}


}
