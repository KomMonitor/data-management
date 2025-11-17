package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.hibernate.annotations.UuidGenerator;

@Entity(name = "IndicatorReference")
public class IndicatorReferenceEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
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
