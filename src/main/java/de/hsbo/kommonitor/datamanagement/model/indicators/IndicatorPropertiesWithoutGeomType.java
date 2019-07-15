package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.HashMap;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IndicatorPropertiesWithoutGeomType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-10-24T11:17:31.441+02:00")

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
	
	@JsonProperty("ID")
  private String id = null;

  @JsonProperty("NAME")
  private String name = null;

  @JsonProperty("validStartDate")
  private String validStartDate = null;

  @JsonProperty("validEndDate")
  private String validEndDate = null;

  public IndicatorPropertiesWithoutGeomType id(String id) {
    this.id = id;
    return this;
  }

   /**
   * the id of the spatial feature
   * @return id
  **/
  @ApiModelProperty(required = true, value = "the id of the spatial feature")
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
  @ApiModelProperty(required = true, value = "the name of the spatial feature")
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
  @ApiModelProperty(required = true, value = "the start date from which on the spatial feature is valid")
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
  @ApiModelProperty(required = true, value = "the end date until the spatial feature is valid - or null if not set")
  public String getValidEndDate() {
    return validEndDate;
  }

  public void setValidEndDate(String validEndDate) {
    this.validEndDate = validEndDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

