package de.hsbo.kommonitor.datamanagement.model.legacy.indicators;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * IndicatorPATCHInputType
 */
public class IndicatorPATCHInputType {

    @JsonProperty("allowedRoles")
    private List<String> allowedRoles = new ArrayList<>();

    public IndicatorPATCHInputType allowedRoles(List<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
        return this;
    }

    public IndicatorPATCHInputType addAllowedRolesItem(String allowedRolesItem) {
        this.allowedRoles.add(allowedRolesItem);
        return this;
    }

    /**
     * list of role identifiers that have read access rights for this dataset
     *
     * @return allowedRoles
     **/
    @ApiModelProperty(required = true, value = "list of role identifiers that have read access rights for this dataset")
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
        IndicatorPATCHInputType indicatorPUTInputType = (IndicatorPATCHInputType) o;
        return Objects.equals(this.allowedRoles, indicatorPUTInputType.allowedRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedRoles);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IndicatorPUTInputType {\n");
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

