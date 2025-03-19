package de.hsbo.kommonitor.datamanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class ErrorType   {

    private String type = null;

    private String label = null;

    private String message = null;

    public ErrorType type(String type) {
        this.type = type;
        return this;
    }

    /**
     * type of the error
     * @return type
     **/
    @NotNull
    @Schema(name = "type", description = "type of the error", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ErrorType label(String label) {
        this.label = label;
        return this;
    }

    /**
     * label of the error
     * @return label
     **/
    @NotNull
    @Schema(name = "label", description = "label of the error", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ErrorType message(String message) {
        this.message = message;
        return this;
    }

    /**
     * additional information about the error
     * @return message
     **/
    @NotNull
    @Schema(name = "message", description = "additional information about the error", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorType errorType = (ErrorType) o;
        return Objects.equals(this.type, errorType.type) &&
                Objects.equals(this.label, errorType.label) &&
                Objects.equals(this.message, errorType.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, label, message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorType {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    label: ").append(toIndentedString(label)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
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