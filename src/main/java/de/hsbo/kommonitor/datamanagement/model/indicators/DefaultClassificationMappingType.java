package de.hsbo.kommonitor.datamanagement.model.indicators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * DefaultClassificationMappingType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-10-16T08:10:08.922+02:00")

public class DefaultClassificationMappingType   {
  @JsonProperty("items")
  
  private List<DefaultClassificationMappingItemType> items = new ArrayList<>();

  public DefaultClassificationMappingType items(List<DefaultClassificationMappingItemType> items) {
    this.items = items;
    return this;
  }

  public DefaultClassificationMappingType addItemsItem(DefaultClassificationMappingItemType itemsItem) {
    this.items.add(itemsItem);
    return this;
  }

   /**
   * array of classification mapping items. In combination they represent the default classification and mapping to custom rating of the indicator values
   * @return items
  **/
  @ApiModelProperty(required = true, value = "array of classification mapping items. In combination they represent the default classification and mapping to custom rating of the indicator values")
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
    return Objects.equals(this.items, defaultClassificationMappingType.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultClassificationMappingType {\n");
    
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

