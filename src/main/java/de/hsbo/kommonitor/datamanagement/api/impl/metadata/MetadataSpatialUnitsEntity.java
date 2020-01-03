package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name = "MetadataSpatialUnits")
public class MetadataSpatialUnitsEntity extends AbstractMetadata {

	private int sridEpsg;
	private String nextLowerHierarchyLevel = null;
	private String nextUpperHierarchyLevel = null;
	
	@ManyToMany
	@JoinTable(name = "metadataSpatialUnits_periodsOfValidity", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), inverseJoinColumns = @JoinColumn(name = "period_of_validity_id", referencedColumnName = "periodofvalidityid"))
	private Collection<PeriodOfValidityEntity_spatialUnits> spatialUnitsPeriodsOfValidity;	
	
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
	public Collection<PeriodOfValidityEntity_spatialUnits> getSpatialUnitsPeriodsOfValidity() {
		return spatialUnitsPeriodsOfValidity;
	}
	public void setSpatialUnitsPeriodsOfValidity(
			Collection<PeriodOfValidityEntity_spatialUnits> spatialUnitsPeriodsOfValidity) {
		this.spatialUnitsPeriodsOfValidity = spatialUnitsPeriodsOfValidity;
	}
	
	public void addPeriodOfValidityIfNotExists(PeriodOfValidityEntity_spatialUnits periodEntity) throws Exception {
		if (this.spatialUnitsPeriodsOfValidity == null)
			this.spatialUnitsPeriodsOfValidity = new ArrayList<PeriodOfValidityEntity_spatialUnits>();

			if (!this.spatialUnitsPeriodsOfValidity.contains(periodEntity))
				this.spatialUnitsPeriodsOfValidity.add(periodEntity);
	}
	
	public void removePeriodOfValidityIfExists(PeriodOfValidityEntity_spatialUnits periodEntity) throws Exception {
		if (this.spatialUnitsPeriodsOfValidity == null)
			this.spatialUnitsPeriodsOfValidity = new ArrayList<PeriodOfValidityEntity_spatialUnits>();

			if (this.spatialUnitsPeriodsOfValidity.contains(periodEntity))
				this.spatialUnitsPeriodsOfValidity.remove(periodEntity);
	}
	
	
	
}
