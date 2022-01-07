package de.hsbo.kommonitor.datamanagement.api.impl.database;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "LastModification")
public class LastModificationEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String id = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date topics = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date spatialUnits = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date indicators = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date georesources = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date processScripts = null;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date accessControl = null;

	public String getId() {
		return id;
	}

	public Date getTopics() {
		return topics;
	}

	public void setTopics(Date topics) {
		this.topics = topics;
	}

	public Date getSpatialUnits() {
		return spatialUnits;
	}

	public void setSpatialUnits(Date spatialUnits) {
		this.spatialUnits = spatialUnits;
	}

	public Date getIndicators() {
		return indicators;
	}

	public void setIndicators(Date indicators) {
		this.indicators = indicators;
	}

	public Date getGeoresources() {
		return georesources;
	}

	public void setGeoresources(Date georesources) {
		this.georesources = georesources;
	}

	public Date getProcessScripts() {
		return processScripts;
	}

	public void setProcessScripts(Date processScripts) {
		this.processScripts = processScripts;
	}

	public Date getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(Date roles) {
		this.accessControl = roles;
	}

	/*
	 * default constructor is required by hibernate / jpa
	 */
	public LastModificationEntity() {

	}

	public LastModificationEntity(Date now) {
		super();
		this.georesources = now;
		this.indicators = now;
		this.processScripts = now;
		this.accessControl = now;
		this.spatialUnits = now;
		this.topics = now;
	}

}
