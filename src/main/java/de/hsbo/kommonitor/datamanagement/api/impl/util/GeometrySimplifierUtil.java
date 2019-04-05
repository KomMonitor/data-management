package de.hsbo.kommonitor.datamanagement.api.impl.util;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

public class GeometrySimplifierUtil {

	private static Logger logger = LoggerFactory.getLogger(GeometrySimplifierUtil.class);

	/**
	 * Simplifies gemoetries based on Geotools implementation of Douglas
	 * Peuckker algorithm. The maxDistance value, used to simplify geometries,
	 * is taken from API parameter.
	 * 
	 * @param features
	 *            input features that shall be simplifed
	 * @return simplified features
	 */
	public static FeatureCollection simplifyGeometriesAccordingToParameter(FeatureCollection features,
			String simplifyGeometries) {
		
		logger.info("Performing geometry simplification process according to parameter simplifyGeometries='{}'", simplifyGeometries);
		
		SimplifyGeometriesEnum simplificationType = SimplifyGeometriesEnum.fromName(simplifyGeometries);
		logger.info("Instantiated geometry simplification type {} with simplificiation value {}", simplificationType.toString(), simplificationType.getValue());

		if (simplifyGeometries.equalsIgnoreCase(SimplifyGeometriesEnum.ORIGINAL.toString())) {
			logger.info(
					"According to parameter simplifyGeometries='{}', feature geometries will not be simplifed and returned in original state.",
					simplifyGeometries);
			return features;
		}	

		FeatureIterator featureIterator = features.features();

		DefaultFeatureCollection collection = new DefaultFeatureCollection();

		while (featureIterator.hasNext()) {
			SimpleFeature feature = (SimpleFeature) featureIterator.next();

			Geometry defaultGeometry = (Geometry) feature.getDefaultGeometry();
			Geometry simplifiedGeometry = DouglasPeuckerSimplifier.simplify(defaultGeometry, Double.parseDouble(simplificationType.getValue()));

			feature.setDefaultGeometry(simplifiedGeometry);

			collection.add(feature);
		}

		featureIterator.close();

		return collection;
	}

}
