package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.topics.TopicsEntity;

public interface SpatialUnitsPeriodsOfValidityRepository extends JpaRepository<PeriodOfValidityEntity_spatialUnits, Long> {
	TopicsEntity findByPeriodOfValidityId(String periodOfValidityId);
    
    boolean existsByPeriodOfValidityId(String periodOfValidityId);
    
    void deleteByPeriodOfValidityId(String periodOfValidityId);

	boolean existsByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}