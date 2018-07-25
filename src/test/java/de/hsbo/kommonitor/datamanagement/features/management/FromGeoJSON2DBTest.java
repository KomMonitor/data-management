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

public class FromGeoJSON2DBTest {

	@Test
	public void test() throws IOException, URISyntaxException, CQLException {

		String geoJSON_Stadtteile = new String(
				Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("Stadtteile_Gelsenkirchen.json").toURI())));

		PeriodOfValidityType periodOfValidity = new PeriodOfValidityType();
		periodOfValidity.setStartDate(LocalDate.of(2014, Month.JANUARY, 1));
		periodOfValidity.setEndDate(null);

		String tableName = GeoJSON2DatabaseTool.writeGeoJSONFeaturesToDatabase(ResourceTypeEnum.SPATIAL_UNIT, geoJSON_Stadtteile,
				periodOfValidity, "metadataIndicatorId1234");
		
		Assert.assertTrue(tableName != null);
	}

}
