package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hsbo.kommonitor.datamanagement.model.AbstractClassificationMappingType;
import de.hsbo.kommonitor.datamanagement.model.ClassificationTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.QualitativeClassificationMappingItemType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QualitativeClassificationMappingType
 */


@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.18.0")
public class QualitativeClassificationMappingType extends AbstractClassificationMappingType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid QualitativeClassificationMappingItemType> items = new ArrayList<>();

  public QualitativeClassificationMappingType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QualitativeClassificationMappingType(List<@Valid QualitativeClassificationMappingItemType> items, String colorBrewerSchemeName) {
    super(colorBrewerSchemeName);
    this.items = items;
  }

  public QualitativeClassificationMappingType items(List<@Valid QualitativeClassificationMappingItemType> items) {
    this.items = items;
    return this;
  }

  public QualitativeClassificationMappingType addItemsItem(QualitativeClassificationMappingItemType itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Array of classification mapping items. Each item holds categorical data as well as its color and label mappings for a certain spatial unit.
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", description = "Array of classification mapping items. Each item holds categorical data as well as its color and label mappings for a certain spatial unit.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid QualitativeClassificationMappingItemType> getItems() {
    return items;
  }

  public void setItems(List<@Valid QualitativeClassificationMappingItemType> items) {
    this.items = items;
  }


  public QualitativeClassificationMappingType classificationType(ClassificationTypeEnum classificationType) {
    super.classificationType(classificationType);
    return this;
  }

  public QualitativeClassificationMappingType colorBrewerSchemeName(String colorBrewerSchemeName) {
    super.colorBrewerSchemeName(colorBrewerSchemeName);
    return this;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QualitativeClassificationMappingType qualitativeClassificationMappingType = (QualitativeClassificationMappingType) o;
    return Objects.equals(this.items, qualitativeClassificationMappingType.items) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualitativeClassificationMappingType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

