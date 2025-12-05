package de.hsbo.kommonitor.datamanagement.export;

import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Component("gpkg")
public class GeoPackageService extends AbstractDataStoreExportService{

    private static final Logger LOG = LoggerFactory.getLogger(GeoPackageService.class);

    @Override
    protected String getExportFileSuffix() {
        return ".gpkg";
    }

    @Override
    protected Map<String, Object> getDataStoreParams(File file) {
        Map<String, Object> params = new HashMap<>();
        params.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
        params.put(GeoPkgDataStoreFactory.DATABASE.key, file);
        return params;
    }

}