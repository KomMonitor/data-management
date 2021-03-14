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
 * ProcessScriptPUTInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-03-14T19:45:51.405Z")


public class ProcessScriptPUTInputType   {
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

  public ProcessScriptPUTInputType description(String description) {
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

  public ProcessScriptPUTInputType name(String name) {
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

  public ProcessScriptPUTInputType requiredGeoresourceIds(List<String> requiredGeoresourceIds) {
    this.requiredGeoresourceIds = requiredGeoresourceIds;
    return this;
  }

  public ProcessScriptPUTInputType addRequiredGeoresourceIdsItem(String requiredGeoresourceIdsItem) {
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

  public ProcessScriptPUTInputType requiredIndicatorIds(List<String> requiredIndicatorIds) {
    this.requiredIndicatorIds = requiredIndicatorIds;
    return this;
  }

  public ProcessScriptPUTInputType addRequiredIndicatorIdsItem(String requiredIndicatorIdsItem) {
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

  public ProcessScriptPUTInputType scriptType(String scriptType) {
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

  public ProcessScriptPUTInputType scriptCodeBase64(String scriptCodeBase64) {
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

  public ProcessScriptPUTInputType variableProcessParameters(List<ProcessInputType> variableProcessParameters) {
    this.variableProcessParameters = variableProcessParameters;
    return this;
  }

  public ProcessScriptPUTInputType addVariableProcessParametersItem(ProcessInputType variableProcessParametersItem) {
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
    ProcessScriptPUTInputType processScriptPUTInputType = (ProcessScriptPUTInputType) o;
    return Objects.equals(this.description, processScriptPUTInputType.description) &&
        Objects.equals(this.name, processScriptPUTInputType.name) &&
        Objects.equals(this.requiredGeoresourceIds, processScriptPUTInputType.requiredGeoresourceIds) &&
        Objects.equals(this.requiredIndicatorIds, processScriptPUTInputType.requiredIndicatorIds) &&
        Objects.equals(this.scriptType, processScriptPUTInputType.scriptType) &&
        Objects.equals(this.scriptCodeBase64, processScriptPUTInputType.scriptCodeBase64) &&
        Objects.equals(this.variableProcessParameters, processScriptPUTInputType.variableProcessParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, name, requiredGeoresourceIds, requiredIndicatorIds, scriptType, scriptCodeBase64, variableProcessParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessScriptPUTInputType {\n");
    
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

