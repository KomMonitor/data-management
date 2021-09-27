package de.hsbo.kommonitor.datamanagement.api.impl.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LastModificationRepository extends JpaRepository<LastModificationEntity, Long> {
	LastModificationEntity findById(String id);

	boolean existsById(String id);

	void deleteById(String id);

}
