package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;

import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;

import de.hsbo.kommonitor.datamanagement.model.PeriodOfValidityType;
import de.hsbo.kommonitor.datamanagement.model.spatialunits.SpatialUnitPUTInputType;

public class UpdateSpatialUnitsTest {

	@Test
	public void test() throws Exception {

		String geoJSON_Stadtteile_update = new String(
				Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("Stadtteile_Gelsenkirchen_updated.json").toURI())));

		PeriodOfValidityType periodOfValidity = new PeriodOfValidityType();
		periodOfValidity.setStartDate(LocalDate.of(2017, Month.JANUARY, 1));
		periodOfValidity.setEndDate(null);

		SpatialUnitPUTInputType type = new SpatialUnitPUTInputType();
		type.setPeriodOfValidity(periodOfValidity);
		type.setGeoJsonString(geoJSON_Stadtteile_update);
		
		GeoJSON2DatabaseTool.updateSpatialUnitFeatures(type, "SPATIAL_UNIT_0");
		
//		GeoJSON2DatabaseTool.getValidFeatures(date, dbTableName)
		
		Assert.assertTrue(true);
	}

}
