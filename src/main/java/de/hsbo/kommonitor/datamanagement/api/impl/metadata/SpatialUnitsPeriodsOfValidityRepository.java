package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpatialUnitsPeriodsOfValidityRepository extends JpaRepository<PeriodOfValidityEntity_spatialUnits, Long> {
	PeriodOfValidityEntity_spatialUnits findByPeriodOfValidityId(String periodOfValidityId);
    
    boolean existsByPeriodOfValidityId(String periodOfValidityId);
    
    void deleteByPeriodOfValidityId(String periodOfValidityId);

	boolean existsByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

	PeriodOfValidityEntity_spatialUnits findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}