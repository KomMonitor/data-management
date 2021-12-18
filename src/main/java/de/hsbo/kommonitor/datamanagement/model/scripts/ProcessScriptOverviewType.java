package de.hsbo.kommonitor.datamanagement.model.scripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ProcessScriptOverviewType
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-03-14T19:45:51.405Z")


public class ProcessScriptOverviewType   {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("indicatorId")
  private String indicatorId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("requiredGeoresourceIds")
  
  private List<String> requiredGeoresourceIds = new ArrayList<String>();

  @JsonProperty("requiredIndicatorIds")
  
  private List<String> requiredIndicatorIds = new ArrayList<String>();

  @JsonProperty("scriptId")
  private String scriptId = null;

  @JsonProperty("scriptType")
  private String scriptType = null;

  @JsonProperty("variableProcessParameters")
  
  private List<ProcessInputType> variableProcessParameters = new ArrayList<ProcessInputType>();

  public ProcessScriptOverviewType description(String description) {
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

  public ProcessScriptOverviewType indicatorId(String indicatorId) {
    this.indicatorId = indicatorId;
    return this;
  }

  /**
   * unique identifier of the associated indicator (e.g. the indicator that is computed by a script or for which the values shall be aggregated to another spatial unit)
   * @return indicatorId
  **/
  @ApiModelProperty(required = true, value = "unique identifier of the associated indicator (e.g. the indicator that is computed by a script or for which the values shall be aggregated to another spatial unit)")
  


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
  **/
  @ApiModelProperty(required = true, value = "name of the process script")
  


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

  public ProcessScriptOverviewType requiredIndicatorIds(List<String> requiredIndicatorIds) {
    this.requiredIndicatorIds = requiredIndicatorIds;
    return this;
  }

  public ProcessScriptOverviewType addRequiredIndicatorIdsItem(String requiredIndicatorIdsItem) {
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

  public ProcessScriptOverviewType scriptId(String scriptId) {
    this.scriptId = scriptId;
    return this;
  }

  /**
   * unique identifier of the process script
   * @return scriptId
  **/
  @ApiModelProperty(required = true, value = "unique identifier of the process script")
  


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
  **/
  @ApiModelProperty(value = "a script type reference name used to distuingish process scripts from a client perspective, i.e. setup admin pages due to knowledge about type-specific script parameters and required indicators/georesources")


  public String getScriptType() {
    return scriptType;
  }

  public void setScriptType(String scriptType) {
    this.scriptType = scriptType;
  }

  public ProcessScriptOverviewType variableProcessParameters(List<ProcessInputType> variableProcessParameters) {
    this.variableProcessParameters = variableProcessParameters;
    return this;
  }

  public ProcessScriptOverviewType addVariableProcessParametersItem(ProcessInputType variableProcessParametersItem) {
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

