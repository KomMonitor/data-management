package de.hsbo.kommonitor.datamanagement.api.impl.metadata.references;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "GeoresourceReference")
public class GeoresourceReferenceEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String entryId = null;

	private String mainIndicatorId = null;
	private String referencedGeoresourceId = null;
	private String referenceDescription = null;

	public String getEntryId() {
		return entryId;
	}

	public String getMainIndicatorId() {
		return mainIndicatorId;
	}

	public void setMainIndicatorId(String indicatorId) {
		this.mainIndicatorId = indicatorId;
	}

	public String getReferencedGeoresourceId() {
		return referencedGeoresourceId;
	}

	public void setReferencedGeoresourceId(String referencedGeoresourceId) {
		this.referencedGeoresourceId = referencedGeoresourceId;
	}

	public String getReferenceDescription() {
		return referenceDescription;
	}

	public void setReferenceDescription(String referenceDescription) {
		this.referenceDescription = referenceDescription;
	}

}
