package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoresourcesPeriodsOfValidityRepository extends JpaRepository<PeriodOfValidityEntity_georesources, Long> {
	PeriodOfValidityEntity_georesources findByPeriodOfValidityId(String periodOfValidityId);
    
    boolean existsByPeriodOfValidityId(String periodOfValidityId);
    
    void deleteByPeriodOfValidityId(String periodOfValidityId);

	boolean existsByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

	List<PeriodOfValidityEntity_georesources> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}