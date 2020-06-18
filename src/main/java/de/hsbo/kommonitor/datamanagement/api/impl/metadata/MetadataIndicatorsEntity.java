package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.*;

import de.hsbo.kommonitor.datamanagement.model.indicators.CreationTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.indicators.DefaultClassificationMappingItemType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

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
	
	private String characteristicValue = null;
	
	private String topicReference = null;
	
	@ElementCollection
    @CollectionTable(name = "indicator_timestamps", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
    @Column(name = "timestamp")
    private Collection<String> availableTimestamps;
	
	@ElementCollection
    @CollectionTable(name = "indicator_tags", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
    @Column(name = "tag")
    private Collection<String> tags;
	
	private String colorBrewerSchemeName;
	
	/*
	 * references to other indicators are mapped by hand
	 * within the entity "IndicatorReferenceEntity"
	 */
	
	/*
	 * references to other georesources are mapped by hand
	 * within the entity "GeoresourceReferenceEntity"
	 */
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "metadataIndicators_defaultClassificationMapping", 
	joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), 
	inverseJoinColumns = @JoinColumn(name = "mapping_id", referencedColumnName = "mappingid"))
	private Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems;
	
	public HashSet<DefaultClassificationMappingItemType> getDefaultClassificationMappingItems() {
		return new HashSet<DefaultClassificationMappingItemType>(defaultClassificationMappingItems);
	}

	public void setDefaultClassificationMappingItems(
			Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems) {
		
//		List<DefaultClassificationMappingItemType> list = new ArrayList<>(defaultClassificationMappingItems);
//		
//		Collections.sort(list);
		
		this.defaultClassificationMappingItems = new HashSet<DefaultClassificationMappingItemType>(defaultClassificationMappingItems);
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

	public HashSet<String> getTags() {
		return new HashSet<String>(tags);
	}

	public void setTags(HashSet<String> tags) {
		this.tags = tags;
	}

	public HashSet<String> getAvailableTimestamps() {
		return new HashSet<String>(availableTimestamps);
	}

	public void setAvailableTimestamps(HashSet<String> availableTimestamps) {
		this.availableTimestamps = availableTimestamps;
	}
	
	public void addTimestampIfNotExist(String timestamp)throws Exception {
		if (this.availableTimestamps == null)
			this.availableTimestamps = new HashSet<String>();

		if (!timestampAlreadyInTimestampReferences(timestamp, this.availableTimestamps))
			this.availableTimestamps.add(timestamp);
		
		this.availableTimestamps = new HashSet<String>(this.availableTimestamps);
	}
	
	public void addTimestampsIfNotExist(Collection<String> timestamps)throws Exception {
		if (this.availableTimestamps == null)
			this.availableTimestamps = new HashSet<>();

		for (String timestamp : timestamps) {
			/*
			 * add timestamp if not exists
			 */
			if (!timestampAlreadyInTimestampReferences(timestamp, this.availableTimestamps))
				this.availableTimestamps.add(timestamp);
		}
		
		this.availableTimestamps = new HashSet<String>(this.availableTimestamps);
	}

	private boolean timestampAlreadyInTimestampReferences(String timestamp, Collection<String> availableTimestamps) {
		if (availableTimestamps.contains(timestamp))
			return true;
		// if code reaches this line, then the topic is not within the list
		return false;
	}
	
	public void removeTimestampIfExists(String timestamp)throws Exception {
		if (this.availableTimestamps == null)
			this.availableTimestamps = new HashSet<>();
		
		while (this.availableTimestamps.contains(timestamp)){
			this.availableTimestamps.remove(timestamp);
		}
		
		this.availableTimestamps = new HashSet<String>(this.availableTimestamps);
	}

	public String getCharacteristicValue() {
		return characteristicValue;
	}

	public void setCharacteristicValue(String characteristicValue) {
		this.characteristicValue = characteristicValue;
	}

	public String getTopicReference() {
		return topicReference;
	}

	public void setTopicReference(String topicReference) {
		this.topicReference = topicReference;
	}

    @ManyToMany()
    @JoinTable(name = "metadataIndicators_roles",
            joinColumns = @JoinColumn(name = "metadataindicators_id", referencedColumnName = "datasetid"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "roleid"))
    private Collection<RolesEntity> roles;

    public HashSet<RolesEntity> getRoles() {
        return new HashSet<RolesEntity>(roles);
    }

    public void setRoles(Collection<RolesEntity> roles) {
        this.roles = new HashSet<RolesEntity>(roles);
    }

}
