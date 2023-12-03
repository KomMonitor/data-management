package de.hsbo.kommonitor.datamanagement.model.spatialunits;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import io.swagger.annotations.ApiModelProperty;

/**
 * SpatialUnitPATCHInputType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-12-03T20:40:35.672Z")

public class SpatialUnitPATCHInputType {
	@JsonProperty("datasetName")
	private String datasetName = null;

	@JsonProperty("allowedRoles")

	private List<String> allowedRoles = new ArrayList<String>();

	@JsonProperty("metadata")
	private CommonMetadataType metadata = null;

	@JsonProperty("nextLowerHierarchyLevel")
	private String nextLowerHierarchyLevel = null;

	@JsonProperty("nextUpperHierarchyLevel")
	private String nextUpperHierarchyLevel = null;

	@JsonProperty("isOutlineLayer")
	private Boolean isOutlineLayer = false;

	@JsonProperty("outlineColor")
	private String outlineColor = null;

	@JsonProperty("outlineWidth")
	private BigDecimal outlineWidth = null;

	@JsonProperty("outlineDashArrayString")
	private String outlineDashArrayString = null;

	public SpatialUnitPATCHInputType datasetName(String datasetName) {
		this.datasetName = datasetName;
		return this;
	}

	/**
	 * the name of the spatial unit - its \"spatialUnitLevel\"
	 * 
	 * @return datasetName
	 **/
	@ApiModelProperty(required = true, value = "the name of the spatial unit - its \"spatialUnitLevel\"")

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public SpatialUnitPATCHInputType allowedRoles(List<String> allowedRoles) {
		this.allowedRoles = allowedRoles;
		return this;
	}

	public SpatialUnitPATCHInputType addAllowedRolesItem(String allowedRolesItem) {
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

	public SpatialUnitPATCHInputType metadata(CommonMetadataType metadata) {
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

	public SpatialUnitPATCHInputType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
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

	public SpatialUnitPATCHInputType nextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
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

	public SpatialUnitPATCHInputType isOutlineLayer(Boolean isOutlineLayer) {
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

	public SpatialUnitPATCHInputType outlineColor(String outlineColor) {
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

	public SpatialUnitPATCHInputType outlineWidth(BigDecimal outlineWidth) {
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

	public SpatialUnitPATCHInputType outlineDashArrayString(String outlineDashArrayString) {
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
		SpatialUnitPATCHInputType spatialUnitPATCHInputType = (SpatialUnitPATCHInputType) o;
		return Objects.equals(this.datasetName, spatialUnitPATCHInputType.datasetName)
				&& Objects.equals(this.allowedRoles, spatialUnitPATCHInputType.allowedRoles)
				&& Objects.equals(this.metadata, spatialUnitPATCHInputType.metadata)
				&& Objects.equals(this.nextLowerHierarchyLevel, spatialUnitPATCHInputType.nextLowerHierarchyLevel)
				&& Objects.equals(this.nextUpperHierarchyLevel, spatialUnitPATCHInputType.nextUpperHierarchyLevel)
				&& Objects.equals(this.isOutlineLayer, spatialUnitPATCHInputType.isOutlineLayer)
				&& Objects.equals(this.outlineColor, spatialUnitPATCHInputType.outlineColor)
				&& Objects.equals(this.outlineWidth, spatialUnitPATCHInputType.outlineWidth)
				&& Objects.equals(this.outlineDashArrayString, spatialUnitPATCHInputType.outlineDashArrayString);
	}

	@Override
	public int hashCode() {
		return Objects.hash(datasetName, allowedRoles, metadata, nextLowerHierarchyLevel, nextUpperHierarchyLevel,
				isOutlineLayer, outlineColor, outlineWidth, outlineDashArrayString);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SpatialUnitPATCHInputType {\n");

		sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
		sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
		sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
		sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
		sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
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
