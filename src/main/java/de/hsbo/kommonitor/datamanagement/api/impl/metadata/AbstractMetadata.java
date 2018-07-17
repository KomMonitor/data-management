package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class AbstractMetadata {

	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String datasetId = null;
	private String datasetName = null;
	private String dbTableName = null;
	private String description = null;
	private String dataSource = null;
	private String contact = null;
	private UpdateIntervalEnum updateIntervall = null;
	private String jsonSchema = null;
	private String wmsUrl = null;
	private String wfsUrl = null;
	
	@Column(columnDefinition = "TIMESTAMP WITH THE ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate = null;
	
	
	
	
	
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getDatasetName() {
		return datasetName;
	}
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
	public String getDbTableName() {
		return dbTableName;
	}
	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public UpdateIntervalEnum getUpdateIntervall() {
		return updateIntervall;
	}
	public void setUpdateIntervall(UpdateIntervalEnum updateIntervall) {
		this.updateIntervall = updateIntervall;
	}
	public String getJsonSchema() {
		return jsonSchema;
	}
	public void setJsonSchema(String jsonSchema) {
		this.jsonSchema = jsonSchema;
	}
	public String getWmsUrl() {
		return wmsUrl;
	}
	public void setWmsUrl(String wmsUrl) {
		this.wmsUrl = wmsUrl;
	}
	public String getWfsUrl() {
		return wfsUrl;
	}
	public void setWfsUrl(String wfsUrl) {
		this.wfsUrl = wfsUrl;
	}
	public String getDatasetId() {
		return datasetId;
	}
	
	
	
	
}
