package de.hsbo.kommonitor.datamanagement.model.organizations;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * organizational unit (group)
 */
@ApiModel(description = "organizational unit (group)")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-12-10T00:30:46.583Z")

public class OrganizationalUnitOverviewType {

    @JsonProperty("organizationalUnitId")
    private String organizationalUnitId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("contact")
    private String contact = null;

    @JsonProperty("description")
    private String description = null;

    public OrganizationalUnitOverviewType organizationalUnitId(String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
        return this;
    }

    /**
     * unique id of this organizational Unit
     *
     * @return organizationalUnitId
     **/
    @ApiModelProperty(required = true, value = "unique id of this organizational Unit")
    @NotNull

    public String getOrganizationalUnitId() {
        return organizationalUnitId;
    }

    public void setOrganizationalUnitId(String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }

    public OrganizationalUnitOverviewType name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of this organizational Unit
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name of this organizational Unit")
    @NotNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrganizationalUnitOverviewType contact(String contact) {
        this.contact = contact;
        return this;
    }

    /**
     * contact information of the person responsible for this group
     *
     * @return contact
     **/
    @ApiModelProperty(required = true, value = "contact information of the person responsible for this group")
    @NotNull

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public OrganizationalUnitOverviewType description(String description) {
        this.description = description;
        return this;
    }

    /**
     * additional information
     *
     * @return description
     **/
    @ApiModelProperty(value = "additional information")

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrganizationalUnitOverviewType organizationalUnitOverviewType = (OrganizationalUnitOverviewType) o;
        return Objects.equals(this.organizationalUnitId, organizationalUnitOverviewType.organizationalUnitId) &&
            Objects.equals(this.name, organizationalUnitOverviewType.name) &&
            Objects.equals(this.contact, organizationalUnitOverviewType.contact) &&
            Objects.equals(this.description, organizationalUnitOverviewType.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationalUnitId, name, contact, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrganizationalUnitOverviewType {\n");

        sb.append("    organizationalUnitId: ").append(toIndentedString(organizationalUnitId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

