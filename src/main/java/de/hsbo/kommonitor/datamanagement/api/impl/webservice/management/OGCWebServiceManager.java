package de.hsbo.kommonitor.datamanagement.api.impl.webservice.management;

import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;

/**
 * Handles the publishment of spatial datasets as OGC web services such as WMS,
 * WFS, WCS.
 * 
 * It makes use of the name of a database table containig the spatial features.
 * 
 * @author CDB
 *
 */
public interface OGCWebServiceManager {

	/**
	 * publish the spatial features of the submitted database table as OGC web
	 * services.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @param resourceType
	 *            the type of spatial features within the KomMonitor project
	 * @return true, if the publishing was successful
	 */
	public boolean publishDbLayerAsOgcService(String dbTableName, ResourceTypeEnum resourceType);

	/**
	 * Unpublish the spatial dataset. After calling this operation the dataset
	 * will no longer be available as OGC service.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @param resourceType
	 *            the type of spatial features within the KomMonitor project
	 * @return
	 */
	public boolean unpublishDbLayer(String dbTableName, ResourceTypeEnum resourceType);

	/**
	 * Claim the URL of the WMS associated to the spatial dataset.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @param resourceType
	 *            the type of spatial features within the KomMonitor project
	 * @return the WMS URL or null if no such service exists
	 */
	public String getWmsUrl(String dbTableName, ResourceTypeEnum resourceType);

	/**
	 * Claim the URL of the WFS associated to the spatial dataset.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @param resourceType
	 *            the type of spatial features within the KomMonitor project
	 * @return the WFS URL or null if no such service exists
	 */
	public String getWfsUrl(String dbTableName, ResourceTypeEnum resourceType);

	/**
	 * Claim the URL of the WCS associated to the spatial dataset.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @param resourceType
	 *            the type of spatial features within the KomMonitor project
	 * @return the WCS URL or null if no such service exists
	 */
	public String getWcsUrl(String dbTableName, ResourceTypeEnum resourceType);

}
