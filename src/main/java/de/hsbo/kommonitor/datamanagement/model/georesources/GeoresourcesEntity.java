package de.hsbo.kommonitor.datamanagement.model.georesources;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Georesources")
public class GeoresourcesEntity {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String georesourceId = null;
	
	public GeoresourcesEntity() {
		
	}
	
	public String getGeoresourceId() {
		return georesourceId;
	}
	
	//TODO: andere Attribute

}
