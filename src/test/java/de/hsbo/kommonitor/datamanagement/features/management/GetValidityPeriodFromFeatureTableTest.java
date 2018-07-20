package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;

import de.hsbo.kommonitor.datamanagement.model.AvailablePeriodOfValidityType;

public class GetValidityPeriodFromFeatureTableTest {

	@Test
	public void test() throws IOException, URISyntaxException, CQLException, SQLException {

		AvailablePeriodOfValidityType availablePeriodOfValidity = GeoJSON2DatabaseTool
				.getAvailablePeriodOfValidity("SPATIAL_UNIT_0");

		System.out.println("Found period " + availablePeriodOfValidity);
		
		Assert.assertTrue(availablePeriodOfValidity.getEarliestStartDate()!=null);
		Assert.assertTrue(availablePeriodOfValidity.getEndDate() != null || availablePeriodOfValidity.getEndDate() == null);
	}

}
