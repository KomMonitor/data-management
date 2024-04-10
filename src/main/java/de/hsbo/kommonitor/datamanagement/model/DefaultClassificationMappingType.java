package de.hsbo.kommonitor.datamanagement.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DefaultClassificationMappingType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-09T13:07:45.192171293Z[GMT]")


public class DefaultClassificationMappingType   {
  @JsonProperty("colorBrewerSchemeName")
  private String colorBrewerSchemeName = null;

  @JsonProperty("numClasses")
  private BigDecimal numClasses = null;

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

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ClassificationMethodEnum fromValue(String text) {
      for (ClassificationMethodEnum b : ClassificationMethodEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("classificationMethod")
  private ClassificationMethodEnum classificationMethod = null;

  @JsonProperty("items")
  @Valid
  private List<DefaultClassificationMappingItemType> items = new ArrayList<DefaultClassificationMappingItemType>();

  public DefaultClassificationMappingType colorBrewerSchemeName(String colorBrewerSchemeName) {
    this.colorBrewerSchemeName = colorBrewerSchemeName;
    return this;
  }

  /**
   * the name of the colorBrewer color scheme used to define the colors for classification (see project http://colorbrewer2.org/#type=sequential&scheme=BuGn&n=3 for colorSchemes). Set to 'INDIVIDUAL' if colors are set arbitrarily.
   * @return colorBrewerSchemeName
   **/
  @Schema(required = true, description = "the name of the colorBrewer color scheme used to define the colors for classification (see project http://colorbrewer2.org/#type=sequential&scheme=BuGn&n=3 for colorSchemes). Set to 'INDIVIDUAL' if colors are set arbitrarily.")
      @NotNull

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
   **/
  @Schema(required = true, description = "the number of classes")
      @NotNull

    @Valid
  @DecimalMin("1") @DecimalMax("9")   public BigDecimal getNumClasses() {
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
   **/
  @Schema(required = true, description = "the classification method as enumeration")
      @NotNull

    public ClassificationMethodEnum getClassificationMethod() {
    return classificationMethod;
  }

  public void setClassificationMethod(ClassificationMethodEnum classificationMethod) {
    this.classificationMethod = classificationMethod;
  }

  public DefaultClassificationMappingType items(List<DefaultClassificationMappingItemType> items) {
    this.items = items;
    return this;
  }

  public DefaultClassificationMappingType addItemsItem(DefaultClassificationMappingItemType itemsItem) {
    this.items.add(itemsItem);
    return this;
  }

  /**
   * array of classification mapping items. each item holds the break values for a certain spatial unit. not all spatial units of a certain indicator must be set.
   * @return items
   **/
  @Schema(required = true, description = "array of classification mapping items. each item holds the break values for a certain spatial unit. not all spatial units of a certain indicator must be set.")
      @NotNull
    @Valid
    public List<DefaultClassificationMappingItemType> getItems() {
    return items;
  }

  public void setItems(List<DefaultClassificationMappingItemType> items) {
    this.items = items;
  }


  @Override
  public boolean equals(java.lang.Object o) {
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
