package de.hsbo.kommonitor.datamanagement.model.spatialunits;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import io.swagger.annotations.ApiModelProperty;

/**
 * SpatialUnitPOSTInputType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2023-12-03T20:40:35.672Z")

public class SpatialUnitPOSTInputType {
	@JsonProperty("allowedRoles")

	private List<String> allowedRoles = new ArrayList<String>();

	@JsonProperty("geoJsonString")
	private String geoJsonString = null;

	@JsonProperty("jsonSchema")
	private String jsonSchema = null;

	@JsonProperty("metadata")
	private CommonMetadataType metadata = null;

	@JsonProperty("nextLowerHierarchyLevel")
	private String nextLowerHierarchyLevel = null;

	@JsonProperty("nextUpperHierarchyLevel")
	private String nextUpperHierarchyLevel = null;

	@JsonProperty("periodOfValidity")
	private PeriodOfValidityType periodOfValidity = null;

	@JsonProperty("spatialUnitLevel")
	private String spatialUnitLevel = null;

	@JsonProperty("isOutlineLayer")
	private Boolean isOutlineLayer = false;

	@JsonProperty("outlineColor")
	private String outlineColor = null;

	@JsonProperty("outlineWidth")
	private BigDecimal outlineWidth = null;

	@JsonProperty("outlineDashArrayString")
	private String outlineDashArrayString = null;

	public SpatialUnitPOSTInputType allowedRoles(List<String> allowedRoles) {
		this.allowedRoles = allowedRoles;
		return this;
	}

	public SpatialUnitPOSTInputType addAllowedRolesItem(String allowedRolesItem) {
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

	public SpatialUnitPOSTInputType geoJsonString(String geoJsonString) {
		this.geoJsonString = geoJsonString;
		return this;
	}

	/**
	 * a valid GeoJSON string containing the features consisting of a geometry and a
	 * unique identifier as property 'uuid'
	 * 
	 * @return geoJsonString
	 **/
	@ApiModelProperty(required = true, value = "a valid GeoJSON string containing the features consisting of a geometry and a unique identifier as property 'uuid'")

	public String getGeoJsonString() {
		return geoJsonString;
	}

	public void setGeoJsonString(String geoJsonString) {
		this.geoJsonString = geoJsonString;
	}

	public SpatialUnitPOSTInputType jsonSchema(String jsonSchema) {
		this.jsonSchema = jsonSchema;
		return this;
	}

	/**
	 * a JSON schema as string that defines the data model for this dataset. It can
	 * be used to validate the geoJsonString property.
	 * 
	 * @return jsonSchema
	 **/
	@ApiModelProperty(required = true, value = "a JSON schema as string that defines the data model for this dataset. It can be used to validate the geoJsonString property.")

	public String getJsonSchema() {
		return jsonSchema;
	}

	public void setJsonSchema(String jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

	public SpatialUnitPOSTInputType metadata(CommonMetadataType metadata) {
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

	public SpatialUnitPOSTInputType nextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
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

	public SpatialUnitPOSTInputType nextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
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

	public SpatialUnitPOSTInputType periodOfValidity(PeriodOfValidityType periodOfValidity) {
		this.periodOfValidity = periodOfValidity;
		return this;
	}

	/**
	 * Get periodOfValidity
	 * 
	 * @return periodOfValidity
	 **/
	@ApiModelProperty(required = true, value = "")

	public PeriodOfValidityType getPeriodOfValidity() {
		return periodOfValidity;
	}

	public void setPeriodOfValidity(PeriodOfValidityType periodOfValidity) {
		this.periodOfValidity = periodOfValidity;
	}

	public SpatialUnitPOSTInputType spatialUnitLevel(String spatialUnitLevel) {
		this.spatialUnitLevel = spatialUnitLevel;
		return this;
	}

	/**
	 * the name and identifier of the spatial unit level the features apply to
	 * 
	 * @return spatialUnitLevel
	 **/
	@ApiModelProperty(required = true, value = "the name and identifier of the spatial unit level the features apply to")

	public String getSpatialUnitLevel() {
		return spatialUnitLevel;
	}

	public void setSpatialUnitLevel(String spatialUnitLevel) {
		this.spatialUnitLevel = spatialUnitLevel;
	}

	public SpatialUnitPOSTInputType isOutlineLayer(Boolean isOutlineLayer) {
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

	public SpatialUnitPOSTInputType outlineColor(String outlineColor) {
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

	public SpatialUnitPOSTInputType outlineWidth(BigDecimal outlineWidth) {
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

	public SpatialUnitPOSTInputType outlineDashArrayString(String outlineDashArrayString) {
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
		SpatialUnitPOSTInputType spatialUnitPOSTInputType = (SpatialUnitPOSTInputType) o;
		return Objects.equals(this.allowedRoles, spatialUnitPOSTInputType.allowedRoles)
				&& Objects.equals(this.geoJsonString, spatialUnitPOSTInputType.geoJsonString)
				&& Objects.equals(this.jsonSchema, spatialUnitPOSTInputType.jsonSchema)
				&& Objects.equals(this.metadata, spatialUnitPOSTInputType.metadata)
				&& Objects.equals(this.nextLowerHierarchyLevel, spatialUnitPOSTInputType.nextLowerHierarchyLevel)
				&& Objects.equals(this.nextUpperHierarchyLevel, spatialUnitPOSTInputType.nextUpperHierarchyLevel)
				&& Objects.equals(this.periodOfValidity, spatialUnitPOSTInputType.periodOfValidity)
				&& Objects.equals(this.spatialUnitLevel, spatialUnitPOSTInputType.spatialUnitLevel)
				&& Objects.equals(this.isOutlineLayer, spatialUnitPOSTInputType.isOutlineLayer)
				&& Objects.equals(this.outlineColor, spatialUnitPOSTInputType.outlineColor)
				&& Objects.equals(this.outlineWidth, spatialUnitPOSTInputType.outlineWidth)
				&& Objects.equals(this.outlineDashArrayString, spatialUnitPOSTInputType.outlineDashArrayString);
	}

	@Override
	public int hashCode() {
		return Objects.hash(allowedRoles, geoJsonString, jsonSchema, metadata, nextLowerHierarchyLevel,
				nextUpperHierarchyLevel, periodOfValidity, spatialUnitLevel, isOutlineLayer, outlineColor, outlineWidth,
				outlineDashArrayString);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SpatialUnitPOSTInputType {\n");

		sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
		sb.append("    geoJsonString: ").append(toIndentedString(geoJsonString)).append("\n");
		sb.append("    jsonSchema: ").append(toIndentedString(jsonSchema)).append("\n");
		sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
		sb.append("    nextLowerHierarchyLevel: ").append(toIndentedString(nextLowerHierarchyLevel)).append("\n");
		sb.append("    nextUpperHierarchyLevel: ").append(toIndentedString(nextUpperHierarchyLevel)).append("\n");
		sb.append("    periodOfValidity: ").append(toIndentedString(periodOfValidity)).append("\n");
		sb.append("    spatialUnitLevel: ").append(toIndentedString(spatialUnitLevel)).append("\n");
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
