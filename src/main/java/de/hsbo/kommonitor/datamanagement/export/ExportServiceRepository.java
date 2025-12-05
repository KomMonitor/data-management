package de.hsbo.kommonitor.datamanagement.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExportServiceRepository {

    @Autowired
    private Map<String, DataExportService> exportServices;

    public Optional<DataExportService> getService(String name){
        return Optional.ofNullable(exportServices.get(name));
    }


    public List<DataExportService> getAll() {
        return new ArrayList<>(exportServices.values());
    }
}
