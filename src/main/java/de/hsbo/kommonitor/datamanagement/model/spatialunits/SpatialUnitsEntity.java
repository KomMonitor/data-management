package de.hsbo.kommonitor.datamanagement.model.spatialunits;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "SpatialUnits")
public class SpatialUnitsEntity {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String spatialUnitId = null;
	
	public SpatialUnitsEntity() {
		
	}
	
	public String getSpatialunitId() {
		return spatialUnitId;
	}
	
	//TODO: andere Attribute

}
