package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import org.hibernate.annotations.GenericGenerator;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType.UpdateIntervalEnum;

@MappedSuperclass
public abstract class AbstractMetadata {

	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String datasetId = null;
	private String datasetName = null;
	private String dbTableName = null;
	@Column(columnDefinition="text")
	private String description = null;
	@Column(columnDefinition="text")
	private String dataSource = null;
	@Column(columnDefinition="text")
	private String dataBasis = null;
	@Column(columnDefinition="text")
	private String note = null;
	@Column(columnDefinition="text")
	private String literature = null;
	@Column(columnDefinition="text")
	private String contact = null;
	@Enumerated(EnumType.STRING)
	private UpdateIntervalEnum updateIntervall = null;
	@Column(columnDefinition="text")
	private String jsonSchema = null;
	private String wmsUrl = null;
	private String wfsUrl = null;
	
	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate = null;

	@Transient
	private List<PermissionLevelType> userPermissions;
	
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
	public String getDataBasis() {
		return dataBasis;
	}
	public void setDataBasis(String dataBasis) {
		this.dataBasis = dataBasis;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getLiterature() {
		return literature;
	}
	public void setLiterature(String literature) {
		this.literature = literature;
	}
	public List<PermissionLevelType> getUserPermissions() {
		return userPermissions;
	}
	public void setUserPermissions(List<PermissionLevelType> userPermissions) {
		this.userPermissions = userPermissions;
	}
}
