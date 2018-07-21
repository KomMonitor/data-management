package de.hsbo.kommonitor.datamanagement.features.management;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputType;
import de.hsbo.kommonitor.datamanagement.model.indicators.IndicatorPOSTInputTypeIndicatorValues;

public class Indicator2DBTest {

	@Test
	public void test() throws IOException, URISyntaxException, CQLException, SQLException {

		String jsonIndicatorMapping = new String(
				Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("TestIndicator.json").toURI())));

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		IndicatorPOSTInputType indicator = mapper.readValue(jsonIndicatorMapping, IndicatorPOSTInputType.class);

		List<IndicatorPOSTInputTypeIndicatorValues> indicatorValues = indicator.getIndicatorValues();

		String tableName = Indicator2Database.writeIndicatorsToDatabase(indicatorValues, "metadataIndicatorId1234");

		Assert.assertTrue(tableName != null);
	}

}
