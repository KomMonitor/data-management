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

  private String testProp;

  public QualitativeClassificationMappingType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QualitativeClassificationMappingType(String testProp, String colorBrewerSchemeName) {
    super(colorBrewerSchemeName);
    this.testProp = testProp;
  }

  public QualitativeClassificationMappingType testProp(String testProp) {
    this.testProp = testProp;
    return this;
  }

  /**
   * Prop for testing
   * @return testProp
   */
  @NotNull 
  @Schema(name = "testProp", description = "Prop for testing", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("testProp")
  public String getTestProp() {
    return testProp;
  }

  public void setTestProp(String testProp) {
    this.testProp = testProp;
  }


  public QualitativeClassificationMappingType classificationType(ClassificationTypeEnum classificationType) {
    super.classificationType(classificationType);
    return this;
  }

  public QualitativeClassificationMappingType colorBrewerSchemeName(String colorBrewerSchemeName) {
    super.colorBrewerSchemeName(colorBrewerSchemeName);
    return this;
  }

  public QualitativeClassificationMappingType individualColors(List<String> individualColors) {
    super.individualColors(individualColors);
    return this;
  }

  public QualitativeClassificationMappingType addIndividualColorsItem(String individualColorsItem) {
    super.addIndividualColorsItem(individualColorsItem);
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
    return Objects.equals(this.testProp, qualitativeClassificationMappingType.testProp) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(testProp, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualitativeClassificationMappingType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    testProp: ").append(toIndentedString(testProp)).append("\n");
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

