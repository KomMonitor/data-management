package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import de.hsbo.kommonitor.datamanagement.features.management.SpatialFeatureDatabaseHandler;
import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodsOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Entity(name = "MetadataSpatialUnits")
public class MetadataSpatialUnitsEntity extends AbstractMetadata {

	private int sridEpsg;
	private String nextLowerHierarchyLevel = null;
	private String nextUpperHierarchyLevel = null;

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
	public HashSet<PeriodOfValidityEntity_spatialUnits> getSpatialUnitsPeriodsOfValidity() throws IOException, SQLException {
		AvailablePeriodsOfValidityType availablePeriodsOfValidity = SpatialFeatureDatabaseHandler.getAvailablePeriodsOfValidity(this.getDbTableName());

		HashSet<PeriodOfValidityEntity_spatialUnits> hashSet = new HashSet<PeriodOfValidityEntity_spatialUnits>();		
        
        for (PeriodOfValidityType periodOfValidityType : availablePeriodsOfValidity) {
        	PeriodOfValidityEntity_spatialUnits periodEntity = new PeriodOfValidityEntity_spatialUnits(periodOfValidityType);
            hashSet.add(periodEntity);
        }        
		
		return hashSet;
	}


}
