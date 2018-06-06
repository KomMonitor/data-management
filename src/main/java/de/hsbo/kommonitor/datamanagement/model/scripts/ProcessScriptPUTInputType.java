package de.hsbo.kommonitor.datamanagement.model.scripts;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.model.scripts.ProcessInputType;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * ProcessScriptPUTInputType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-05-17T10:54:51.077+02:00")

public class ProcessScriptPUTInputType   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("scriptCode")
  private String scriptCode = null;

  @JsonProperty("requiredIndicatorIds")
  
  private List<String> requiredIndicatorIds = new ArrayList<>();

  @JsonProperty("requiredGeoresourceIds")
  
  private List<String> requiredGeoresourceIds = new ArrayList<>();

  @JsonProperty("variableProcessParameters")
  
  private List<ProcessInputType> variableProcessParameters = new ArrayList<>();

  public ProcessScriptPUTInputType name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the process script
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of the process script")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProcessScriptPUTInputType description(String description) {
    this.description = description;
    return this;
  }

   /**
   * short description of the scripts content (what does it do)
   * @return description
  **/
  @ApiModelProperty(required = true, value = "short description of the scripts content (what does it do)")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProcessScriptPUTInputType scriptCode(String scriptCode) {
    this.scriptCode = scriptCode;
    return this;
  }

   /**
   * the actual script code (JavaScript) as a string
   * @return scriptCode
  **/
  @ApiModelProperty(required = true, value = "the actual script code (JavaScript) as a string")
  public String getScriptCode() {
    return scriptCode;
  }

  public void setScriptCode(String scriptCode) {
    this.scriptCode = scriptCode;
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
  public List<String> getRequiredIndicatorIds() {
    return requiredIndicatorIds;
  }

  public void setRequiredIndicatorIds(List<String> requiredIndicatorIds) {
    this.requiredIndicatorIds = requiredIndicatorIds;
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
  public List<String> getRequiredGeoresourceIds() {
    return requiredGeoresourceIds;
  }

  public void setRequiredGeoresourceIds(List<String> requiredGeoresourceIds) {
    this.requiredGeoresourceIds = requiredGeoresourceIds;
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
    return Objects.equals(this.name, processScriptPUTInputType.name) &&
        Objects.equals(this.description, processScriptPUTInputType.description) &&
        Objects.equals(this.scriptCode, processScriptPUTInputType.scriptCode) &&
        Objects.equals(this.requiredIndicatorIds, processScriptPUTInputType.requiredIndicatorIds) &&
        Objects.equals(this.requiredGeoresourceIds, processScriptPUTInputType.requiredGeoresourceIds) &&
        Objects.equals(this.variableProcessParameters, processScriptPUTInputType.variableProcessParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, scriptCode, requiredIndicatorIds, requiredGeoresourceIds, variableProcessParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessScriptPUTInputType {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    scriptCode: ").append(toIndentedString(scriptCode)).append("\n");
    sb.append("    requiredIndicatorIds: ").append(toIndentedString(requiredIndicatorIds)).append("\n");
    sb.append("    requiredGeoresourceIds: ").append(toIndentedString(requiredGeoresourceIds)).append("\n");
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

