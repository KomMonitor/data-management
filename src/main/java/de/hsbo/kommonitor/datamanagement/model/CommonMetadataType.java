package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CommonMetadataType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-21T01:22:09.865691461+01:00[Europe/Berlin]")
public class CommonMetadataType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String contact;

  private String databasis;

  private String datasource;

  private String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate lastUpdate;

  private String literature;

  private String note;

  private BigDecimal sridEPSG;

  /**
   * Gets or Sets updateInterval
   */
  public enum UpdateIntervalEnum {
    ARBITRARY("ARBITRARY"),
    
    MONTHLY("MONTHLY"),
    
    QUARTERLY("QUARTERLY"),
    
    HALF_YEARLY("HALF_YEARLY"),
    
    YEARLY("YEARLY"),
    
    DAILY("DAILY"),
    
    WEEKLY("WEEKLY");

    private String value;

    UpdateIntervalEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UpdateIntervalEnum fromValue(String value) {
      for (UpdateIntervalEnum b : UpdateIntervalEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private UpdateIntervalEnum updateInterval;

  public CommonMetadataType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CommonMetadataType(String contact, String datasource, String description, BigDecimal sridEPSG, UpdateIntervalEnum updateInterval) {
    this.contact = contact;
    this.datasource = datasource;
    this.description = description;
    this.sridEPSG = sridEPSG;
    this.updateInterval = updateInterval;
  }

  public CommonMetadataType contact(String contact) {
    this.contact = contact;
    return this;
  }

  /**
   * contact details where additional information can be achieved
   * @return contact
  */
  @NotNull 
  @Schema(name = "contact", description = "contact details where additional information can be achieved", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contact")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public CommonMetadataType databasis(String databasis) {
    this.databasis = databasis;
    return this;
  }

  /**
   * information about data used as a basis to generate the dataset
   * @return databasis
  */
  
  @Schema(name = "databasis", description = "information about data used as a basis to generate the dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("databasis")
  public String getDatabasis() {
    return databasis;
  }

  public void setDatabasis(String databasis) {
    this.databasis = databasis;
  }

  public CommonMetadataType datasource(String datasource) {
    this.datasource = datasource;
    return this;
  }

  /**
   * information about the origin/source of the dataset
   * @return datasource
  */
  @NotNull 
  @Schema(name = "datasource", description = "information about the origin/source of the dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("datasource")
  public String getDatasource() {
    return datasource;
  }

  public void setDatasource(String datasource) {
    this.datasource = datasource;
  }

  public CommonMetadataType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * description of the dataset
   * @return description
  */
  @NotNull 
  @Schema(name = "description", description = "description of the dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CommonMetadataType lastUpdate(LocalDate lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  /**
   * a timestamp representing the lastUpdate according to ISO 8601 (e.g. 2018-01-30)
   * @return lastUpdate
  */
  @Valid 
  @Schema(name = "lastUpdate", description = "a timestamp representing the lastUpdate according to ISO 8601 (e.g. 2018-01-30)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdate")
  public LocalDate getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(LocalDate lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public CommonMetadataType literature(String literature) {
    this.literature = literature;
    return this;
  }

  /**
   * an optional hint to literature about the dataset (e.g. URL or book/article name)
   * @return literature
  */
  
  @Schema(name = "literature", description = "an optional hint to literature about the dataset (e.g. URL or book/article name)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("literature")
  public String getLiterature() {
    return literature;
  }

  public void setLiterature(String literature) {
    this.literature = literature;
  }

  public CommonMetadataType note(String note) {
    this.note = note;
    return this;
  }

  /**
   * an optional note with background information about the dataset
   * @return note
  */
  
  @Schema(name = "note", description = "an optional note with background information about the dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public CommonMetadataType sridEPSG(BigDecimal sridEPSG) {
    this.sridEPSG = sridEPSG;
    return this;
  }

  /**
   * the coordinate reference system of the dataset as EPSG code
   * @return sridEPSG
  */
  @NotNull @Valid 
  @Schema(name = "sridEPSG", example = "0.0", description = "the coordinate reference system of the dataset as EPSG code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sridEPSG")
  public BigDecimal getSridEPSG() {
    return sridEPSG;
  }

  public void setSridEPSG(BigDecimal sridEPSG) {
    this.sridEPSG = sridEPSG;
  }

  public CommonMetadataType updateInterval(UpdateIntervalEnum updateInterval) {
    this.updateInterval = updateInterval;
    return this;
  }

  /**
   * Get updateInterval
   * @return updateInterval
  */
  @NotNull 
  @Schema(name = "updateInterval", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updateInterval")
  public UpdateIntervalEnum getUpdateInterval() {
    return updateInterval;
  }

  public void setUpdateInterval(UpdateIntervalEnum updateInterval) {
    this.updateInterval = updateInterval;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommonMetadataType commonMetadataType = (CommonMetadataType) o;
    return Objects.equals(this.contact, commonMetadataType.contact) &&
        Objects.equals(this.databasis, commonMetadataType.databasis) &&
        Objects.equals(this.datasource, commonMetadataType.datasource) &&
        Objects.equals(this.description, commonMetadataType.description) &&
        Objects.equals(this.lastUpdate, commonMetadataType.lastUpdate) &&
        Objects.equals(this.literature, commonMetadataType.literature) &&
        Objects.equals(this.note, commonMetadataType.note) &&
        Objects.equals(this.sridEPSG, commonMetadataType.sridEPSG) &&
        Objects.equals(this.updateInterval, commonMetadataType.updateInterval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contact, databasis, datasource, description, lastUpdate, literature, note, sridEPSG, updateInterval);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonMetadataType {\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    databasis: ").append(toIndentedString(databasis)).append("\n");
    sb.append("    datasource: ").append(toIndentedString(datasource)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
    sb.append("    literature: ").append(toIndentedString(literature)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    sridEPSG: ").append(toIndentedString(sridEPSG)).append("\n");
    sb.append("    updateInterval: ").append(toIndentedString(updateInterval)).append("\n");
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

