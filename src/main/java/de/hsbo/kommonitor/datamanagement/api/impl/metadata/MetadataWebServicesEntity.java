package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import de.hsbo.kommonitor.datamanagement.api.impl.RestrictedEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.OrganizationalUnitEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.accesscontrol.PermissionEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.users.UserInfoEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.webservice.ConnectionDetailsEntity;
import de.hsbo.kommonitor.datamanagement.model.PermissionLevelType;
import de.hsbo.kommonitor.datamanagement.model.ServiceResourceEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "metadataservices")
public class MetadataWebServicesEntity implements RestrictedEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
	private String id = null;

	private String title = null;

	@Column(columnDefinition="text")
	private String description = null;

	private String topicReference;

	private Boolean isPublic;

	@Column(columnDefinition="text")
	private String dataBasis = null;

	@Column(columnDefinition="text")
	private String dataSource = null;

	@Column(columnDefinition="text")
	private String contact = null;

	@Column(columnDefinition="text")
	private String note = null;

	private ServiceResourceEnum serviceResource = null;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "connectiondetailsid", referencedColumnName = "id")
	private ConnectionDetailsEntity connectionDetails;

	@ManyToMany()
	@JoinTable(name = "metadataservices_permissions",
			joinColumns = @JoinColumn(name = "metadataservices_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Collection<PermissionEntity> permissions;

	@ManyToOne
	private OrganizationalUnitEntity owner;

	@Transient
	private List<PermissionLevelType> userPermissions;

	@ManyToMany(mappedBy = "webServiceFavourites")
	private Set<UserInfoEntity> userFavorites = new HashSet<>();

	public Set<UserInfoEntity> getUserFavorites() {
		return userFavorites;
	}

	public void setUserFavorites(Set<UserInfoEntity> userFavorites) {
		this.userFavorites = userFavorites;
	}

	public MetadataWebServicesEntity() {
	}

	public String getId() {
		return id;
	}

	@Override
	public Boolean isPublic() {
		return isPublic;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTopicReference() {
		return topicReference;
	}

	public void setTopicReference(String topicReference) {
		this.topicReference = topicReference;
	}

	public Boolean getPublic() {
		return isPublic;
	}

	public void setPublic(Boolean aPublic) {
		isPublic = aPublic;
	}

	public String getDataBasis() {
		return dataBasis;
	}

	public void setDataBasis(String dataBasis) {
		this.dataBasis = dataBasis;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ServiceResourceEnum getServiceResource() {
		return serviceResource;
	}

	public void setServiceResource(ServiceResourceEnum serviceResource) {
		this.serviceResource = serviceResource;
	}

	public ConnectionDetailsEntity getConnectionDetails() {
		return connectionDetails;
	}

	public void setConnectionDetails(ConnectionDetailsEntity connectionDetails) {
		this.connectionDetails = connectionDetails;
	}

	public List<PermissionLevelType> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<PermissionLevelType> userPermissions) {
		this.userPermissions = userPermissions;
	}
}
