package de.hsbo.kommonitor.datamanagement.export;

import de.hsbo.kommonitor.datamanagement.api.impl.exception.ResourceNotFoundException;
import de.hsbo.kommonitor.datamanagement.api.impl.indicators.IndicatorsManager;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Component
public class ExportManager {

    private static final Logger LOG = LoggerFactory.getLogger(ExportManager.class);

    @Autowired
    private ExportServiceRepository exportServiceRepository;

    public File exportFeatureCollection(SimpleFeatureCollection featureCollection, String format) throws ResourceNotFoundException, IOException {
        Optional<DataExportService> exportService = exportServiceRepository.getService(format);

        if (exportService.isEmpty()) {
            LOG.error("No export service found for format '{}'.", format);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND.value(), "Export format " + format +  "is not supported");
        }

        return exportService.get().createExportFile(featureCollection);
    }
}
