package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import de.hsbo.kommonitor.datamanagement.api.impl.topics.TopicsHelper;
import de.hsbo.kommonitor.datamanagement.model.georesources.PoiMarkerColorEnum;
import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

@Entity(name = "MetadataGeoresources")
public class MetadataGeoresourcesEntity extends AbstractMetadata {

	private int sridEpsg;
	
	private boolean isPOI;
	
	private PoiMarkerColorEnum poiMarkerColor;
	
	private String poiSymbolBootstrap3Name;

	@ManyToMany
	@JoinTable(name = "metadataGeoresources_topics", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "topicid"))
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

	public void addTopicsIfNotExist(List<String> applicableTopics) throws Exception {
		if (this.georesourcesTopics == null)
			this.georesourcesTopics = new ArrayList<>();

		for (String topic : applicableTopics) {
			/*
			 * add topic if not exists
			 */
			if (!topicAlreadyInTopicReferences(topic, applicableTopics))
				this.georesourcesTopics.add(TopicsHelper.getTopicByName(topic));
		}
	}

	private boolean topicAlreadyInTopicReferences(String topic, List<String> applicableTopics) throws Exception {
		TopicsEntity topicEntity = TopicsHelper.getTopicByName(topic);
		if (applicableTopics.contains(topicEntity))
			return true;
		// if code reaches this line, then the topic is not within the list
		return false;
	}

	public boolean isPOI() {
		return isPOI;
	}

	public void setPOI(boolean isPOI) {
		this.isPOI = isPOI;
	}

	public PoiMarkerColorEnum getPoiMarkerColor() {
		return poiMarkerColor;
	}

	public void setPoiMarkerColor(PoiMarkerColorEnum poiMarkerColor) {
		this.poiMarkerColor = poiMarkerColor;
	}

	public String getPoiSymbolBootstrap3Name() {
		return poiSymbolBootstrap3Name;
	}

	public void setPoiSymbolBootstrap3Name(String poiSymbolBootstrap3Name) {
		this.poiSymbolBootstrap3Name = poiSymbolBootstrap3Name;
	}

}
