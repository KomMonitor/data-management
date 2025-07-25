package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hsbo.kommonitor.datamanagement.model.ProcessInputType;
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
 * ProcessScriptOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-26T12:50:04.783434100+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class ProcessScriptOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String description;

  private String indicatorId;

  private String name;

  @Valid
  private List<String> requiredGeoresourceIds = new ArrayList<>();

  @Valid
  private List<String> requiredIndicatorIds = new ArrayList<>();

  private String scriptId;

  private @Nullable String scriptType;

  @Valid
  private List<@Valid ProcessInputType> variableProcessParameters = new ArrayList<>();

  public ProcessScriptOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProcessScriptOverviewType(String description, String indicatorId, String name, List<String> requiredGeoresourceIds, List<String> requiredIndicatorIds, String scriptId, List<@Valid ProcessInputType> variableProcessParameters) {
    this.description = description;
    this.indicatorId = indicatorId;
    this.name = name;
    this.requiredGeoresourceIds = requiredGeoresourceIds;
    this.requiredIndicatorIds = requiredIndicatorIds;
    this.scriptId = scriptId;
    this.variableProcessParameters = variableProcessParameters;
  }

  public ProcessScriptOverviewType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * short description of the scripts content (what does it do)
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "short description of the scripts content (what does it do)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProcessScriptOverviewType indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

  /**
   * unique identifier of the associated indicator (e.g. the indicator that is computed by a script or for which the values shall be aggregated to another spatial unit)
   * @return indicatorId
   */
  @NotNull 
  @Schema(name = "indicatorId", description = "unique identifier of the associated indicator (e.g. the indicator that is computed by a script or for which the values shall be aggregated to another spatial unit)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorId")
  public String getIndicatorId() {
    return indicatorId;
  }

  public void setIndicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
  }

  public ProcessScriptOverviewType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of the process script
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "name of the process script", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProcessScriptOverviewType requiredGeoresourceIds(List<String> requiredGeoresourceIds) {
    this.requiredGeoresourceIds = requiredGeoresourceIds;
    return this;
  }

  public ProcessScriptOverviewType addRequiredGeoresourceIdsItem(String requiredGeoresourceIdsItem) {
    if (this.requiredGeoresourceIds == null) {
      this.requiredGeoresourceIds = new ArrayList<>();
    }
    this.requiredGeoresourceIds.add(requiredGeoresourceIdsItem);
    return this;
  }

  /**
   * identifiers of georesources that are used within the script.
   * @return requiredGeoresourceIds
   */
  @NotNull 
  @Schema(name = "requiredGeoresourceIds", description = "identifiers of georesources that are used within the script.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requiredGeoresourceIds")
  public List<String> getRequiredGeoresourceIds() {
    return requiredGeoresourceIds;
  }

  public void setRequiredGeoresourceIds(List<String> requiredGeoresourceIds) {
    this.requiredGeoresourceIds = requiredGeoresourceIds;
  }

  public ProcessScriptOverviewType requiredIndicatorIds(List<String> requiredIndicatorIds) {
    this.requiredIndicatorIds = requiredIndicatorIds;
    return this;
  }

  public ProcessScriptOverviewType addRequiredIndicatorIdsItem(String requiredIndicatorIdsItem) {
    if (this.requiredIndicatorIds == null) {
      this.requiredIndicatorIds = new ArrayList<>();
    }
    this.requiredIndicatorIds.add(requiredIndicatorIdsItem);
    return this;
  }

  /**
   * identifiers of indicators that are used within the script.
   * @return requiredIndicatorIds
   */
  @NotNull 
  @Schema(name = "requiredIndicatorIds", description = "identifiers of indicators that are used within the script.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requiredIndicatorIds")
  public List<String> getRequiredIndicatorIds() {
    return requiredIndicatorIds;
  }

  public void setRequiredIndicatorIds(List<String> requiredIndicatorIds) {
    this.requiredIndicatorIds = requiredIndicatorIds;
  }

  public ProcessScriptOverviewType scriptId(String scriptId) {
    this.scriptId = scriptId;
    return this;
  }

  /**
   * unique identifier of the process script
   * @return scriptId
   */
  @NotNull 
  @Schema(name = "scriptId", description = "unique identifier of the process script", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scriptId")
  public String getScriptId() {
    return scriptId;
  }

  public void setScriptId(String scriptId) {
    this.scriptId = scriptId;
  }

  public ProcessScriptOverviewType scriptType(String scriptType) {
    this.scriptType = scriptType;
    return this;
  }

  /**
   * a script type reference name used to distuingish process scripts from a client perspective, i.e. setup admin pages due to knowledge about type-specific script parameters and required indicators/georesources
   * @return scriptType
   */
  
  @Schema(name = "scriptType", description = "a script type reference name used to distuingish process scripts from a client perspective, i.e. setup admin pages due to knowledge about type-specific script parameters and required indicators/georesources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scriptType")
  public String getScriptType() {
    return scriptType;
  }

  public void setScriptType(String scriptType) {
    this.scriptType = scriptType;
  }

  public ProcessScriptOverviewType variableProcessParameters(List<@Valid ProcessInputType> variableProcessParameters) {
    this.variableProcessParameters = variableProcessParameters;
    return this;
  }

  public ProcessScriptOverviewType addVariableProcessParametersItem(ProcessInputType variableProcessParametersItem) {
    if (this.variableProcessParameters == null) {
      this.variableProcessParameters = new ArrayList<>();
    }
    this.variableProcessParameters.add(variableProcessParametersItem);
    return this;
  }

  /**
   * list of process parameters that can be set by an expert user. They are used within the script to parameterize the indicator computation
   * @return variableProcessParameters
   */
  @NotNull @Valid 
  @Schema(name = "variableProcessParameters", description = "list of process parameters that can be set by an expert user. They are used within the script to parameterize the indicator computation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("variableProcessParameters")
  public List<@Valid ProcessInputType> getVariableProcessParameters() {
    return variableProcessParameters;
  }

  public void setVariableProcessParameters(List<@Valid ProcessInputType> variableProcessParameters) {
    this.variableProcessParameters = variableProcessParameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProcessScriptOverviewType processScriptOverviewType = (ProcessScriptOverviewType) o;
    return Objects.equals(this.description, processScriptOverviewType.description) &&
        Objects.equals(this.indicatorId, processScriptOverviewType.indicatorId) &&
        Objects.equals(this.name, processScriptOverviewType.name) &&
        Objects.equals(this.requiredGeoresourceIds, processScriptOverviewType.requiredGeoresourceIds) &&
        Objects.equals(this.requiredIndicatorIds, processScriptOverviewType.requiredIndicatorIds) &&
        Objects.equals(this.scriptId, processScriptOverviewType.scriptId) &&
        Objects.equals(this.scriptType, processScriptOverviewType.scriptType) &&
        Objects.equals(this.variableProcessParameters, processScriptOverviewType.variableProcessParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, indicatorId, name, requiredGeoresourceIds, requiredIndicatorIds, scriptId, scriptType, variableProcessParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessScriptOverviewType {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    indicatorId: ").append(toIndentedString(indicatorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    requiredGeoresourceIds: ").append(toIndentedString(requiredGeoresourceIds)).append("\n");
    sb.append("    requiredIndicatorIds: ").append(toIndentedString(requiredIndicatorIds)).append("\n");
    sb.append("    scriptId: ").append(toIndentedString(scriptId)).append("\n");
    sb.append("    scriptType: ").append(toIndentedString(scriptType)).append("\n");
    sb.append("    variableProcessParameters: ").append(toIndentedString(variableProcessParameters)).append("\n");
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

