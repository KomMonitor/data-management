package de.hsbo.kommonitor.datamanagement.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

/**
 * CommonMetadataType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class CommonMetadataType   {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("sridEPSG")
  private BigDecimal sridEPSG = null;

  @JsonProperty("datasource")
  private String datasource = null;

  @JsonProperty("contact")
  private String contact = null;

  /**
   * Gets or Sets updateInterval
   */
  public enum UpdateIntervalEnum {
    ARBITRARY("ARBITRARY"),
    
    MONTHLY("MONTHLY"),
    
    QUARTERLY("QUARTERLY"),
    
    HALF_YEARLY("HALF_YEARLY"),
    
    YEARLY("YEARLY");

    private String value;

    UpdateIntervalEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UpdateIntervalEnum fromValue(String text) {
      for (UpdateIntervalEnum b : UpdateIntervalEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("updateInterval")
  private UpdateIntervalEnum updateInterval = null;

  public CommonMetadataType description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the dataset
   * @return description
  **/
  @ApiModelProperty(required = true, value = "description of the dataset")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CommonMetadataType sridEPSG(BigDecimal sridEPSG) {
    this.sridEPSG = sridEPSG;
    return this;
  }

   /**
   * the coordinate reference system of the dataset as EPSG code
   * @return sridEPSG
  **/
  @ApiModelProperty(required = true, value = "the coordinate reference system of the dataset as EPSG code")
  public BigDecimal getSridEPSG() {
    return sridEPSG;
  }

  public void setSridEPSG(BigDecimal sridEPSG) {
    this.sridEPSG = sridEPSG;
  }

  public CommonMetadataType datasource(String datasource) {
    this.datasource = datasource;
    return this;
  }

   /**
   * information about the origin/source of the dataset
   * @return datasource
  **/
  @ApiModelProperty(required = true, value = "information about the origin/source of the dataset")
  public String getDatasource() {
    return datasource;
  }

  public void setDatasource(String datasource) {
    this.datasource = datasource;
  }

  public CommonMetadataType contact(String contact) {
    this.contact = contact;
    return this;
  }

   /**
   * contact details where additional information can be achieved
   * @return contact
  **/
  @ApiModelProperty(required = true, value = "contact details where additional information can be achieved")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public CommonMetadataType updateInterval(UpdateIntervalEnum updateInterval) {
    this.updateInterval = updateInterval;
    return this;
  }

   /**
   * Get updateInterval
   * @return updateInterval
  **/
  @ApiModelProperty(required = true, value = "")
  public UpdateIntervalEnum getUpdateInterval() {
    return updateInterval;
  }

  public void setUpdateInterval(UpdateIntervalEnum updateInterval) {
    this.updateInterval = updateInterval;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommonMetadataType commonMetadataType = (CommonMetadataType) o;
    return Objects.equals(this.description, commonMetadataType.description) &&
        Objects.equals(this.sridEPSG, commonMetadataType.sridEPSG) &&
        Objects.equals(this.datasource, commonMetadataType.datasource) &&
        Objects.equals(this.contact, commonMetadataType.contact) &&
        Objects.equals(this.updateInterval, commonMetadataType.updateInterval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, sridEPSG, datasource, contact, updateInterval);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonMetadataType {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    sridEPSG: ").append(toIndentedString(sridEPSG)).append("\n");
    sb.append("    datasource: ").append(toIndentedString(datasource)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    updateInterval: ").append(toIndentedString(updateInterval)).append("\n");
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

