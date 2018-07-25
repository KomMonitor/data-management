package de.hsbo.kommonitor.datamanagement.api.impl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

	public static String toISO8601UTC(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		return df.format(date);
	}

	public static Date fromISO8601UTC(String dateStr) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);

		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Date fromLocalDate(LocalDate date) {
		return java.sql.Date.valueOf(date);
	}
	
	public static LocalDate toLocalDate(Date date) {
		return new java.sql.Date(date.getTime()).toLocalDate();
	}

}
