package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.IndicatorPOSTInputTypeIndicatorValues;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * IndicatorPUTInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-20T01:40:05.349474681+01:00[Europe/Berlin]")
public class IndicatorPUTInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> permissions = new ArrayList<>();

  private String applicableSpatialUnit;

  private DefaultClassificationMappingType defaultClassificationMapping;

  @Valid
  private List<@Valid IndicatorPOSTInputTypeIndicatorValues> indicatorValues = new ArrayList<>();

  private String ownerId;

  public IndicatorPUTInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorPUTInputType(List<String> permissions, String applicableSpatialUnit, List<@Valid IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
    this.permissions = permissions;
    this.applicableSpatialUnit = applicableSpatialUnit;
    this.indicatorValues = indicatorValues;
  }

  public IndicatorPUTInputType permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public IndicatorPUTInputType addPermissionsItem(String permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return permissions
  */
  @NotNull 
  @Schema(name = "permissions", description = "list of role identifiers that have read access rights for this dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public IndicatorPUTInputType applicableSpatialUnit(String applicableSpatialUnit) {
    this.applicableSpatialUnit = applicableSpatialUnit;
    return this;
  }

  /**
   * Get applicableSpatialUnit
   * @return applicableSpatialUnit
  */
  @NotNull 
  @Schema(name = "applicableSpatialUnit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("applicableSpatialUnit")
  public String getApplicableSpatialUnit() {
    return applicableSpatialUnit;
  }

  public void setApplicableSpatialUnit(String applicableSpatialUnit) {
    this.applicableSpatialUnit = applicableSpatialUnit;
  }

  public IndicatorPUTInputType defaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
    return this;
  }

  /**
   * Get defaultClassificationMapping
   * @return defaultClassificationMapping
  */
  @Valid 
  @Schema(name = "defaultClassificationMapping", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultClassificationMapping")
  public DefaultClassificationMappingType getDefaultClassificationMapping() {
    return defaultClassificationMapping;
  }

  public void setDefaultClassificationMapping(DefaultClassificationMappingType defaultClassificationMapping) {
    this.defaultClassificationMapping = defaultClassificationMapping;
  }

  public IndicatorPUTInputType indicatorValues(List<@Valid IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
    this.indicatorValues = indicatorValues;
    return this;
  }

  public IndicatorPUTInputType addIndicatorValuesItem(IndicatorPOSTInputTypeIndicatorValues indicatorValuesItem) {
    if (this.indicatorValues == null) {
      this.indicatorValues = new ArrayList<>();
    }
    this.indicatorValues.add(indicatorValuesItem);
    return this;
  }

  /**
   * an array of entries containing indicator values and mapping to spatial features via identifiers
   * @return indicatorValues
  */
  @NotNull @Valid 
  @Schema(name = "indicatorValues", description = "an array of entries containing indicator values and mapping to spatial features via identifiers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorValues")
  public List<@Valid IndicatorPOSTInputTypeIndicatorValues> getIndicatorValues() {
    return indicatorValues;
  }

  public void setIndicatorValues(List<@Valid IndicatorPOSTInputTypeIndicatorValues> indicatorValues) {
    this.indicatorValues = indicatorValues;
  }

  public IndicatorPUTInputType ownerId(String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * identifier of the owning group
   * @return ownerId
  */

  @Schema(name = "ownerId", description = "identifier of the owning group", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPUTInputType indicatorPUTInputType = (IndicatorPUTInputType) o;
    return Objects.equals(this.permissions, indicatorPUTInputType.permissions) &&
        Objects.equals(this.applicableSpatialUnit, indicatorPUTInputType.applicableSpatialUnit) &&
        Objects.equals(this.defaultClassificationMapping, indicatorPUTInputType.defaultClassificationMapping) &&
        Objects.equals(this.indicatorValues, indicatorPUTInputType.indicatorValues) &&
        Objects.equals(this.ownerId, indicatorPUTInputType.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions, applicableSpatialUnit, defaultClassificationMapping, indicatorValues, ownerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPUTInputType {\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    applicableSpatialUnit: ").append(toIndentedString(applicableSpatialUnit)).append("\n");
    sb.append("    defaultClassificationMapping: ").append(toIndentedString(defaultClassificationMapping)).append("\n");
    sb.append("    indicatorValues: ").append(toIndentedString(indicatorValues)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

