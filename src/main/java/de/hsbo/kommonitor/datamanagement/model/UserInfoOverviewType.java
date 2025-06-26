package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * UserInfoOverviewType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-26T12:50:04.783434100+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class UserInfoOverviewType implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userInfoId;

  private String keycloakId;

  @Valid
  private List<String> georesourceFavourites = new ArrayList<>();

  @Valid
  private List<String> indicatorFavourites = new ArrayList<>();

  @Valid
  private List<String> georesourceTopicFavourites = new ArrayList<>();

  @Valid
  private List<String> indicatorTopicFavourites = new ArrayList<>();

  public UserInfoOverviewType() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserInfoOverviewType(String userInfoId, String keycloakId, List<String> georesourceFavourites, List<String> indicatorFavourites, List<String> georesourceTopicFavourites, List<String> indicatorTopicFavourites) {
    this.userInfoId = userInfoId;
    this.keycloakId = keycloakId;
    this.georesourceFavourites = georesourceFavourites;
    this.indicatorFavourites = indicatorFavourites;
    this.georesourceTopicFavourites = georesourceTopicFavourites;
    this.indicatorTopicFavourites = indicatorTopicFavourites;
  }

  public UserInfoOverviewType userInfoId(String userInfoId) {
    this.userInfoId = userInfoId;
    return this;
  }

  /**
   * the identifier of the user info
   * @return userInfoId
   */
  @NotNull 
  @Schema(name = "userInfoId", description = "the identifier of the user info", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userInfoId")
  public String getUserInfoId() {
    return userInfoId;
  }

  public void setUserInfoId(String userInfoId) {
    this.userInfoId = userInfoId;
  }

  public UserInfoOverviewType keycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
    return this;
  }

  /**
   * the Keycloak user ID
   * @return keycloakId
   */
  @NotNull 
  @Schema(name = "keycloakId", description = "the Keycloak user ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("keycloakId")
  public String getKeycloakId() {
    return keycloakId;
  }

  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  public UserInfoOverviewType georesourceFavourites(List<String> georesourceFavourites) {
    this.georesourceFavourites = georesourceFavourites;
    return this;
  }

  public UserInfoOverviewType addGeoresourceFavouritesItem(String georesourceFavouritesItem) {
    if (this.georesourceFavourites == null) {
      this.georesourceFavourites = new ArrayList<>();
    }
    this.georesourceFavourites.add(georesourceFavouritesItem);
    return this;
  }

  /**
   * list of georesource user favourites
   * @return georesourceFavourites
   */
  @NotNull 
  @Schema(name = "georesourceFavourites", description = "list of georesource user favourites", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("georesourceFavourites")
  public List<String> getGeoresourceFavourites() {
    return georesourceFavourites;
  }

  public void setGeoresourceFavourites(List<String> georesourceFavourites) {
    this.georesourceFavourites = georesourceFavourites;
  }

  public UserInfoOverviewType indicatorFavourites(List<String> indicatorFavourites) {
    this.indicatorFavourites = indicatorFavourites;
    return this;
  }

  public UserInfoOverviewType addIndicatorFavouritesItem(String indicatorFavouritesItem) {
    if (this.indicatorFavourites == null) {
      this.indicatorFavourites = new ArrayList<>();
    }
    this.indicatorFavourites.add(indicatorFavouritesItem);
    return this;
  }

  /**
   * list of indicator user favourites
   * @return indicatorFavourites
   */
  @NotNull 
  @Schema(name = "indicatorFavourites", description = "list of indicator user favourites", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorFavourites")
  public List<String> getIndicatorFavourites() {
    return indicatorFavourites;
  }

  public void setIndicatorFavourites(List<String> indicatorFavourites) {
    this.indicatorFavourites = indicatorFavourites;
  }

  public UserInfoOverviewType georesourceTopicFavourites(List<String> georesourceTopicFavourites) {
    this.georesourceTopicFavourites = georesourceTopicFavourites;
    return this;
  }

  public UserInfoOverviewType addGeoresourceTopicFavouritesItem(String georesourceTopicFavouritesItem) {
    if (this.georesourceTopicFavourites == null) {
      this.georesourceTopicFavourites = new ArrayList<>();
    }
    this.georesourceTopicFavourites.add(georesourceTopicFavouritesItem);
    return this;
  }

  /**
   * list of georesource topic user favourites
   * @return georesourceTopicFavourites
   */
  @NotNull 
  @Schema(name = "georesourceTopicFavourites", description = "list of georesource topic user favourites", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("georesourceTopicFavourites")
  public List<String> getGeoresourceTopicFavourites() {
    return georesourceTopicFavourites;
  }

  public void setGeoresourceTopicFavourites(List<String> georesourceTopicFavourites) {
    this.georesourceTopicFavourites = georesourceTopicFavourites;
  }

  public UserInfoOverviewType indicatorTopicFavourites(List<String> indicatorTopicFavourites) {
    this.indicatorTopicFavourites = indicatorTopicFavourites;
    return this;
  }

  public UserInfoOverviewType addIndicatorTopicFavouritesItem(String indicatorTopicFavouritesItem) {
    if (this.indicatorTopicFavourites == null) {
      this.indicatorTopicFavourites = new ArrayList<>();
    }
    this.indicatorTopicFavourites.add(indicatorTopicFavouritesItem);
    return this;
  }

  /**
   * list of indicator topic user favourites
   * @return indicatorTopicFavourites
   */
  @NotNull 
  @Schema(name = "indicatorTopicFavourites", description = "list of indicator topic user favourites", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("indicatorTopicFavourites")
  public List<String> getIndicatorTopicFavourites() {
    return indicatorTopicFavourites;
  }

  public void setIndicatorTopicFavourites(List<String> indicatorTopicFavourites) {
    this.indicatorTopicFavourites = indicatorTopicFavourites;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfoOverviewType userInfoOverviewType = (UserInfoOverviewType) o;
    return Objects.equals(this.userInfoId, userInfoOverviewType.userInfoId) &&
        Objects.equals(this.keycloakId, userInfoOverviewType.keycloakId) &&
        Objects.equals(this.georesourceFavourites, userInfoOverviewType.georesourceFavourites) &&
        Objects.equals(this.indicatorFavourites, userInfoOverviewType.indicatorFavourites) &&
        Objects.equals(this.georesourceTopicFavourites, userInfoOverviewType.georesourceTopicFavourites) &&
        Objects.equals(this.indicatorTopicFavourites, userInfoOverviewType.indicatorTopicFavourites);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userInfoId, keycloakId, georesourceFavourites, indicatorFavourites, georesourceTopicFavourites, indicatorTopicFavourites);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfoOverviewType {\n");
    sb.append("    userInfoId: ").append(toIndentedString(userInfoId)).append("\n");
    sb.append("    keycloakId: ").append(toIndentedString(keycloakId)).append("\n");
    sb.append("    georesourceFavourites: ").append(toIndentedString(georesourceFavourites)).append("\n");
    sb.append("    indicatorFavourites: ").append(toIndentedString(indicatorFavourites)).append("\n");
    sb.append("    georesourceTopicFavourites: ").append(toIndentedString(georesourceTopicFavourites)).append("\n");
    sb.append("    indicatorTopicFavourites: ").append(toIndentedString(indicatorTopicFavourites)).append("\n");
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

