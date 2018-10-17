package de.hsbo.kommonitor.datamanagement.api.impl.webservice.management;

import java.net.MalformedURLException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.IllegalFilterException;
import org.springframework.stereotype.Component;

import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;
import de.hsbo.kommonitor.datamanagement.model.indicators.DefaultClassificationMappingType;

/**
 * Handles the publishment of spatial datasets as OGC web services such as WMS,
 * WFS, WCS.
 * 
 * It makes use of the name of a database table containig the spatial features.
 * 
 * @author CDB
 *
 */
@Component
public interface OGCWebServiceManager {

	/**
	 * publish the spatial features of the submitted database table as OGC web
	 * services.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @param title
	 *            the title of the layer that will be created
	 * @param defaultStyleName the name of the published style that will be used as default style
	 * @param resourceType
	 *            the type of spatial features within the KomMonitor project
	 * @return true, if the publishing was successful
	 * @throws Exception
	 */
	public boolean publishDbLayerAsOgcService(String dbTableName, String title, String defaultStyleName, ResourceTypeEnum resourceType)
			throws Exception;

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
	public boolean unpublishDbLayer(String dbTableName, ResourceTypeEnum resourceType) throws Exception;

	/**
	 * Claim the URL of the WMS associated to the spatial dataset.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @return the WMS URL or null if no such service exists
	 */
	public String getWmsUrl(String dbTableName);

	/**
	 * Claim the URL of the WFS associated to the spatial dataset.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @return the WFS URL or null if no such service exists
	 */
	public String getWfsUrl(String dbTableName);

	/**
	 * Claim the URL of the WCS associated to the spatial dataset.
	 * 
	 * @param dbTableName
	 *            the name of the database table that contains the spatial
	 *            features
	 * @return the WCS URL or null if no such service exists
	 */
	public String getWcsUrl(String dbTableName);

	/**
	 * create a style (e.g. as SLD) for the combination of datasetTitle and
	 * mostCurrentDate with the help of the indicatorValues and
	 * defaultClassification parameters
	 * 
	 * @param datasetTitle
	 *            the title of the associated layer
	 * @param validFeatures
	 *            the features containing the numeric values for the specified year, which will be used
	 *            to compute the natural breaks classification
	 * @param defaultClassificationMappingType
	 *            parameters for classification process, i.e. number of classes
	 *            and colors
	 * @param targetPropertyName
	 *            the target property, which shall be used to apply the style.
	 *            it corresponds to the most current indicator date
	 * @return the name of the created and published style
	 * @throws TransformerException 
	 * @throws MalformedURLException 
	 * @throws IllegalFilterException 
	 * @throws FactoryRegistryException 
	 */
	public String createAndPublishStyle(String datasetTitle, FeatureCollection validFeatures,
			DefaultClassificationMappingType defaultClassificationMappingType, String targetPropertyName) throws TransformerException, FactoryRegistryException, IllegalFilterException, MalformedURLException;

}
