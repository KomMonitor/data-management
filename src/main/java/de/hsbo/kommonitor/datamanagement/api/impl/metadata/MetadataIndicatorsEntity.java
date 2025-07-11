package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.hsbo.kommonitor.datamanagement.api.impl.users.UserInfoEntity;
import de.hsbo.kommonitor.datamanagement.model.CreationTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingItemType;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingType.ClassificationMethodEnum;
import de.hsbo.kommonitor.datamanagement.model.IndicatorTypeEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity(name = "MetadataIndicators")
public class MetadataIndicatorsEntity extends AbstractMetadata implements RestrictedEntity {

	@Column(columnDefinition="text")
	private String processDescription = null;
	private String unit = null;
	private CreationTypeEnum creationType = null;
	private IndicatorTypeEnum indicatorType = null;
	private String lowestSpatialUnitForComputation = null;
	private String abbreviation = null;
	private boolean isHeadlineIndicator = false;
	private String interpretation = null;
	private String referenceDateNote = null;
	private int displayOrder = 0;
	
	private String characteristicValue = null;
	
	private String topicReference = null;

	private Integer precision;
	
	@ElementCollection
    @CollectionTable(name = "indicator_timestamps", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
    @Column(name = "timestamp")
    private Collection<String> availableTimestamps;
	
	@ElementCollection
    @CollectionTable(name = "indicator_tags", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"))
    @Column(name = "tag")
    private Collection<String> tags;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "metadataindicators_regionalreferencevalues",
	joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"),
	inverseJoinColumns = @JoinColumn(name = "mapping_id", referencedColumnName = "mappingid"))
    private Collection<RegionalReferenceValueEntity> regionalReferenceValues;

	private String colorBrewerSchemeName;
	
	@Column(columnDefinition = "integer default 5")
	private int numClasses;

	@Column(columnDefinition = "integer default 2")
	private ClassificationMethodEnum classificationMethod;

	/*
	 * references to other indicators are mapped by hand
	 * within the entity "IndicatorReferenceEntity"
	 */
	
	/*
	 * references to other georesources are mapped by hand
	 * within the entity "GeoresourceReferenceEntity"
	 */
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "metadataindicators_defaultclassification",
	joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), 
	inverseJoinColumns = @JoinColumn(name = "mapping_id", referencedColumnName = "mappingid"))
	private Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems;
	
	public HashSet<DefaultClassificationMappingItemType> getDefaultClassificationMappingItems() {
		return new HashSet<DefaultClassificationMappingItemType>(defaultClassificationMappingItems);
	}

	public void setDefaultClassificationMappingItems(
			Collection<DefaultClassificationMappingItemType> defaultClassificationMappingItems) {
		this.defaultClassificationMappingItems = new HashSet<DefaultClassificationMappingItemType>(defaultClassificationMappingItems);
	}

	@ManyToMany(mappedBy = "indicatorFavourites")
	private Set<UserInfoEntity> userFavorites = new HashSet<>();

	public Set<UserInfoEntity> getUserFavorites() {
		return userFavorites;
	}

	public void setUserFavorites(Set<UserInfoEntity> userFavorites) {
		this.userFavorites = userFavorites;
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
	
	public int getNumClasses() {
		return numClasses;
	}

	public void setNumClasses(int numClasses) {
		this.numClasses = numClasses;
	}

	public ClassificationMethodEnum getClassificationMethod() {
		return classificationMethod;
	}

	public void setClassificationMethod(ClassificationMethodEnum classificationMethod) {
		this.classificationMethod = classificationMethod;
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
    @JoinTable(name = "metadataIndicators_permissions",
            joinColumns = @JoinColumn(name = "metadataindicators_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Collection<PermissionEntity> permissions;

	@ManyToOne
	private OrganizationalUnitEntity owner;

	@Column
	@Nullable
	private Boolean isPublic;

	public HashSet<PermissionEntity> getPermissions() {
		return new HashSet<>(permissions);
	}

	public void setPermissions(Collection<PermissionEntity> permissions) {
		this.permissions = new HashSet<>(permissions);
	}

	public OrganizationalUnitEntity getOwner() {
		return owner;
	}

	public void setOwner(OrganizationalUnitEntity owner) {
		this.owner = owner;
	}

	@Override
	public Boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean aPublic) {
		isPublic = aPublic;
	}

	public String getReferenceDateNote() {
		return referenceDateNote;
	}

	public void setReferenceDateNote(String referenceDateNote) {
		this.referenceDateNote = referenceDateNote;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Collection<RegionalReferenceValueEntity> getRegionalReferenceValues() {
		return regionalReferenceValues;
	}

	public void setRegionalReferenceValues(Collection<RegionalReferenceValueEntity> regionalReferenceValues) {
		this.regionalReferenceValues = regionalReferenceValues;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
}
