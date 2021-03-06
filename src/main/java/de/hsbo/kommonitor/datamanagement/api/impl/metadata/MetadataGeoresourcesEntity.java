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
import de.hsbo.kommonitor.datamanagement.model.georesources.PoiMarkerColorEnum;
import de.hsbo.kommonitor.datamanagement.model.georesources.PoiSymbolColorEnum;
import de.hsbo.kommonitor.datamanagement.model.roles.RolesEntity;

@Entity(name = "MetadataGeoresources")
public class MetadataGeoresourcesEntity extends AbstractMetadata {

	private int sridEpsg;
	
	private boolean isPOI;
	
	private boolean isLOI;
	
	private boolean isAOI;
	
	private String topicReference;
	
	private PoiMarkerColorEnum poiMarkerColor;
	
	private PoiSymbolColorEnum poiSymbolColor;
	
	private String poiSymbolBootstrap3Name;
	
	private String loiColor = null;
	
	private Integer loiWidth = null;

	private String loiDashArrayString = null;
	
	private String aoiColor = null;
	
//	@ManyToMany
//	@JoinTable(name = "metadataGeoresources_periodsOfValidity", joinColumns = @JoinColumn(name = "dataset_id", referencedColumnName = "datasetid"), inverseJoinColumns = @JoinColumn(name = "period_of_validity_id", referencedColumnName = "periodofvalidityid"))
//	private Collection<PeriodOfValidityEntity_georesources> georesourcesPeriodsOfValidity;

	public int getSridEpsg() {
		return sridEpsg;
	}

	public void setSridEpsg(int sridEpsg) {
		this.sridEpsg = sridEpsg;
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

	public PoiSymbolColorEnum getPoiSymbolColor() {
		return poiSymbolColor;
	}

	public void setPoiSymbolColor(PoiSymbolColorEnum poiSymbolColor) {
		this.poiSymbolColor = poiSymbolColor;
	}

	public HashSet<PeriodOfValidityEntity_georesources> getGeoresourcesPeriodsOfValidity() throws IOException, SQLException {
		AvailablePeriodsOfValidityType availablePeriodsOfValidity = SpatialFeatureDatabaseHandler.getAvailablePeriodsOfValidity(this.getDbTableName());

		HashSet<PeriodOfValidityEntity_georesources> hashSet = new HashSet<PeriodOfValidityEntity_georesources>();		
        
        for (PeriodOfValidityType periodOfValidityType : availablePeriodsOfValidity) {
            PeriodOfValidityEntity_georesources periodEntity = new PeriodOfValidityEntity_georesources(periodOfValidityType);
            hashSet.add(periodEntity);
        }        
		
		return hashSet;
	}

	@ManyToMany()
	@JoinTable(name = "metadataGeoresources_roles",
			joinColumns = @JoinColumn(name = "metadatageoresources_id", referencedColumnName = "datasetid"),
			inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "roleid"))
	private Collection<RolesEntity> roles;

	public HashSet<RolesEntity> getRoles() {
		return new HashSet<RolesEntity>(roles);
	}

	public void setRoles(Collection<RolesEntity> roles) {
		this.roles = new HashSet<RolesEntity>(roles);
	}

	public boolean isLOI() {
		return isLOI;
	}

	public void setLOI(boolean isLOI) {
		this.isLOI = isLOI;
	}

	public boolean isAOI() {
		return isAOI;
	}

	public void setAOI(boolean isAOI) {
		this.isAOI = isAOI;
	}

	public String getTopicReference() {
		return topicReference;
	}

	public void setTopicReference(String topicReference) {
		this.topicReference = topicReference;
	}

	public String getLoiColor() {
		return loiColor;
	}

	public void setLoiColor(String loiColor) {
		this.loiColor = loiColor;
	}

	public String getLoiDashArrayString() {
		return loiDashArrayString;
	}

	public void setLoiDashArrayString(String loiDashArrayString) {
		this.loiDashArrayString = loiDashArrayString;
	}

	public String getAoiColor() {
		return aoiColor;
	}

	public void setAoiColor(String aoiColor) {
		this.aoiColor = aoiColor;
	}

	public Integer getLoiWidth() {
		return loiWidth;
	}

	public void setLoiWidth(Integer loiWidth) {
		this.loiWidth = loiWidth;
	}

}
