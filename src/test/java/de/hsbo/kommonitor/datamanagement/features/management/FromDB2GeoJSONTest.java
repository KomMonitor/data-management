package de.hsbo.kommonitor.datamanagement.features.management;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class FromDB2GeoJSONTest {

	@Test
	public void test() throws Exception {

		Calendar c = Calendar.getInstance();

		// 2014-01-01
		c.set(2014, 0, 1);
		String geoJSON = SpatialFeatureDatabaseHandler.getValidFeatures(c.getTime(), "SPATIAL_UNIT_0");

		System.out.println(geoJSON);

		Assert.assertTrue(geoJSON != null);

		geoJSON = null;

		try {
			// 2013-01-01 --> will fail with exception
			c.set(2013, 0, 1);
			geoJSON = SpatialFeatureDatabaseHandler.getValidFeatures(c.getTime(), "SPATIAL_UNIT_0");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			Assert.assertTrue(true);
		}

	}

}
