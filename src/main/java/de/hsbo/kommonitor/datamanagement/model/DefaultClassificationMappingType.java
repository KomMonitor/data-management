package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingItemType;
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
 * DefaultClassificationMappingType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T08:34:59.565131300+02:00[Europe/Berlin]")
public class DefaultClassificationMappingType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String colorBrewerSchemeName;

  private BigDecimal numClasses;

  /**
   * the classification method as enumeration
   */
  public enum ClassificationMethodEnum {
    REGIONAL_DEFAULT("REGIONAL_DEFAULT"),
    
    JENKS("JENKS"),
    
    EQUAL_INTERVAL("EQUAL_INTERVAL"),
    
    QUANTILE("QUANTILE");

    private String value;

    ClassificationMethodEnum(String value) {
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
    public static ClassificationMethodEnum fromValue(String value) {
      for (ClassificationMethodEnum b : ClassificationMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ClassificationMethodEnum classificationMethod;

  @Valid
  private List<@Valid DefaultClassificationMappingItemType> items = new ArrayList<>();

  public DefaultClassificationMappingType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DefaultClassificationMappingType(String colorBrewerSchemeName, BigDecimal numClasses, ClassificationMethodEnum classificationMethod, List<@Valid DefaultClassificationMappingItemType> items) {
    this.colorBrewerSchemeName = colorBrewerSchemeName;
    this.numClasses = numClasses;
    this.classificationMethod = classificationMethod;
    this.items = items;
  }

  public DefaultClassificationMappingType colorBrewerSchemeName(String colorBrewerSchemeName) {
    this.colorBrewerSchemeName = colorBrewerSchemeName;
    return this;
  }

  /**
   * the name of the colorBrewer color scheme used to define the colors for classification (see project http://colorbrewer2.org/#type=sequential&scheme=BuGn&n=3 for colorSchemes). Set to 'INDIVIDUAL' if colors are set arbitrarily.
   * @return colorBrewerSchemeName
  */
  @NotNull 
  @Schema(name = "colorBrewerSchemeName", description = "the name of the colorBrewer color scheme used to define the colors for classification (see project http://colorbrewer2.org/#type=sequential&scheme=BuGn&n=3 for colorSchemes). Set to 'INDIVIDUAL' if colors are set arbitrarily.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("colorBrewerSchemeName")
  public String getColorBrewerSchemeName() {
    return colorBrewerSchemeName;
  }

  public void setColorBrewerSchemeName(String colorBrewerSchemeName) {
    this.colorBrewerSchemeName = colorBrewerSchemeName;
  }

  public DefaultClassificationMappingType numClasses(BigDecimal numClasses) {
    this.numClasses = numClasses;
    return this;
  }

  /**
   * the number of classes
   * minimum: 1
   * maximum: 9
   * @return numClasses
  */
  @NotNull @Valid @DecimalMin("1") @DecimalMax("9") 
  @Schema(name = "numClasses", description = "the number of classes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("numClasses")
  public BigDecimal getNumClasses() {
    return numClasses;
  }

  public void setNumClasses(BigDecimal numClasses) {
    this.numClasses = numClasses;
  }

  public DefaultClassificationMappingType classificationMethod(ClassificationMethodEnum classificationMethod) {
    this.classificationMethod = classificationMethod;
    return this;
  }

  /**
   * the classification method as enumeration
   * @return classificationMethod
  */
  @NotNull 
  @Schema(name = "classificationMethod", description = "the classification method as enumeration", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("classificationMethod")
  public ClassificationMethodEnum getClassificationMethod() {
    return classificationMethod;
  }

  public void setClassificationMethod(ClassificationMethodEnum classificationMethod) {
    this.classificationMethod = classificationMethod;
  }

  public DefaultClassificationMappingType items(List<@Valid DefaultClassificationMappingItemType> items) {
    this.items = items;
    return this;
  }

  public DefaultClassificationMappingType addItemsItem(DefaultClassificationMappingItemType itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * array of classification mapping items. each item holds the break values for a certain spatial unit. not all spatial units of a certain indicator must be set.
   * @return items
  */
  @NotNull @Valid 
  @Schema(name = "items", description = "array of classification mapping items. each item holds the break values for a certain spatial unit. not all spatial units of a certain indicator must be set.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid DefaultClassificationMappingItemType> getItems() {
    return items;
  }

  public void setItems(List<@Valid DefaultClassificationMappingItemType> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultClassificationMappingType defaultClassificationMappingType = (DefaultClassificationMappingType) o;
    return Objects.equals(this.colorBrewerSchemeName, defaultClassificationMappingType.colorBrewerSchemeName) &&
        Objects.equals(this.numClasses, defaultClassificationMappingType.numClasses) &&
        Objects.equals(this.classificationMethod, defaultClassificationMappingType.classificationMethod) &&
        Objects.equals(this.items, defaultClassificationMappingType.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(colorBrewerSchemeName, numClasses, classificationMethod, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultClassificationMappingType {\n");
    sb.append("    colorBrewerSchemeName: ").append(toIndentedString(colorBrewerSchemeName)).append("\n");
    sb.append("    numClasses: ").append(toIndentedString(numClasses)).append("\n");
    sb.append("    classificationMethod: ").append(toIndentedString(classificationMethod)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

