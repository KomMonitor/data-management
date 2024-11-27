package de.hsbo.kommonitor.datamanagement.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserInfoInputType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-11-25T11:39:57.130709600+01:00[Europe/Berlin]")
public class UserInfoInputType implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<String> georesourceFavourites;

  @Valid
  private List<String> indicatorFavourites;

  @Valid
  private List<String> georesourceTopicFavourites;

  @Valid
  private List<String> indicatorTopicFavourites;

  public UserInfoInputType georesourceFavourites(List<String> georesourceFavourites) {
    this.georesourceFavourites = georesourceFavourites;
    return this;
  }

  public UserInfoInputType addGeoresourceFavouritesItem(String georesourceFavouritesItem) {
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
  
  @Schema(name = "georesourceFavourites", description = "list of georesource user favourites", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("georesourceFavourites")
  public List<String> getGeoresourceFavourites() {
    return georesourceFavourites;
  }

  public void setGeoresourceFavourites(List<String> georesourceFavourites) {
    this.georesourceFavourites = georesourceFavourites;
  }

  public UserInfoInputType indicatorFavourites(List<String> indicatorFavourites) {
    this.indicatorFavourites = indicatorFavourites;
    return this;
  }

  public UserInfoInputType addIndicatorFavouritesItem(String indicatorFavouritesItem) {
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
  
  @Schema(name = "indicatorFavourites", description = "list of indicator user favourites", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorFavourites")
  public List<String> getIndicatorFavourites() {
    return indicatorFavourites;
  }

  public void setIndicatorFavourites(List<String> indicatorFavourites) {
    this.indicatorFavourites = indicatorFavourites;
  }

  public UserInfoInputType georesourceTopicFavourites(List<String> georesourceTopicFavourites) {
    this.georesourceTopicFavourites = georesourceTopicFavourites;
    return this;
  }

  public UserInfoInputType addGeoresourceTopicFavouritesItem(String georesourceTopicFavouritesItem) {
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
  
  @Schema(name = "georesourceTopicFavourites", description = "list of georesource topic user favourites", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("georesourceTopicFavourites")
  public List<String> getGeoresourceTopicFavourites() {
    return georesourceTopicFavourites;
  }

  public void setGeoresourceTopicFavourites(List<String> georesourceTopicFavourites) {
    this.georesourceTopicFavourites = georesourceTopicFavourites;
  }

  public UserInfoInputType indicatorTopicFavourites(List<String> indicatorTopicFavourites) {
    this.indicatorTopicFavourites = indicatorTopicFavourites;
    return this;
  }

  public UserInfoInputType addIndicatorTopicFavouritesItem(String indicatorTopicFavouritesItem) {
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
  
  @Schema(name = "indicatorTopicFavourites", description = "list of indicator topic user favourites", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    UserInfoInputType userInfoInputType = (UserInfoInputType) o;
    return Objects.equals(this.georesourceFavourites, userInfoInputType.georesourceFavourites) &&
        Objects.equals(this.indicatorFavourites, userInfoInputType.indicatorFavourites) &&
        Objects.equals(this.georesourceTopicFavourites, userInfoInputType.georesourceTopicFavourites) &&
        Objects.equals(this.indicatorTopicFavourites, userInfoInputType.indicatorTopicFavourites);
  }

  @Override
  public int hashCode() {
    return Objects.hash(georesourceFavourites, indicatorFavourites, georesourceTopicFavourites, indicatorTopicFavourites);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfoInputType {\n");
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

