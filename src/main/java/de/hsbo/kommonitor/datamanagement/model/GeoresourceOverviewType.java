package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.CommonMetadataType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import java.math.BigDecimal;
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
 * GeoresourceOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-06T15:24:58.815569400+01:00[Europe/Berlin]")
public class GeoresourceOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> allowedRoles;

  private String aoiColor;

  @Valid
  private List<@Valid PeriodOfValidityType> availablePeriodsOfValidity = new ArrayList<>();

  private String datasetName;

  private String georesourceId;

  private Boolean isAOI;

  private Boolean isLOI;

  private Boolean isPOI;

  private String loiColor;

  private String loiDashArrayString;

  private BigDecimal loiWidth;

  private CommonMetadataType metadata;

  /**
   * If georesource is a POI then custom POI marker color can be set by specifying one of the following color names
   */
  public enum PoiMarkerColorEnum {
    WHITE("white"),
    
    RED("red"),
    
    ORANGE("orange"),
    
    BEIGE("beige"),
    
    GREEN("green"),
    
    BLUE("blue"),
    
    PURPLE("purple"),
    
    PINK("pink"),
    
    GRAY("gray"),
    
    BLACK("black");

    private String value;

    PoiMarkerColorEnum(String value) {
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
    public static PoiMarkerColorEnum fromValue(String value) {
      for (PoiMarkerColorEnum b : PoiMarkerColorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PoiMarkerColorEnum poiMarkerColor;

  private String poiSymbolBootstrap3Name;

  /**
   * If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names
   */
  public enum PoiSymbolColorEnum {
    WHITE("white"),
    
    RED("red"),
    
    ORANGE("orange"),
    
    BEIGE("beige"),
    
    GREEN("green"),
    
    BLUE("blue"),
    
    PURPLE("purple"),
    
    PINK("pink"),
    
    GRAY("gray"),
    
    BLACK("black");

    private String value;

    PoiSymbolColorEnum(String value) {
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
    public static PoiSymbolColorEnum fromValue(String value) {
      for (PoiSymbolColorEnum b : PoiSymbolColorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PoiSymbolColorEnum poiSymbolColor;

  private String topicReference;

  /**
   * Gets or Sets userPermissions
   */
  public enum UserPermissionsEnum {
    CREATOR("creator"),
    
    PUBLISHER("publisher"),
    
    EDITOR("editor"),
    
    VIEWER("viewer");

    private String value;

    UserPermissionsEnum(String value) {
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
    public static UserPermissionsEnum fromValue(String value) {
      for (UserPermissionsEnum b : UserPermissionsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<UserPermissionsEnum> userPermissions;

  private String wfsUrl;

  private String wmsUrl;

  public GeoresourceOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GeoresourceOverviewType(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity, String datasetName, String georesourceId, Boolean isAOI, Boolean isLOI, Boolean isPOI, CommonMetadataType metadata, String topicReference, String wfsUrl, String wmsUrl) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    this.datasetName = datasetName;
    this.georesourceId = georesourceId;
    this.isAOI = isAOI;
    this.isLOI = isLOI;
    this.isPOI = isPOI;
    this.metadata = metadata;
    this.topicReference = topicReference;
    this.wfsUrl = wfsUrl;
    this.wmsUrl = wmsUrl;
  }

  public GeoresourceOverviewType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public GeoresourceOverviewType addAllowedRolesItem(String allowedRolesItem) {
    if (this.allowedRoles == null) {
      this.allowedRoles = new ArrayList<>();
    }
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * list of role identifiers that have read access rights for this dataset
   * @return allowedRoles
  */
  
  @Schema(name = "allowedRoles", description = "list of role identifiers that have read access rights for this dataset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowedRoles")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public GeoresourceOverviewType aoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
    return this;
  }

  /**
   * color name or color code (i.e. hex number) for areas of interest
   * @return aoiColor
  */
  
  @Schema(name = "aoiColor", description = "color name or color code (i.e. hex number) for areas of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aoiColor")
  public String getAoiColor() {
    return aoiColor;
  }

  public void setAoiColor(String aoiColor) {
    this.aoiColor = aoiColor;
  }

  public GeoresourceOverviewType availablePeriodsOfValidity(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
    return this;
  }

  public GeoresourceOverviewType addAvailablePeriodsOfValidityItem(PeriodOfValidityType availablePeriodsOfValidityItem) {
    if (this.availablePeriodsOfValidity == null) {
      this.availablePeriodsOfValidity = new ArrayList<>();
    }
    this.availablePeriodsOfValidity.add(availablePeriodsOfValidityItem);
    return this;
  }

  /**
   * Get availablePeriodsOfValidity
   * @return availablePeriodsOfValidity
  */
  @NotNull @Valid 
  @Schema(name = "availablePeriodsOfValidity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("availablePeriodsOfValidity")
  public List<@Valid PeriodOfValidityType> getAvailablePeriodsOfValidity() {
    return availablePeriodsOfValidity;
  }

  public void setAvailablePeriodsOfValidity(List<@Valid PeriodOfValidityType> availablePeriodsOfValidity) {
    this.availablePeriodsOfValidity = availablePeriodsOfValidity;
  }

  public GeoresourceOverviewType datasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  /**
   * the meaningful name of the dataset
   * @return datasetName
  */
  @NotNull 
  @Schema(name = "datasetName", description = "the meaningful name of the dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("datasetName")
  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public GeoresourceOverviewType georesourceId(String georesourceId) {
    this.georesourceId = georesourceId;
    return this;
  }

  /**
   * the unique identifier of the dataset
   * @return georesourceId
  */
  @NotNull 
  @Schema(name = "georesourceId", description = "the unique identifier of the dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("georesourceId")
  public String getGeoresourceId() {
    return georesourceId;
  }

  public void setGeoresourceId(String georesourceId) {
    this.georesourceId = georesourceId;
  }

  public GeoresourceOverviewType isAOI(Boolean isAOI) {
    this.isAOI = isAOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains areas of interest
   * @return isAOI
  */
  @NotNull 
  @Schema(name = "isAOI", description = "boolean value indicating if the dataset contains areas of interest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isAOI")
  public Boolean getIsAOI() {
    return isAOI;
  }

  public void setIsAOI(Boolean isAOI) {
    this.isAOI = isAOI;
  }

  public GeoresourceOverviewType isLOI(Boolean isLOI) {
    this.isLOI = isLOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains lines of interest
   * @return isLOI
  */
  @NotNull 
  @Schema(name = "isLOI", description = "boolean value indicating if the dataset contains lines of interest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isLOI")
  public Boolean getIsLOI() {
    return isLOI;
  }

  public void setIsLOI(Boolean isLOI) {
    this.isLOI = isLOI;
  }

  public GeoresourceOverviewType isPOI(Boolean isPOI) {
    this.isPOI = isPOI;
    return this;
  }

  /**
   * boolean value indicating if the dataset contains points of interest
   * @return isPOI
  */
  @NotNull 
  @Schema(name = "isPOI", description = "boolean value indicating if the dataset contains points of interest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isPOI")
  public Boolean getIsPOI() {
    return isPOI;
  }

  public void setIsPOI(Boolean isPOI) {
    this.isPOI = isPOI;
  }

  public GeoresourceOverviewType loiColor(String loiColor) {
    this.loiColor = loiColor;
    return this;
  }

  /**
   * color name or color code (i.e. hex number) for lines of interest
   * @return loiColor
  */
  
  @Schema(name = "loiColor", description = "color name or color code (i.e. hex number) for lines of interest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiColor")
  public String getLoiColor() {
    return loiColor;
  }

  public void setLoiColor(String loiColor) {
    this.loiColor = loiColor;
  }

  public GeoresourceOverviewType loiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
    return this;
  }

  /**
   * sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)
   * @return loiDashArrayString
  */
  
  @Schema(name = "loiDashArrayString", description = "sring of line stroke dash array for lines of interest (e.g. 20,20; see https://developer.mozilla.org/de/docs/Web/SVG/Attribute/stroke-dasharray)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiDashArrayString")
  public String getLoiDashArrayString() {
    return loiDashArrayString;
  }

  public void setLoiDashArrayString(String loiDashArrayString) {
    this.loiDashArrayString = loiDashArrayString;
  }

  public GeoresourceOverviewType loiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
    return this;
  }

  /**
   * display width for lines of interest (number of pixels in leaflet)
   * @return loiWidth
  */
  @Valid 
  @Schema(name = "loiWidth", example = "0.0", description = "display width for lines of interest (number of pixels in leaflet)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loiWidth")
  public BigDecimal getLoiWidth() {
    return loiWidth;
  }

  public void setLoiWidth(BigDecimal loiWidth) {
    this.loiWidth = loiWidth;
  }

  public GeoresourceOverviewType metadata(CommonMetadataType metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
  */
  @NotNull @Valid 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metadata")
  public CommonMetadataType getMetadata() {
    return metadata;
  }

  public void setMetadata(CommonMetadataType metadata) {
    this.metadata = metadata;
  }

  public GeoresourceOverviewType poiMarkerColor(PoiMarkerColorEnum poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
    return this;
  }

  /**
   * If georesource is a POI then custom POI marker color can be set by specifying one of the following color names
   * @return poiMarkerColor
  */
  
  @Schema(name = "poiMarkerColor", description = "If georesource is a POI then custom POI marker color can be set by specifying one of the following color names", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiMarkerColor")
  public PoiMarkerColorEnum getPoiMarkerColor() {
    return poiMarkerColor;
  }

  public void setPoiMarkerColor(PoiMarkerColorEnum poiMarkerColor) {
    this.poiMarkerColor = poiMarkerColor;
  }

  public GeoresourceOverviewType poiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
    return this;
  }

  /**
   * If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)
   * @return poiSymbolBootstrap3Name
  */
  
  @Schema(name = "poiSymbolBootstrap3Name", description = "If georesource is a POI then custom POI marker symbol can be set by specifying the name of a Bootstrap 3 glyphicon symbol (i.e. \"home\" for a home symbol or \"education\" for a students hat symbol)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiSymbolBootstrap3Name")
  public String getPoiSymbolBootstrap3Name() {
    return poiSymbolBootstrap3Name;
  }

  public void setPoiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
    this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
  }

  public GeoresourceOverviewType poiSymbolColor(PoiSymbolColorEnum poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
    return this;
  }

  /**
   * If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names
   * @return poiSymbolColor
  */
  
  @Schema(name = "poiSymbolColor", description = "If georesource is a POI then custom POI symbol color can be set by specifying one of the following color names", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poiSymbolColor")
  public PoiSymbolColorEnum getPoiSymbolColor() {
    return poiSymbolColor;
  }

  public void setPoiSymbolColor(PoiSymbolColorEnum poiSymbolColor) {
    this.poiSymbolColor = poiSymbolColor;
  }

  public GeoresourceOverviewType topicReference(String topicReference) {
    this.topicReference = topicReference;
    return this;
  }

  /**
   * id of the last topic hierarchy entity 
   * @return topicReference
  */
  @NotNull 
  @Schema(name = "topicReference", description = "id of the last topic hierarchy entity ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("topicReference")
  public String getTopicReference() {
    return topicReference;
  }

  public void setTopicReference(String topicReference) {
    this.topicReference = topicReference;
  }

  public GeoresourceOverviewType userPermissions(List<UserPermissionsEnum> userPermissions) {
    this.userPermissions = userPermissions;
    return this;
  }

  public GeoresourceOverviewType addUserPermissionsItem(UserPermissionsEnum userPermissionsItem) {
    if (this.userPermissions == null) {
      this.userPermissions = new ArrayList<>();
    }
    this.userPermissions.add(userPermissionsItem);
    return this;
  }

  /**
   * list of permissions that are effective on this dataset for the current user 
   * @return userPermissions
  */
  
  @Schema(name = "userPermissions", description = "list of permissions that are effective on this dataset for the current user ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userPermissions")
  public List<UserPermissionsEnum> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<UserPermissionsEnum> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public GeoresourceOverviewType wfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
    return this;
  }

  /**
   * the URL of a running WFS instance serving the spatial features of the associated dataset
   * @return wfsUrl
  */
  @NotNull 
  @Schema(name = "wfsUrl", description = "the URL of a running WFS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("wfsUrl")
  public String getWfsUrl() {
    return wfsUrl;
  }

  public void setWfsUrl(String wfsUrl) {
    this.wfsUrl = wfsUrl;
  }

  public GeoresourceOverviewType wmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
    return this;
  }

  /**
   * the URL of a running WMS instance serving the spatial features of the associated dataset
   * @return wmsUrl
  */
  @NotNull 
  @Schema(name = "wmsUrl", description = "the URL of a running WMS instance serving the spatial features of the associated dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("wmsUrl")
  public String getWmsUrl() {
    return wmsUrl;
  }

  public void setWmsUrl(String wmsUrl) {
    this.wmsUrl = wmsUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoresourceOverviewType georesourceOverviewType = (GeoresourceOverviewType) o;
    return Objects.equals(this.allowedRoles, georesourceOverviewType.allowedRoles) &&
        Objects.equals(this.aoiColor, georesourceOverviewType.aoiColor) &&
        Objects.equals(this.availablePeriodsOfValidity, georesourceOverviewType.availablePeriodsOfValidity) &&
        Objects.equals(this.datasetName, georesourceOverviewType.datasetName) &&
        Objects.equals(this.georesourceId, georesourceOverviewType.georesourceId) &&
        Objects.equals(this.isAOI, georesourceOverviewType.isAOI) &&
        Objects.equals(this.isLOI, georesourceOverviewType.isLOI) &&
        Objects.equals(this.isPOI, georesourceOverviewType.isPOI) &&
        Objects.equals(this.loiColor, georesourceOverviewType.loiColor) &&
        Objects.equals(this.loiDashArrayString, georesourceOverviewType.loiDashArrayString) &&
        Objects.equals(this.loiWidth, georesourceOverviewType.loiWidth) &&
        Objects.equals(this.metadata, georesourceOverviewType.metadata) &&
        Objects.equals(this.poiMarkerColor, georesourceOverviewType.poiMarkerColor) &&
        Objects.equals(this.poiSymbolBootstrap3Name, georesourceOverviewType.poiSymbolBootstrap3Name) &&
        Objects.equals(this.poiSymbolColor, georesourceOverviewType.poiSymbolColor) &&
        Objects.equals(this.topicReference, georesourceOverviewType.topicReference) &&
        Objects.equals(this.userPermissions, georesourceOverviewType.userPermissions) &&
        Objects.equals(this.wfsUrl, georesourceOverviewType.wfsUrl) &&
        Objects.equals(this.wmsUrl, georesourceOverviewType.wmsUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles, aoiColor, availablePeriodsOfValidity, datasetName, georesourceId, isAOI, isLOI, isPOI, loiColor, loiDashArrayString, loiWidth, metadata, poiMarkerColor, poiSymbolBootstrap3Name, poiSymbolColor, topicReference, userPermissions, wfsUrl, wmsUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeoresourceOverviewType {\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    aoiColor: ").append(toIndentedString(aoiColor)).append("\n");
    sb.append("    availablePeriodsOfValidity: ").append(toIndentedString(availablePeriodsOfValidity)).append("\n");
    sb.append("    datasetName: ").append(toIndentedString(datasetName)).append("\n");
    sb.append("    georesourceId: ").append(toIndentedString(georesourceId)).append("\n");
    sb.append("    isAOI: ").append(toIndentedString(isAOI)).append("\n");
    sb.append("    isLOI: ").append(toIndentedString(isLOI)).append("\n");
    sb.append("    isPOI: ").append(toIndentedString(isPOI)).append("\n");
    sb.append("    loiColor: ").append(toIndentedString(loiColor)).append("\n");
    sb.append("    loiDashArrayString: ").append(toIndentedString(loiDashArrayString)).append("\n");
    sb.append("    loiWidth: ").append(toIndentedString(loiWidth)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    poiMarkerColor: ").append(toIndentedString(poiMarkerColor)).append("\n");
    sb.append("    poiSymbolBootstrap3Name: ").append(toIndentedString(poiSymbolBootstrap3Name)).append("\n");
    sb.append("    poiSymbolColor: ").append(toIndentedString(poiSymbolColor)).append("\n");
    sb.append("    topicReference: ").append(toIndentedString(topicReference)).append("\n");
    sb.append("    userPermissions: ").append(toIndentedString(userPermissions)).append("\n");
    sb.append("    wfsUrl: ").append(toIndentedString(wfsUrl)).append("\n");
    sb.append("    wmsUrl: ").append(toIndentedString(wmsUrl)).append("\n");
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

