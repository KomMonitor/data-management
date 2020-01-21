package de.hsbo.kommonitor.datamanagement.api.impl.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeUtil {
	
	private static Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

	public static String toISO8601UTC(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		return df.format(date);
	}

	public static Date fromISO8601UTC(String dateStr) {
//		TimeZone tz = TimeZone.getTimeZone("UTC");
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
//		df.setTimeZone(tz);
//
//		try {
//			return df.parse(dateStr);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		return null;
		
		try {
			LocalDate localDate = fromISO8601UTC_localDate(dateStr);
			
			return fromLocalDate(localDate);
		} catch (Exception e) {
			logger.info("Failed to parse dateString '{}' as LocalDate. Check if input String follows ISO8601 rules. Will return null instead.");
			
			try {
				LocalDateTime localDateTime = fromISO8601UTC_localDateTime(dateStr);
				
				return fromLocalDateTime(localDateTime);
			} catch (Exception e2) {
				logger.info("Failed to parse dateString '{}' as LocalDateTime. Check if input String follows ISO8601 rules. Will return null instead.");
			}
		}
		
		return null;
		
	}
	
	private static Date fromLocalDateTime(LocalDateTime localDateTime) {
		return java.sql.Date.valueOf(localDateTime.toLocalDate());
	}

	public static LocalDate fromISO8601UTC_localDate(String dateStr) {	
		
		try {
			return LocalDate.parse(dateStr);
		} catch (Exception e) {
			logger.info("Failed to parse dateString '{}' as LocalDate. Check if input String follows ISO8601 rules. Will return null instead.");
		}

		return null;

	}
	
	public static LocalDateTime fromISO8601UTC_localDateTime(String dateStr) {		

		try {
			return LocalDateTime.parse(dateStr);
		} catch (Exception e) {
			logger.info("Failed to parse dateString '{}' as LocalDateTime. Check if input String follows ISO8601 rules. Will return null instead.");
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
