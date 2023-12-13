package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.DefaultClassificationMappingItemType;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-13T09:18:57.441387500+01:00[Europe/Berlin]")
public class DefaultClassificationMappingType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String colorBrewerSchemeName;

  @Valid
  private List<@Valid DefaultClassificationMappingItemType> items = new ArrayList<>();

  public DefaultClassificationMappingType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DefaultClassificationMappingType(String colorBrewerSchemeName, List<@Valid DefaultClassificationMappingItemType> items) {
    this.colorBrewerSchemeName = colorBrewerSchemeName;
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
   * array of classification mapping items. The order of the items corresponds to indicator value intervals from low to high. The number of items represents the number of classes. In combination they represent the default classification and mapping to custom rating of the indicator values
   * @return items
  */
  @NotNull @Valid 
  @Schema(name = "items", description = "array of classification mapping items. The order of the items corresponds to indicator value intervals from low to high. The number of items represents the number of classes. In combination they represent the default classification and mapping to custom rating of the indicator values", requiredMode = Schema.RequiredMode.REQUIRED)
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
        Objects.equals(this.items, defaultClassificationMappingType.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(colorBrewerSchemeName, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultClassificationMappingType {\n");
    sb.append("    colorBrewerSchemeName: ").append(toIndentedString(colorBrewerSchemeName)).append("\n");
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

