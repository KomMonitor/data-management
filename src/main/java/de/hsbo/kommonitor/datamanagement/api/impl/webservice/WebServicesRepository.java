package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataWebServicesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebServicesRepository extends JpaRepository<MetadataWebServicesEntity, Long> {
    MetadataWebServicesEntity findById(String id);

    boolean existsById(String idd);

    void deleteById(String id);

}
