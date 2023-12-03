package de.hsbo.kommonitor.datamanagement.model.spatialunits;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.roles.PermissionLevelType;
import io.swagger.annotations.ApiModelProperty;

/**
 * SpatialUnitOverviewType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-12-03T20:40:35.672Z")

public class SpatialUnitOverviewType {
	@JsonProperty("allowedRoles")

	private List<String> allowedRoles = new ArrayList<String>();

	@JsonProperty("availablePeriodsOfValidity")

	private List<PeriodOfValidityType> availablePeriodsOfValidity = null;

	@JsonProperty("metadata")
	private CommonMetadataType metadata = null;

	@JsonProperty("nextLowerHierarchyLevel")
	private String nextLowerHierarchyLevel = null;

	@JsonProperty("nextUpperHierarchyLevel")
	private String nextUpperHierarchyLevel = null;

	@JsonProperty("spatialUnitId")
	private String spatialUnitId = null;

	@JsonProperty("spatialUnitLevel")
	private String spatialUnitLevel = null;

	@JsonProperty("wfsUrl")
	private String wfsUrl = null;

	@JsonProperty("wmsUrl")
	private String wmsUrl = null;

	@JsonProperty("userPermissions")

	private List<PermissionLevelType> userPermissions = null;

	@JsonProperty("isOutlineLayer")
	private Boolean isOutlineLayer = null;

	@JsonProperty("outlineColor")
	private String outlineColor = null;

	@JsonProperty("outlineWidth")
	private BigDecimal outlineWidth = null;

	@JsonProperty("outlineDashArrayString")
	private String outlineDashArrayString = null;

	public SpatialUnitOverviewType allowedRoles(List<String> allowedRoles) {
		this.allowedRoles = allowedRoles;
		return this;
	}

	public SpatialUnitOverviewType addAllowedRolesItem(String allowedRolesItem) {
		this.allowedRoles.add(allowedRolesItem);
		return this;
	}

	/**
	 * list of role identifiers that have read access rights for this dataset
	 * 
	 * @return allowedRoles
	 **/
	@ApiModelProperty(required = true, value = "list of role identifiers that have read access rights for this dataset")

	public List<String> getAllowedRoles() {
		return allowedRoles;
	}

	public void setAllowedRoles(List<String> allowedRoles) {
		this.allowedRoles = allowedRoles;
	}

	public SpatialUnitOverviewType availablePeriodsOfValidity(List<PeriodOfValidityType> availablePeriodsOfValidity) {
		this.availablePeriodsOfValidity = availablePeriodsOfValidity;
		return this;
	}

	public SpatialUnitOverviewType addAvailablePeriodsOfValidityItem(
			PeriodOfValidityType availablePeriodsOfValidityItem) {
		if (this.availablePeriodsOfValidity == null) {
			this.availablePeriodsOfValidity = new ArrayList<PeriodOfValidityType>();
		}
		this.availablePeriodsOfValidity.add(availablePeriodsOfValidityItem);
		return this;
	}

	/**
	 * Get availablePeriodsOfValidity
	 * 
	 * @return availablePeriodsOfValidity
	 **/
	@ApiModelProperty(value = "")

	public List<PeriodOfValidityType> getAvailablePeriodsOfValidity() {
		return availablePeriodsOfValidity;
	}

	public void setAvailablePeriodsOfValidity(List<PeriodOfValidityType> availablePeriodsOfValidity) {
		this.availablePeriodsOfValidity = availablePeriodsOfValidity;
	}

	public SpatialUnitOverviewType metadata(CommonMetadataType metadata) {
		this.metadata = metadata;
		return this;
	}

	/**
	 * Get metadata
	 * 
	 * @return metadata
	 **/
	@ApiModelProperty(required = true, value = "")

	public CommonMetadataType getMetadata() {
		return metadata;
	}

	public void setMetadata(CommonMetadataType metadata) {
		this.metadata = metadata;
	}

	public SpatialUnitOverviewType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
		this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
		return this;
	}

	/**
	 * the identifier/name of the spatial unit level that contains the features of
	 * the nearest lower hierarchy level
	 * 
	 * @return nextLowerHierarchyLevel
	 **/
	@ApiModelProperty(required = true, value = "the identifier/name of the spatial unit level that contains the features of the nearest lower hierarchy level")

	public String getNextLowerHierarchyLevel() {
		return nextLowerHierarchyLevel;
	}

	public void setNextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
		this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
	}

	public SpatialUnitOverviewType nextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
		this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
		return this;
	}

	/**
	 * the identifier/name of the spatial unit level that contains the features of
	 * the nearest upper hierarchy level
	 * 
	 * @return nextUpperHierarchyLevel
	 **/
	@ApiModelProperty(required = true, value = "the identifier/name of the spatial unit level that contains the features of the nearest upper hierarchy level")

	public String getNextUpperHierarchyLevel() {
		return nextUpperHierarchyLevel;
	}

	public void setNextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
		this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
	}

	public SpatialUnitOverviewType spatialUnitId(String spatialUnitId) {
		this.spatialUnitId = spatialUnitId;
		return this;
	}

	/**
	 * the unique identifier of the spatial unit level the features apply to
	 * 
	 * @return spatialUnitId
	 **/
	@ApiModelProperty(required = true, value = "the unique identifier of the spatial unit level the features apply to")

	public String getSpatialUnitId() {
		return spatialUnitId;
	}

	public void setSpatialUnitId(String spatialUnitId) {
		this.spatialUnitId = spatialUnitId;
	}

	public SpatialUnitOverviewType spatialUnitLevel(String spatialUnitLevel) {
		this.spatialUnitLevel = spatialUnitLevel;
		return this;
	}

	/**
	 * the name of the spatial unit level the features apply to
	 * 
	 * @return spatialUnitLevel
	 **/
	@ApiModelProperty(required = true, value = "the name of the spatial unit level the features apply to")

	public String getSpatialUnitLevel() {
		return spatialUnitLevel;
	}

	public void setSpatialUnitLevel(String spatialUnitLevel) {
		this.spatialUnitLevel = spatialUnitLevel;
	}

	public SpatialUnitOverviewType wfsUrl(String wfsUrl) {
		this.wfsUrl = wfsUrl;
		return this;
	}

	/**
	 * the URL of a running WFS instance serving the spatial features of the
	 * associated dataset
	 * 
	 * @return wfsUrl
	 **/
	@ApiModelProperty(required = true, value = "the URL of a running WFS instance serving the spatial features of the associated dataset")

	public String getWfsUrl() {
		return wfsUrl;
	}

	public void setWfsUrl(String wfsUrl) {
		this.wfsUrl = wfsUrl;
	}

	public SpatialUnitOverviewType wmsUrl(String wmsUrl) {
		this.wmsUrl = wmsUrl;
		return this;
	}

	/**
	 * the URL of a running WMS instance serving the spatial features of the
	 * associated dataset
	 * 
	 * @return wmsUrl
	 **/
	@ApiModelProperty(required = true, value = "the URL of a running WMS instance serving the spatial features of the associated dataset")

	public String getWmsUrl() {
		return wmsUrl;
	}

	public void setWmsUrl(String wmsUrl) {
		this.wmsUrl = wmsUrl;
	}

	public SpatialUnitOverviewType userPermissions(List<PermissionLevelType> userPermissions) {
		this.userPermissions = userPermissions;
		return this;
	}

	public SpatialUnitOverviewType addUserPermissionsItem(PermissionLevelType userPermissionsItem) {
		if (this.userPermissions == null) {
			this.userPermissions = new ArrayList<PermissionLevelType>();
		}
		this.userPermissions.add(userPermissionsItem);
		return this;
	}

	/**
	 * List of permissions that are effective on this dataset for the current user
	 * 
	 * @return userPermissions
	 **/
	@ApiModelProperty(value = "List of permissions that are effective on this dataset for the current user")

	public List<PermissionLevelType> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<PermissionLevelType> list) {
		this.userPermissions = list;
	}

	public SpatialUnitOverviewType isOutlineLayer(Boolean isOutlineLayer) {
		this.isOutlineLayer = isOutlineLayer;
		return this;
	}

	/**
	 * if true, then KomMonitor web client map application will offer this spatial
	 * unit as outline layer in legend control
	 * 
	 * @return isOutlineLayer
	 **/
	@ApiModelProperty(value = "if true, then KomMonitor web client map application will offer this spatial unit as outline layer in legend control")

	public Boolean isIsOutlineLayer() {
		return isOutlineLayer;
	}

	public void setIsOutlineLayer(Boolean isOutlineLayer) {
		this.isOutlineLayer = isOutlineLayer;
	}

	public SpatialUnitOverviewType outlineColor(String outlineColor) {
		this.outlineColor = outlineColor;
		return this;
	}

	/**
	 * outline color for this layer as hex code
	 * 
	 * @return outlineColor
	 **/
	@ApiModelProperty(value = "outline color for this layer as hex code")

	public String getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(String outlineColor) {
		this.outlineColor = outlineColor;
	}

	public SpatialUnitOverviewType outlineWidth(BigDecimal outlineWidth) {
		this.outlineWidth = outlineWidth;
		return this;
	}

	/**
	 * outline width as stroke width for outline geometry
	 * 
	 * @return outlineWidth
	 **/
	@ApiModelProperty(value = "outline width as stroke width for outline geometry")

	public BigDecimal getOutlineWidth() {
		return outlineWidth;
	}

	public void setOutlineWidth(BigDecimal outlineWidth) {
		this.outlineWidth = outlineWidth;
	}

	public SpatialUnitOverviewType outlineDashArrayString(String outlineDashArrayString) {
		this.outlineDashArrayString = outlineDashArrayString;
		return this;
	}

	/**
	 * string of line stroke dash array for lines of interest (e.g. 20,20; see
	 * https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
	 * 
	 * @return outlineDashArrayString
	 **/
	@ApiModelProperty(value = "string of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)")

	public String getOutlineDashArrayString() {
		return outlineDashArrayString;
	}

	public void setOutlineDashArrayString(String outlineDashArrayString) {
		this.outlineDashArrayString = outlineDashArrayString;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SpatialUnitOverviewType spatialUnitOverviewType = (SpatialUnitOverviewType) o;
		return Objects.equals(this.allowedRoles, spatialUnitOverviewType.allowedRoles)
				&& Objects.equals(this.availablePeriodsOfValidity, spatialUnitOverviewType.availablePeriodsOfValidity)
				&& Objects.equals(this.metadata, spatialUnitOverviewType.metadata)
				&& Objects.equals(this.nextLowerHierarchyLevel, spatialUnitOverviewType.nextLowerHierarchyLevel)
				&& Objects.equals(this.nextUpperHierarchyLevel, spatialUnitOverviewType.nextUpperHierarchyLevel)
				&& Objects.equals(this.spatialUnitId, spatialUnitOverviewType.spatialUnitId)
				&& Objects.equals(this.spatialUnitLevel, spatialUnitOverviewType.spatialUnitLevel)
				&& Objects.equals(this.wfsUrl, spatialUnitOverviewType.wfsUrl)
				&& Objects.equals(this.wmsUrl, spatialUnitOverviewType.wmsUrl)
				&& Objects.equals(this.userPermissions, spatialUnitOverviewType.userPermissions)
				&& Objects.equals(this.isOutlineLayer, spatialUnitOverviewType.isOutlineLayer)
				&& Objects.equals(this.outlineColor, spatialUnitOverviewType.outlineColor)
				&& Objects.equals(this.outlineWidth, spatialUnitOverviewType.outlineWidth)
				&& Objects.equals(this.outlineDashArrayString, spatialUnitOverviewType.outlineDashArrayString);
	}

	@Override
	public int hashCode() {
		return Objects.hash(allowedRoles, availablePeriodsOfValidity, metadata, nextLowerHierarchyLevel,
				nextUpperHierarchyLevel, spatialUnitId, spatialUnitLevel, wfsUrl, wmsUrl, userPermissions,
				isOutlineLayer, outlineColor, outlineWidth, outlineDashArrayString);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SpatialUnitOverviewType {\n");

		sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
		sb.append("    availablePeriodsOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
		sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
		sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
		sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
		sb.append("    spatialUnitId: ").append(toIndentedString(spatialUnitId)).append("\n");
		sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
		sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
		sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
		sb.append("    userPermissions: ").append(toIndentedString(userPermissions)).append("\n");
		sb.append("    isOutlineLayer: ").append(toIndentedString(isOutlineLayer)).append("\n");
		sb.append("    outlineColor: ").append(toIndentedString(outlineColor)).append("\n");
		sb.append("    outlineWidth: ").append(toIndentedString(outlineWidth)).append("\n");
		sb.append("    outlineDashArrayString: ").append(toIndentedString(outlineDashArrayString)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
