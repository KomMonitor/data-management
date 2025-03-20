package de.hsbo.kommonitor.datamanagement.api.impl.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.kommonitor.datamanagement.features.management.KomMonitorFeaturePropertyConstants;

public class DateTimeUtil {

	private static Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

	public static String toISO8601UTC(Date date) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		return df.format(date);
	}

	public static Date fromISO8601UTC(String dateStr) {
		// TimeZone tz = TimeZone.getTimeZone("UTC");
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		// df.setTimeZone(tz);
		//
		// try {
		// return df.parse(dateStr);
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		//
		// return null;

//		logger.info("Try to parse date or dateTime from following string: {}", dateStr);

		/*
		 * 1999-12-31T23:00:00.000+0000
		 * 
		 * if string contains sth like ".000+0000" parsing fails. Hence we
		 * detect dots and take first string
		 */

		dateStr = removeTailingDotSubstrings(dateStr);

		try {
			LocalDate localDate = fromISO8601UTC_localDate(dateStr);

			return fromLocalDate(localDate);
		} catch (Exception e) {

			try {
				LocalDateTime localDateTime = fromISO8601UTC_localDateTime(dateStr);

				return fromLocalDateTime(localDateTime);
			} catch (Exception e2) {
			}
		}

		return null;

	}

	private static String removeTailingDotSubstrings(String dateStr) {
		if (dateStr.contains(".")) {
//			logger.info("Detected dot in dateString. Will remove substring to aquire following string: '{}'",
//					dateStr.split("\\.")[0]);
			// must escape dot in regex
			dateStr = dateStr.split("\\.")[0];
		}
		return dateStr;
	}

	private static Date fromLocalDateTime(LocalDateTime localDateTime) {
		
		Date date = new GregorianCalendar(localDateTime.getYear(), localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth()).getTime();
		return date;
	}

	public static LocalDate fromISO8601UTC_localDate(String dateStr) {

//		logger.info("Try to parse date or dateTime from following string: {}", dateStr);

		/*
		 * 1999-12-31T23:00:00.000+0000
		 * 
		 * if string contains sth like ".000+0000" parsing fails. Hence we
		 * detect dots and take first string
		 */

		dateStr = removeTailingDotSubstrings(dateStr);

		try {
			return LocalDate.parse(dateStr);
		} catch (Exception e) {
			logger.info(
					"Failed to parse dateString '{}' as LocalDate. Check if input String follows ISO8601 rules. Will return null instead.",
					dateStr);
		}

		return null;

	}

	public static LocalDateTime fromISO8601UTC_localDateTime(String dateStr) {

//		logger.info("Try to parse date or dateTime from following string: {}", dateStr);

		/*
		 * 1999-12-31T23:00:00.000+0000
		 * 
		 * if string contains sth like ".000+0000" parsing fails. Hence we
		 * detect dots and take first string
		 */

		dateStr = removeTailingDotSubstrings(dateStr);

		try {
			return LocalDateTime.parse(dateStr);
		} catch (Exception e) {
			logger.info(
					"Failed to parse dateString '{}' as LocalDateTime. Check if input String follows ISO8601 rules. Will return null instead.",
					dateStr);
		}

		return null;

	}

	public static Date fromLocalDate(LocalDate date) {
		
		Date date2 = new GregorianCalendar(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).getTime();
		
		return date2;
	}

	public static LocalDate toLocalDate(Date date) {
		return new java.sql.Date(date.getTime()).toLocalDate();
	}
	
	public static OffsetDateTime toOffsetDateTime(Date date) {
		return date.toInstant()
				  .atOffset(ZoneOffset.UTC);
	}

	public static FeatureCollection fixDateResonseTypes(FeatureCollection features) {
		
		SimpleFeatureType schema = (SimpleFeatureType) features.getSchema();
		SimpleFeatureType schema_modified = fixSchema_dates(schema);		
		
		FeatureIterator featureIterator = features.features();

		DefaultFeatureCollection collection = new DefaultFeatureCollection();			

		while (featureIterator.hasNext()) {
			SimpleFeature feature = (SimpleFeature) featureIterator.next();

			SimpleFeature feature_retyped = SimpleFeatureBuilder.retype(feature, schema_modified);
			feature_retyped = replaceDateProperty(feature_retyped, KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME);
			feature_retyped = replaceDateProperty(feature_retyped, KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
			collection.add(feature_retyped);		
		}

		featureIterator.close();

		return collection;
	}

	private static SimpleFeatureType fixSchema_dates(SimpleFeatureType schema) {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(schema.getName());
		tb.setNamespaceURI(schema.getName().getNamespaceURI());
		tb.setCRS(schema.getCoordinateReferenceSystem());
		tb.addAll(schema.getAttributeDescriptors());
		
		/*
		 * if property already exists then insert it as LocalDate!!!!!! so we must
		 * update the property type to LocalDate
		 */

		AttributeDescriptor attributeDescriptor_startDate = tb
				.get(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME);
		AttributeDescriptor attributeDescriptor_endDate = tb
				.get(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME);
		if (attributeDescriptor_startDate == null) {
			tb.add(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, LocalDate.class);
		} else {

			AttributeTypeBuilder builder = new AttributeTypeBuilder();
			builder.setName("DateType");
			builder.setBinding(LocalDate.class);
			builder.setNillable(true);
			AttributeType buildType = builder.buildType();
			attributeDescriptor_startDate = new AttributeDescriptorImpl(buildType,
					attributeDescriptor_startDate.getName(), attributeDescriptor_startDate.getMinOccurs(),
					attributeDescriptor_startDate.getMaxOccurs(), attributeDescriptor_startDate.isNillable(),
					attributeDescriptor_startDate.getDefaultValue());

			tb.set(KomMonitorFeaturePropertyConstants.VALID_START_DATE_NAME, attributeDescriptor_startDate);
		}

		if (attributeDescriptor_endDate == null) {
			tb.add(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, LocalDate.class);
		} else {

			AttributeTypeBuilder builder = new AttributeTypeBuilder();
			builder.setName("DateType");
			builder.setBinding(LocalDate.class);
			builder.setNillable(true);
			AttributeType buildType = builder.buildType();
			attributeDescriptor_endDate = new AttributeDescriptorImpl(buildType, attributeDescriptor_endDate.getName(),
					attributeDescriptor_endDate.getMinOccurs(), attributeDescriptor_endDate.getMaxOccurs(),
					attributeDescriptor_endDate.isNillable(), attributeDescriptor_endDate.getDefaultValue());

			tb.set(KomMonitorFeaturePropertyConstants.VALID_END_DATE_NAME, attributeDescriptor_endDate);
		}

		return tb.buildFeatureType();
	}

	private static SimpleFeature replaceDateProperty(SimpleFeature feature, String datePropertyName) {
		Object datePropertyValue = feature.getAttribute(datePropertyName);
		
		if(datePropertyValue == null) {
			
		}
		else if (datePropertyValue instanceof Date) {
			LocalDate dateWithoutTime = toLocalDate((Date) datePropertyValue);
			feature.setAttribute(datePropertyName, dateWithoutTime);
		}
		else if (datePropertyValue instanceof LocalDate) {
			
		}
		else {
			
		}
		return feature;
	}

	public static Date getDateMinusOneDay(Date startDate_new) {
		return new Date(startDate_new.getTime() - (1000 * 60 * 60 * 24));
	}
	
	public static Date getDatePlusOneDay(Date startDate_new) {
		return new Date(startDate_new.getTime() + (1000 * 60 * 60 * 24));
	}

	

}
