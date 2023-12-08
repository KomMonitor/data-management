package de.hsbo.kommonitor.datamanagement.model.legacy.indicators;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * DefaultClassificationMappingItemType
 */

@javax.annotation.Generated(value = "de.prospectiveharvest.codegen.PHServerGenerator", date = "2018-10-16T10:32:04.949+02:00")

@Entity(name = "DefaultClassificationMappingItems")
public class DefaultClassificationMappingItemType   {
	
	 @Id
	  @GeneratedValue(generator = "UUID")
	  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	  private String mappingId = null;
	
  @JsonProperty("defaultColorAsHex")
  private String defaultColorAsHex = null;

  @JsonProperty("defaultCustomRating")
  private String defaultCustomRating = null;

  public DefaultClassificationMappingItemType defaultColorAsHex(String defaultColorAsHex) {
    this.defaultColorAsHex = defaultColorAsHex;
    return this;
  }
  
  @ManyToOne() 
  private MetadataIndicatorsEntity associatedIndicatorMetadata;

   /**
   * the default color for the specified value interval as hex string inclusive leading #, i.e. '#ffffff'
   * @return defaultColorAsHex
  **/
  @ApiModelProperty(required = true, value = "the default color for the specified value interval as hex string inclusive leading #, i.e. '#ffffff'")
  public String getDefaultColorAsHex() {
    return defaultColorAsHex;
  }

  public void setDefaultColorAsHex(String defaultColorAsHex) {
    this.defaultColorAsHex = defaultColorAsHex;
  }

  public DefaultClassificationMappingItemType defaultCustomRating(String defaultCustomRating) {
    this.defaultCustomRating = defaultCustomRating;
    return this;
  }

   /**
   * the default custom rating string for the specified value interval, i.e. 'very high'/'very low' or 'good'/'bad'
   * @return defaultCustomRating
  **/
  @ApiModelProperty(required = true, value = "the default custom rating string for the specified value interval, i.e. 'very high'/'very low' or 'good'/'bad'")
  public String getDefaultCustomRating() {
    return defaultCustomRating;
  }

  public void setDefaultCustomRating(String defaultCustomRating) {
    this.defaultCustomRating = defaultCustomRating;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultClassificationMappingItemType defaultClassificationMappingItemType = (DefaultClassificationMappingItemType) o;
    return Objects.equals(this.defaultColorAsHex, defaultClassificationMappingItemType.defaultColorAsHex) &&
        Objects.equals(this.defaultCustomRating, defaultClassificationMappingItemType.defaultCustomRating);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultColorAsHex, defaultCustomRating);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefaultClassificationMappingItemType {\n");
    
    sb.append("    defaultColorAsHex: ").append(toIndentedString(defaultColorAsHex)).append("\n");
    sb.append("    defaultCustomRating: ").append(toIndentedString(defaultCustomRating)).append("\n");
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

