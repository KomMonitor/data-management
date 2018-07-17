package de.hsbo.kommonitor.datamanagement.model.topics;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataGeoresourcesEntity;
import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataIndicatorsEntity;
import de.hsbo.kommonitor.datamanagement.model.users.UsersEntity;

@Entity(name = "Topics")
public class TopicsEntity {
	
	  @Id
	  @GeneratedValue(generator = "UUID")
	  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	  private String topicId = null;

	  private String topicName = null;

	  private String topicDescription = null;
	  
	  /*
	   * default constructor is required by hibernate / jpa
	   */
	  
	  @ManyToMany(mappedBy = "georesourcesTopics")
	    private Collection<MetadataGeoresourcesEntity> metadataGeoresources;
	  
	  @ManyToMany(mappedBy = "indicatorTopics")
	    private Collection<MetadataIndicatorsEntity> metadataIndicators;
	  
	  public TopicsEntity(){
		  
	  }
	  

	  public String getTopicId() {
	    return topicId;
	  }

	  public TopicsEntity topicName(String topicName) {
	    this.topicName = topicName;
	    return this;
	  }

	  public String getTopicName() {
	    return topicName;
	  }

	  public void setTopicName(String topicName) {
	    this.topicName = topicName;
	  }

	  public TopicsEntity topicDescription(String topicDescription) {
	    this.topicDescription = topicDescription;
	    return this;
	  }

	  public String getTopicDescription() {
	    return topicDescription;
	  }

	  public void setTopicDescription(String topicDescription) {
	    this.topicDescription = topicDescription;
	  }

}
