package de.hsbo.kommonitor.datamanagement.api.impl.webservice;

import de.hsbo.kommonitor.datamanagement.api.impl.metadata.MetadataWebServicesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebServicesRepository extends JpaRepository<MetadataWebServicesEntity, Long> {
    MetadataWebServicesEntity findById(String id);

    MetadataWebServicesEntity findByTile(String title);

    boolean existsById(String id);

    boolean existsByTitle(String title);

    void deleteById(String id);

}
