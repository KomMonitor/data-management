package de.hsbo.kommonitor.datamanagement.api.impl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SimplifyGeometriesEnum {

	ORIGINAL("original"),

	WEAK("0.0000001"),

	MEDIUM("0.000001"),

	STRONG("0.00001");

	private String simplificationValue;

	SimplifyGeometriesEnum(String value) {
		this.simplificationValue = value;
	}

	public String getValue() {
		return simplificationValue;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(name());
	}

	public static SimplifyGeometriesEnum[] names() {
		SimplifyGeometriesEnum[] elements = new SimplifyGeometriesEnum[] { SimplifyGeometriesEnum.ORIGINAL,
				SimplifyGeometriesEnum.WEAK, SimplifyGeometriesEnum.MEDIUM, SimplifyGeometriesEnum.STRONG };

		return elements;
	}

	@JsonCreator
	public static SimplifyGeometriesEnum fromName(String name) {
		for (SimplifyGeometriesEnum b : SimplifyGeometriesEnum.names()) {
			if (String.valueOf(b.name()).equalsIgnoreCase(name)) {
				return b;
			}
		}
		Logger logger = LoggerFactory.getLogger(SimplifyGeometriesEnum.class);
		logger.info(
				"The submitted parameter value for parameter 'simplifiyFeatures' was set to the unrecognizable value of {}. Allowed names are {}. Will use 'ORIGINAL' as default.",
				name, SimplifyGeometriesEnum.names());
		return ORIGINAL;
	}
}
