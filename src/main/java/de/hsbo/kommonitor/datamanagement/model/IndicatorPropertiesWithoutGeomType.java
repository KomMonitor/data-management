package de.hsbo.kommonitor.datamanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Objects;

/**
 * IndicatorPropertiesWithoutGeomType
 */
public class IndicatorPropertiesWithoutGeomType extends HashMap<String, String>  {
  
	/*
	 * README
	 * 
	 * the properties of this type will be set directly. 
	 * BUT, MORE IMPORTANTLY, the properties will also be set within the HashMap as Key-Value Pairs!!!
	 * ONLY THE KEY-VALUE-PAIRS WITHIN HASHMAP WILL BE INCLUDED IN JSON RESPONSE!!
	 * 
	 * So do not try to moduiy their value by setting the property alone, instead modify HasMap Entry as well
	 */

  private String id = null;

  private String name = null;

  private String validStartDate = null;

  private String validEndDate = null;

  public IndicatorPropertiesWithoutGeomType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * the id of the spatial feature
   * @return id
  **/
  @NotNull
  @Schema(name = "ID", description = "the id of the spatial feature", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ID")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IndicatorPropertiesWithoutGeomType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * the name of the spatial feature
   * @return name
  **/
  @NotNull
  @Schema(name = "NAME", description = "the name of the spatial feature", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IndicatorPropertiesWithoutGeomType validStartDate(String validStartDate) {
    this.validStartDate = validStartDate;
    return this;
  }

   /**
   * the start date from which on the spatial feature is valid
   * @return validStartDate
  **/
  @NotNull
  @Schema(name = "validStartDate", description = "the start date from which on the spatial feature is valid", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validStartDate")
  public String getValidStartDate() {
    return validStartDate;
  }

  public void setValidStartDate(String validStartDate) {
    this.validStartDate = validStartDate;
  }

  public IndicatorPropertiesWithoutGeomType validEndDate(String validEndDate) {
    this.validEndDate = validEndDate;
    return this;
  }

   /**
   * the end date until the spatial feature is valid - or null if not set
   * @return validEndDate
  **/
  @NotNull
  @Schema(name = "validEndDate", description = "the end date until the spatial feature is valid - or null if not set", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validEndDate")
  public String getValidEndDate() {
    return validEndDate;
  }

  public void setValidEndDate(String validEndDate) {
    this.validEndDate = validEndDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPropertiesWithoutGeomType indicatorPropertiesWithoutGeomType = (IndicatorPropertiesWithoutGeomType) o;
    return Objects.equals(this.id, indicatorPropertiesWithoutGeomType.id) &&
        Objects.equals(this.name, indicatorPropertiesWithoutGeomType.name) &&
        Objects.equals(this.validStartDate, indicatorPropertiesWithoutGeomType.validStartDate) &&
        Objects.equals(this.validEndDate, indicatorPropertiesWithoutGeomType.validEndDate) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, validStartDate, validEndDate, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPropertiesWithoutGeomType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    validStartDate: ").append(toIndentedString(validStartDate)).append("\n");
    sb.append("    validEndDate: ").append(toIndentedString(validEndDate)).append("\n");
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

