package de.hsbo.kommonitor.datamanagement.model.organizations;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * organizational unit (group)
 */
@ApiModel(description = "organizational unit (group)")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2022-01-04T19:28:20.077Z")

public class OrganizationalUnitInputType {

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("contact")
    private String contact = null;

    @JsonProperty("description")
    private String description = null;

    public OrganizationalUnitInputType name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name of this organizational Unit
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name of this organizational Unit")

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrganizationalUnitInputType contact(String contact) {
        this.contact = contact;
        return this;
    }

    /**
     * contact information of the person responsible for this group
     *
     * @return contact
     **/
    @ApiModelProperty(required = true, value = "contact information of the person responsible for this group")

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public OrganizationalUnitInputType description(String description) {
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
        OrganizationalUnitInputType organizationalUnitInputType = (OrganizationalUnitInputType) o;
        return Objects.equals(this.name, organizationalUnitInputType.name) &&
            Objects.equals(this.contact, organizationalUnitInputType.contact) &&
            Objects.equals(this.description, organizationalUnitInputType.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, contact, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrganizationalUnitInputType {\n");

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

