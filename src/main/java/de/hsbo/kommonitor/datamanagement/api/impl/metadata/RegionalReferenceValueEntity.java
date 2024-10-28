package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "RegionalReferenceValueType")
public class RegionalReferenceValueEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String mappingId = null;

	private String referenceDate = null;

	private Float regionalSum = null;

	private Float regionalAverage = null;

	private Float spatiallyUnassignable = null;

	public String getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
	}

	public Float getRegionalSum() {
		return regionalSum;
	}

	public void setRegionalSum(Float regionalSum) {
		this.regionalSum = regionalSum;
	}

	public Float getRegionalAverage() {
		return regionalAverage;
	}

	public void setRegionalAverage(Float regionalAverage) {
		this.regionalAverage = regionalAverage;
	}

	public Float getSpatiallyUnassignable() {
		return spatiallyUnassignable;
	}

	public void setSpatiallyUnassignable(Float spatiallyUnassignable) {
		this.spatiallyUnassignable = spatiallyUnassignable;
	}

	public String getMappingId() {
		return mappingId;
	}

}
