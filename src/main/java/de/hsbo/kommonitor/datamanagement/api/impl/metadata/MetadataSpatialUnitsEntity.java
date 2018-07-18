package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import javax.persistence.Entity;

@Entity(name = "MetadataSpatialUnits")
public class MetadataSpatialUnitsEntity extends AbstractMetadata {

	private int sridEpsg;
	private String nextLowerHierarchyLevel = null;
	private String nextUpperHierarchyLevel = null;
	
	
	
	public int getSridEpsg() {
		return sridEpsg;
	}
	public void setSridEpsg(int sridEpsg) {
		this.sridEpsg = sridEpsg;
	}
	public String getNextLowerHierarchyLevel() {
		return nextLowerHierarchyLevel;
	}
	public void setNextLowerHierarchyLevel(String nextLowerHierarchyLevel) {
		this.nextLowerHierarchyLevel = nextLowerHierarchyLevel;
	}
	public String getNextUpperHierarchyLevel() {
		return nextUpperHierarchyLevel;
	}
	public void setNextUpperHierarchyLevel(String nextUpperHierarchyLevel) {
		this.nextUpperHierarchyLevel = nextUpperHierarchyLevel;
	}
	
	
	
}
