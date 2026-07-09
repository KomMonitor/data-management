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
import de.hsbo.kommonitor.datamanagement.model.CategoricalMappingType;
import de.hsbo.kommonitor.datamanagement.model.ClassificationTypeEnum;
import java.math.BigDecimal;
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


@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.23.0")
public class QualitativeClassificationMappingType extends AbstractClassificationMappingType implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<@Valid CategoricalMappingType> categoricalData = new ArrayList<>();

  public QualitativeClassificationMappingType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QualitativeClassificationMappingType(List<@Valid CategoricalMappingType> categoricalData, String colorBrewerSchemeName, BigDecimal numClasses) {
    super(colorBrewerSchemeName, numClasses);
    this.categoricalData = categoricalData;
  }

  public QualitativeClassificationMappingType categoricalData(List<@Valid CategoricalMappingType> categoricalData) {
    this.categoricalData = categoricalData;
    return this;
  }

  public QualitativeClassificationMappingType addCategoricalDataItem(CategoricalMappingType categoricalDataItem) {
    if (this.categoricalData == null) {
      this.categoricalData = new ArrayList<>();
    }
    this.categoricalData.add(categoricalDataItem);
    return this;
  }

  /**
   * mapping of categorical values, colors and labels
   * @return categoricalData
   */
  @NotNull @Valid 
  @Schema(name = "categoricalData", description = "mapping of categorical values, colors and labels", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("categoricalData")
  public List<@Valid CategoricalMappingType> getCategoricalData() {
    return categoricalData;
  }

  @JsonProperty("categoricalData")
  public void setCategoricalData(List<@Valid CategoricalMappingType> categoricalData) {
    this.categoricalData = categoricalData;
  }


  public QualitativeClassificationMappingType classificationType(ClassificationTypeEnum classificationType) {
    super.classificationType(classificationType);
    return this;
  }

  public QualitativeClassificationMappingType colorBrewerSchemeName(String colorBrewerSchemeName) {
    super.colorBrewerSchemeName(colorBrewerSchemeName);
    return this;
  }

  public QualitativeClassificationMappingType numClasses(BigDecimal numClasses) {
    super.numClasses(numClasses);
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
    return Objects.equals(this.categoricalData, qualitativeClassificationMappingType.categoricalData) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoricalData, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualitativeClassificationMappingType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    categoricalData: ").append(toIndentedString(categoricalData)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

