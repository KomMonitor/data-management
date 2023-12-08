package de.hsbo.kommonitor.datamanagement.api.impl.users;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsbo.kommonitor.datamanagement.model.legacy.users.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
	UsersEntity findByUserId(String userId);

	UsersEntity findByUserName(String userName);

	boolean existsByUserId(String userId);

	boolean existsByUserName(String userName);

	void deleteByUserId(String userId);

}
