package de.hsbo.kommonitor.datamanagement.api.impl.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {

    UserInfoEntity findByUserInfoId(String id);

    UserInfoEntity findByKeycloakId(String id);

    boolean existsByUserInfoId(String id);

    boolean existsByKeycloakId(String id);

    void deleteByUserInfoId(String id);

}
