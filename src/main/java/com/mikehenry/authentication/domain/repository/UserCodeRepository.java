package com.mikehenry.authentication.domain.repository;

import com.mikehenry.authentication.domain.entity.User;
import com.mikehenry.authentication.domain.entity.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCodeRepository extends JpaRepository<UserCode, Long>, JpaSpecificationExecutor<UserCode> {

    @Query(
            """
            SELECT uc FROM UserCode uc JOIN uc.user u WHERE u.email = :email AND uc.code = :code ORDER BY uc.id DESC
            """
    )
    Optional<UserCode> findLatestUserCode(String email, String code);
}
