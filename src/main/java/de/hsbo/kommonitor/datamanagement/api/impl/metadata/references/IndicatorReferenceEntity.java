package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "IndicatorReference")
public class IndicatorReferenceEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String entryId = null;

	private String indicatorId = null;
	private String referencedIndicatorId = null;
	@Column(columnDefinition="text")
	private String referenceDescription = null;

	public String getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	public String getReferencedIndicatorId() {
		return referencedIndicatorId;
	}

	public void setReferencedIndicatorId(String referencedIndicatorId) {
		this.referencedIndicatorId = referencedIndicatorId;
	}

	public String getReferenceDescription() {
		return referenceDescription;
	}

	public void setReferenceDescription(String referenceDescription) {
		this.referenceDescription = referenceDescription;
	}

	public String getEntryId() {
		return entryId;
	}

}
