package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.net.URISyntaxException;

import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;

public class DeleteFeatureTableTest {

	@Test
	public void test() throws IOException, URISyntaxException, CQLException {

		SpatialFeatureDatabaseHandler.deleteFeatureTable(ResourceTypeEnum.SPATIAL_UNIT, "SPATIAL_UNIT_0");
		
		Assert.assertTrue(true);
	}

}
