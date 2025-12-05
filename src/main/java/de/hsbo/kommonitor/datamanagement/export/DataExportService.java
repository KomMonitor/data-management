package de.hsbo.kommonitor.datamanagement.export;

import org.geotools.data.simple.SimpleFeatureCollection;

import java.io.File;
import java.io.IOException;

public interface DataExportService {
    File createExportFile(SimpleFeatureCollection featureCollection) throws IOException;
}
