package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.*;

@Entity(name = "MetadataSpatialUnits")
public class MetadataSpatialUnitsEntity extends AbstractMetadata {

	private int sridEpsg;
	private String nextLowerHierarchyLevel = null;
	private String nextUpperHierarchyLevel = null;

	@ManyToMany
	@JoinTable(name = "metadataSpatialUnits_periodsOfValidity", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), inverseJoinColumns = @JoinColumn(name = "period_of_validity_id", referencedColumnName = "periodofvalidityid"))
	private Collection<PeriodOfValidityEntity_spatialUnits> spatialUnitsPeriodsOfValidity;

	@ManyToMany()
	@JoinTable(name = "metadataSpatialUnits_roles",
			joinColumns = @JoinColumn(name = "metadataspatialunits_id", referencedColumnName = "datasetid"),
			inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "roleid"))
	private Collection<RolesEntity> roles;

	public HashSet<RolesEntity> getRoles() {
		return new HashSet<RolesEntity>(roles);
	}

	public void setRoles(Collection<RolesEntity> roles) {
		this.roles = new HashSet<RolesEntity>(roles);
	}

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
	public HashSet<PeriodOfValidityEntity_spatialUnits> getSpatialUnitsPeriodsOfValidity() {
		return new HashSet<PeriodOfValidityEntity_spatialUnits>(spatialUnitsPeriodsOfValidity);
	}
	public void setSpatialUnitsPeriodsOfValidity(
			Collection<PeriodOfValidityEntity_spatialUnits> spatialUnitsPeriodsOfValidity) {
		this.spatialUnitsPeriodsOfValidity = new HashSet<PeriodOfValidityEntity_spatialUnits>(spatialUnitsPeriodsOfValidity);
	}

	public void addPeriodOfValidityIfNotExists(PeriodOfValidityEntity_spatialUnits periodEntity) throws Exception {
		if (this.spatialUnitsPeriodsOfValidity == null)
			this.spatialUnitsPeriodsOfValidity = new HashSet<PeriodOfValidityEntity_spatialUnits>();

			if (!this.spatialUnitsPeriodsOfValidity.contains(periodEntity))
				this.spatialUnitsPeriodsOfValidity.add(periodEntity);
	}

	public void removePeriodOfValidityIfExists(PeriodOfValidityEntity_spatialUnits periodEntity) throws Exception {
		if (this.spatialUnitsPeriodsOfValidity == null)
			this.spatialUnitsPeriodsOfValidity = new HashSet<PeriodOfValidityEntity_spatialUnits>();

			if (this.spatialUnitsPeriodsOfValidity.contains(periodEntity))
				this.spatialUnitsPeriodsOfValidity.remove(periodEntity);
	}

	public void setPeriodsOfValidity(ArrayList<PeriodOfValidityEntity_spatialUnits> periods) {
		this.spatialUnitsPeriodsOfValidity = new HashSet<PeriodOfValidityEntity_spatialUnits>(periods);
	}



}
