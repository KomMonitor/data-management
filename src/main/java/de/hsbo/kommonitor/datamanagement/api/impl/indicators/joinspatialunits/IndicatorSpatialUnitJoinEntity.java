package de.hsbo.kommonitor.datamanagement.api.impl.indicators.joinspatialunits;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "IndicatorSpatialUnits")
public class IndicatorSpatialUnitJoinEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String entryId;

	private String indicatorMetadataId = null;
	private String indicatorName = null;
	private String spatialUnitId = null;
	private String spatialUnitName = null;
	private String indicatorValueTableName = null;
	private String featureViewTableName = null;

	public IndicatorSpatialUnitJoinEntity() {
	};

	public String getIndicatorMetadataId() {
		return indicatorMetadataId;
	}

	public void setIndicatorMetadataId(String indicatorMetadataId) {
		this.indicatorMetadataId = indicatorMetadataId;
	}

	public String getSpatialUnitId() {
		return spatialUnitId;
	}

	public void setSpatialUnitId(String spatialUnitId) {
		this.spatialUnitId = spatialUnitId;
	}

	public String getFeatureViewTableName() {
		return featureViewTableName;
	}

	public void setFeatureViewTableName(String featureViewTableName) {
		this.featureViewTableName = featureViewTableName;
	}

	public String getEntryId() {
		return entryId;
	}

	public String getIndicatorValueTableName() {
		return indicatorValueTableName;
	}

	public void setIndicatorValueTableName(String indicatorValueTableName) {
		this.indicatorValueTableName = indicatorValueTableName;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public String getSpatialUnitName() {
		return spatialUnitName;
	}

	public void setSpatialUnitName(String spatialUnitName) {
		this.spatialUnitName = spatialUnitName;
	}

}
