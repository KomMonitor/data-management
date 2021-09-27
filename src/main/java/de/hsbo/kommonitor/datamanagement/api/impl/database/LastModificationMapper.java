package de.hsbo.kommonitor.datamanagement.api.impl.database;

import de.hsbo.kommonitor.datamanagement.api.impl.util.DateTimeUtil;
import de.hsbo.kommonitor.datamanagement.model.database.LastModificationOverviewType;

public class LastModificationMapper {

	public static LastModificationOverviewType mapToSwaggerModification(LastModificationEntity lastModifcationInfo) {
		LastModificationOverviewType lastMod = new LastModificationOverviewType();
		
		lastMod.setGeoresources(DateTimeUtil.toOffsetDateTime(lastModifcationInfo.getGeoresources()));
		lastMod.setSpatialUnits(DateTimeUtil.toOffsetDateTime(lastModifcationInfo.getSpatialUnits()));
		lastMod.setIndicators(DateTimeUtil.toOffsetDateTime(lastModifcationInfo.getIndicators()));
		lastMod.setTopics(DateTimeUtil.toOffsetDateTime(lastModifcationInfo.getTopics()));
		lastMod.setProcessScripts(DateTimeUtil.toOffsetDateTime(lastModifcationInfo.getProcessScripts()));
		lastMod.setRoles(DateTimeUtil.toOffsetDateTime(lastModifcationInfo.getRoles()));
		
		return lastMod;
	}
	
	

}
