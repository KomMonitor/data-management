package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * IndicatorPATCHInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-10T08:34:59.565131300+02:00[Europe/Berlin]")
public class IndicatorPATCHInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> allowedRoles = new ArrayList<>();

  public IndicatorPATCHInputType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IndicatorPATCHInputType(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public IndicatorPATCHInputType allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public IndicatorPATCHInputType addAllowedRolesItem(String allowedRolesItem) {
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
  @NotNull 
  @Schema(name = "allowedRoles", description = "list of role identifiers that have read access rights for this dataset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("allowedRoles")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndicatorPATCHInputType indicatorPATCHInputType = (IndicatorPATCHInputType) o;
    return Objects.equals(this.allowedRoles, indicatorPATCHInputType.allowedRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndicatorPATCHInputType {\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
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

