package de.hsbo.kommonitor.datamanagement.model.scripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ProcessScriptPOSTInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-03-14T19:45:51.405Z")


public class ProcessScriptPOSTInputType   {
  @JsonProperty("associatedIndicatorId")
  private String associatedIndicatorId = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("requiredGeoresourceIds")
  @Valid
  private List<String> requiredGeoresourceIds = new ArrayList<String>();

  @JsonProperty("requiredIndicatorIds")
  @Valid
  private List<String> requiredIndicatorIds = new ArrayList<String>();

  @JsonProperty("scriptType")
  private String scriptType = null;

  @JsonProperty("scriptCodeBase64")
  private String scriptCodeBase64 = null;

  @JsonProperty("variableProcessParameters")
  @Valid
  private List<ProcessInputType> variableProcessParameters = new ArrayList<ProcessInputType>();

  public ProcessScriptPOSTInputType associatedIndicatorId(String associatedIndicatorId) {
    this.associatedIndicatorId = associatedIndicatorId;
    return this;
  }

  /**
   * unique identifier of the associated indicator (e.g. the indicator that is computed by a script or for which the values shall be aggregated to another spatial unit)
   * @return associatedIndicatorId
  **/
  @ApiModelProperty(required = true, value = "unique identifier of the associated indicator (e.g. the indicator that is computed by a script or for which the values shall be aggregated to another spatial unit)")
  @NotNull


  public String getAssociatedIndicatorId() {
    return associatedIndicatorId;
  }

  public void setAssociatedIndicatorId(String associatedIndicatorId) {
    this.associatedIndicatorId = associatedIndicatorId;
  }

  public ProcessScriptPOSTInputType description(String description) {
    this.description = description;
    return this;
  }

  /**
   * short description of the scripts content (what does it do)
   * @return description
  **/
  @ApiModelProperty(required = true, value = "short description of the scripts content (what does it do)")
  @NotNull


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProcessScriptPOSTInputType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of the process script
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of the process script")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProcessScriptPOSTInputType requiredGeoresourceIds(List<String> requiredGeoresourceIds) {
    this.requiredGeoresourceIds = requiredGeoresourceIds;
    return this;
  }

  public ProcessScriptPOSTInputType addRequiredGeoresourceIdsItem(String requiredGeoresourceIdsItem) {
    this.requiredGeoresourceIds.add(requiredGeoresourceIdsItem);
    return this;
  }

  /**
   * identifiers of georesources that are used within the script.
   * @return requiredGeoresourceIds
  **/
  @ApiModelProperty(required = true, value = "identifiers of georesources that are used within the script.")
  @NotNull


  public List<String> getRequiredGeoresourceIds() {
    return requiredGeoresourceIds;
  }

  public void setRequiredGeoresourceIds(List<String> requiredGeoresourceIds) {
    this.requiredGeoresourceIds = requiredGeoresourceIds;
  }

  public ProcessScriptPOSTInputType requiredIndicatorIds(List<String> requiredIndicatorIds) {
    this.requiredIndicatorIds = requiredIndicatorIds;
    return this;
  }

  public ProcessScriptPOSTInputType addRequiredIndicatorIdsItem(String requiredIndicatorIdsItem) {
    this.requiredIndicatorIds.add(requiredIndicatorIdsItem);
    return this;
  }

  /**
   * identifiers of indicators that are used within the script.
   * @return requiredIndicatorIds
  **/
  @ApiModelProperty(required = true, value = "identifiers of indicators that are used within the script.")
  @NotNull


  public List<String> getRequiredIndicatorIds() {
    return requiredIndicatorIds;
  }

  public void setRequiredIndicatorIds(List<String> requiredIndicatorIds) {
    this.requiredIndicatorIds = requiredIndicatorIds;
  }

  public ProcessScriptPOSTInputType scriptType(String scriptType) {
    this.scriptType = scriptType;
    return this;
  }

  /**
   * a script type reference name used to distuingish process scripts from a client perspective, i.e. setup admin pages due to knowledge about type-specific script parameters and required indicators/georesources
   * @return scriptType
  **/
  @ApiModelProperty(value = "a script type reference name used to distuingish process scripts from a client perspective, i.e. setup admin pages due to knowledge about type-specific script parameters and required indicators/georesources")


  public String getScriptType() {
    return scriptType;
  }

  public void setScriptType(String scriptType) {
    this.scriptType = scriptType;
  }

  public ProcessScriptPOSTInputType scriptCodeBase64(String scriptCodeBase64) {
    this.scriptCodeBase64 = scriptCodeBase64;
    return this;
  }

  /**
   * the actual script code (JavaScript) as BASE64 encoded string
   * @return scriptCodeBase64
  **/
  @ApiModelProperty(required = true, value = "the actual script code (JavaScript) as BASE64 encoded string")
  @NotNull


  public String getScriptCodeBase64() {
    return scriptCodeBase64;
  }

  public void setScriptCodeBase64(String scriptCodeBase64) {
    this.scriptCodeBase64 = scriptCodeBase64;
  }

  public ProcessScriptPOSTInputType variableProcessParameters(List<ProcessInputType> variableProcessParameters) {
    this.variableProcessParameters = variableProcessParameters;
    return this;
  }

  public ProcessScriptPOSTInputType addVariableProcessParametersItem(ProcessInputType variableProcessParametersItem) {
    this.variableProcessParameters.add(variableProcessParametersItem);
    return this;
  }

  /**
   * list of process parameters that can be set by an expert user. They are used within the script to parameterize the indicator computation
   * @return variableProcessParameters
  **/
  @ApiModelProperty(required = true, value = "list of process parameters that can be set by an expert user. They are used within the script to parameterize the indicator computation")
  @NotNull

  @Valid

  public List<ProcessInputType> getVariableProcessParameters() {
    return variableProcessParameters;
  }

  public void setVariableProcessParameters(List<ProcessInputType> variableProcessParameters) {
    this.variableProcessParameters = variableProcessParameters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProcessScriptPOSTInputType processScriptPOSTInputType = (ProcessScriptPOSTInputType) o;
    return Objects.equals(this.associatedIndicatorId, processScriptPOSTInputType.associatedIndicatorId) &&
        Objects.equals(this.description, processScriptPOSTInputType.description) &&
        Objects.equals(this.name, processScriptPOSTInputType.name) &&
        Objects.equals(this.requiredGeoresourceIds, processScriptPOSTInputType.requiredGeoresourceIds) &&
        Objects.equals(this.requiredIndicatorIds, processScriptPOSTInputType.requiredIndicatorIds) &&
        Objects.equals(this.scriptType, processScriptPOSTInputType.scriptType) &&
        Objects.equals(this.scriptCodeBase64, processScriptPOSTInputType.scriptCodeBase64) &&
        Objects.equals(this.variableProcessParameters, processScriptPOSTInputType.variableProcessParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(associatedIndicatorId, description, name, requiredGeoresourceIds, requiredIndicatorIds, scriptType, scriptCodeBase64, variableProcessParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessScriptPOSTInputType {\n");
    
    sb.append("    associatedIndicatorId: ").append(toIndentedString(associatedIndicatorId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    requiredGeoresourceIds: ").append(toIndentedString(requiredGeoresourceIds)).append("\n");
    sb.append("    requiredIndicatorIds: ").append(toIndentedString(requiredIndicatorIds)).append("\n");
    sb.append("    scriptType: ").append(toIndentedString(scriptType)).append("\n");
    sb.append("    scriptCodeBase64: ").append(toIndentedString(scriptCodeBase64)).append("\n");
    sb.append("    variableProcessParameters: ").append(toIndentedString(variableProcessParameters)).append("\n");
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

