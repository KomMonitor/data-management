package de.hsbo.kommonitor.datamanagement.export;

import de.hsbo.kommonitor.datamanagement.api.impl.database.LastModificationManager;
import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3.xlink.Simple;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GeoPackageService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoPackageService.class);

    public File createGeoPackage(SimpleFeatureCollection featureCollection) throws IOException {
        File file = Files.createTempFile("kommonitor-export-", ".gpkg").toFile();

        Map<String, Object> params = new HashMap<>();
        params.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
        params.put(GeoPkgDataStoreFactory.DATABASE.key, file);

        DataStore dataStore = null;
        Transaction transaction = Transaction.AUTO_COMMIT;

        try {
            dataStore = DataStoreFinder.getDataStore(params);

            SimpleFeatureType featureType = createSchema(featureCollection.getSchema());
            SimpleFeatureCollection transformedFeatureCollection = transformData(featureType, featureCollection);

            dataStore.createSchema(featureType);

            String typeName = dataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

            if (featureSource instanceof SimpleFeatureStore featureStore) {
                featureStore.setTransaction(transaction);
                featureStore.addFeatures(transformedFeatureCollection);
                transaction.commit();
            }
            return file;
        } catch (Exception e) {
            LOG.error("Failed to create GeoPackage", e);
            transaction.rollback();
            throw new IOException("Failed to create GeoPackage", e);
        } finally {
            transaction.close();
            if (dataStore != null) {
                dataStore.dispose();
            }
        }
    }

    private SimpleFeatureType createSchema(SimpleFeatureType schema) throws FactoryException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(schema.getName());

        CoordinateReferenceSystem crs = schema.getCoordinateReferenceSystem();
        if (crs == null) {
            crs = CRS.decode("EPSG:4326", true);
            builder.setCRS(crs);
        }

        for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
            String name = descriptor.getLocalName();
            Class<?> binding = descriptor.getType().getBinding();

            if (descriptor instanceof GeometryDescriptor) {
                builder.add(name, binding, crs);
            }
            else if (binding.equals(java.time.LocalDate.class) || binding.equals(java.time.LocalDateTime.class))
            {
                builder.add(name, java.util.Date.class);
            }
            else {
                builder.add(name, binding);
            }
        }
        return builder.buildFeatureType();
    }

    private SimpleFeatureCollection transformData(SimpleFeatureType targetSchema, SimpleFeatureCollection sourceData) {
        List<SimpleFeature> newFeatures = new ArrayList<>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(targetSchema);

        try (var iterator = sourceData.features()) {
            while (iterator.hasNext()) {
                SimpleFeature oldFeature = iterator.next();
                featureBuilder.reset();

                for (AttributeDescriptor descriptor : targetSchema.getAttributeDescriptors()) {
                    String name = descriptor.getLocalName();

                    Object value = oldFeature.getAttribute(name);

                    // --- DATE VALUE CONVERSION START ---
                    if (value instanceof java.time.LocalDate) {
                        java.time.LocalDate ld = (java.time.LocalDate) value;
                        value = java.sql.Timestamp.valueOf(ld.atStartOfDay());
                    }
                    else if (value instanceof java.time.LocalDateTime) {
                        value = java.sql.Timestamp.valueOf((java.time.LocalDateTime) value);
                    }
                    else if (value instanceof java.util.Date) {
                        value = new java.sql.Timestamp(((java.util.Date) value).getTime());
                    }
                    if (value != null) {
                        featureBuilder.set(name, value);
                    }
                }
                featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);

                // Handle FID
                String validId = oldFeature.getProperty("ID") != null ? (String) oldFeature.getProperty("ID").getValue() : java.util.UUID.randomUUID().toString();
                SimpleFeature feature = featureBuilder.buildFeature(validId);
                newFeatures.add(feature);
            }
        }
        return new ListFeatureCollection(targetSchema, newFeatures);
    }
}