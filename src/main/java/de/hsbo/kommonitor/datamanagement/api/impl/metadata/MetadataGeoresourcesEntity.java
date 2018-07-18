package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

@Entity(name = "MetadataGeoresources")
public class MetadataGeoresourcesEntity extends AbstractMetadata {

	
	private int sridEpsg;


	@ManyToMany
	@JoinTable(
			name = "metadataGeoresources_topics",
			joinColumns = @JoinColumn(
			          name = "dataset_id", referencedColumnName = "datasetid"), 
			        inverseJoinColumns = @JoinColumn(
			          name = "topic_id", referencedColumnName = "topicid"))
			    private Collection<TopicsEntity> georesourcesTopics;			

	public int getSridEpsg() {
		return sridEpsg;
	}

	public void setSridEpsg(int sridEpsg) {
		this.sridEpsg = sridEpsg;
	}

	public Collection<TopicsEntity> getGeoresourcesTopics() {
		return georesourcesTopics;
	}

	public void setGeoresourcesTopics(Collection<TopicsEntity> georesourcesTopics) {
		this.georesourcesTopics = georesourcesTopics;
	}
	



}
