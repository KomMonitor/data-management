package de.hsbo.kommonitor.datamanagement.export;

import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public abstract class AbstractDataStoreExportService implements DataExportService{

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDataStoreExportService.class);

    @Override
    public File createExportFile(SimpleFeatureCollection featureCollection) throws IOException {
        File file = Files.createTempFile("kommonitor-export-", getExportFileSuffix()).toFile();

        DataStore dataStore = null;
        Transaction transaction = Transaction.AUTO_COMMIT;

        try {
            dataStore = DataStoreFinder.getDataStore(getDataStoreParams(file));

            Map<String, String> propertyMapping = new HashMap<>();
            SimpleFeatureType featureType = createSchema(featureCollection.getSchema(), propertyMapping);
            SimpleFeatureCollection transformedFeatureCollection = transformData(featureType, featureCollection, propertyMapping);

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

    private SimpleFeatureType createSchema(SimpleFeatureType schema, Map<String, String> propertyMapping) throws FactoryException {
        List<String> attributes = new ArrayList<>();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(schema.getName());

        CoordinateReferenceSystem crs = schema.getCoordinateReferenceSystem();
        if (crs == null) {
            crs = CRS.decode("EPSG:4326", true);
            builder.setCRS(crs);
        }

        for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
            String sourceName = descriptor.getLocalName();
            String targetName = checkAndGetName(descriptor.getLocalName(), attributes);
            Class<?> binding = descriptor.getType().getBinding();

            if (descriptor instanceof GeometryDescriptor) {
                builder.add(targetName, binding, crs);
            }
            else if (binding.equals(java.time.LocalDate.class) || binding.equals(java.time.LocalDateTime.class))
            {
                builder.add(targetName, java.util.Date.class);
            }
//            else if ("id".equalsIgnoreCase(name)) {
//                builder.add("source_id", binding); // Rename ID
//            }
            else {
                builder.add(targetName, binding);
            }
            attributes.add(sourceName);
            // We use this map to make the source name available when transforming the data and fetching the
            // values from a feature for the source name
            propertyMapping.put(targetName, sourceName);
        }
        return builder.buildFeatureType();
    }

    // Since GeoPackage does not allow identical column names, even with different uppercase and lowercase letters,
    // we have to rename the existing column names.
    private String checkAndGetName(String name, List<String> attributes) {
        if (propertyExists(name, attributes)) {
            return name + "_1";
        } else {
            return name;
        }
    }

    private boolean propertyExists(String name, List<String> attributes) {
        return attributes.stream().anyMatch(a -> a.equalsIgnoreCase(name));
    }

    private SimpleFeatureCollection transformData(SimpleFeatureType targetSchema, SimpleFeatureCollection sourceData, Map<String, String> propertyMapping) {
        List<SimpleFeature> newFeatures = new ArrayList<>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(targetSchema);
        List<String> attributes = new ArrayList<>();

        long featureId = 0;

        try (var iterator = sourceData.features()) {
            while (iterator.hasNext()) {
                SimpleFeature oldFeature = iterator.next();
                featureBuilder.reset();

                for (AttributeDescriptor descriptor : targetSchema.getAttributeDescriptors()) {
                    String name = checkAndGetName(descriptor.getLocalName(), attributes);

//                    String sourceName = targetName;
//                    if ("source_id".equals(targetName)) {
//                        sourceName = (oldFeature.getAttribute("ID") != null) ? "ID" : "id";
//                    }

                    Object value = oldFeature.getAttribute(propertyMapping.get(name));

                    // Convert dates to be GeoPackage conform
                    if (value instanceof java.time.LocalDate ld) {
                        value = java.sql.Timestamp.valueOf(ld.atStartOfDay());
                    }
                    else if (value instanceof java.time.LocalDateTime ld) {
                        value = java.sql.Timestamp.valueOf(ld);
                    }
                    else if (value instanceof java.util.Date d) {
                        value = new java.sql.Timestamp(d.getTime());
                    }
                    if (value != null) {
                        featureBuilder.set(name, value);
                    }
                }
                featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);

                // Handle FID
                // String validId = oldFeature.getProperty("ID") != null ? (String) oldFeature.getProperty("ID").getValue() : java.util.UUID.randomUUID().toString();
                SimpleFeature feature = featureBuilder.buildFeature(String.valueOf(featureId++));
                newFeatures.add(feature);
            }
        }
        return new ListFeatureCollection(targetSchema, newFeatures);
    }

    abstract Map<String,?> getDataStoreParams(File file);

    abstract String getExportFileSuffix();

}
