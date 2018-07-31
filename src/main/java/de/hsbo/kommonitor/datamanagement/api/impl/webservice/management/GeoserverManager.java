package de.hsbo.kommonitor.datamanagement.api.impl.webservice.management;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import de.hsbo.kommonitor.datamanagement.features.management.ResourceTypeEnum;

/**
 * Implementation of {@link OGCWebServiceManager} that publishes spatial
 * datasets using an instance of GeoServer
 * 
 * @author CDB
 *
 */
public class GeoserverManager implements OGCWebServiceManager {

	private static Properties geoserverProps = null;

	@Override
	public boolean publishDbLayerAsOgcService(String dbTableName, ResourceTypeEnum resourceType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unpublishDbLayer(String dbTableName, ResourceTypeEnum resourceType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getWmsUrl(String dbTableName, ResourceTypeEnum resourceType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWfsUrl(String dbTableName, ResourceTypeEnum resourceType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWcsUrl(String dbTableName, ResourceTypeEnum resourceType) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void parseResourceFiles() throws IOException, FileNotFoundException {

		if (geoserverProps == null)
			;
		geoserverProps = parseProperties(ResourceFileConstants.geoserverPropertiesFileLocation);
	}

	private static Properties parseProperties(String filename) throws IOException, FileNotFoundException {
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String resourcePath = rootPath + filename;

		Properties props = new Properties();
		props.load(new FileInputStream(resourcePath));
		return props;
	}
}
