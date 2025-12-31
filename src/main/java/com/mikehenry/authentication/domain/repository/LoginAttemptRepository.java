package com.mikehenry.authentication.domain.repository;

import com.mikehenry.authentication.domain.entity.LoginAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long>, JpaSpecificationExecutor<LoginAttempt> {
    Page<LoginAttempt> findByEmailOrderByIdDesc(String email, Pageable pageable);
}
