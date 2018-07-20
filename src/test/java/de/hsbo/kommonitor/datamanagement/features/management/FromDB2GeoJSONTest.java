package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;

import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;

public class FromDB2GeoJSONTest {

	@Test
	public void test() throws IOException, URISyntaxException, CQLException {

		Calendar c = Calendar.getInstance();

		// 2014-01-01
		c.set(2014, 0, 1);
		String geoJSON = GeoJSON2DatabaseTool.getValidFeatures(c.getTime(), "SPATIAL_UNIT_0");

		System.out.println(geoJSON);

		Assert.assertTrue(geoJSON != null);
	}

}
