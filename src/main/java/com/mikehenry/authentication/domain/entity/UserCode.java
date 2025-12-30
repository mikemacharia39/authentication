package com.mikehenry.authentication.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SuperBuilder(toBuilder = true)
@Getter
@Entity
@Table(name = "user_code")
public class UserCode extends BaseEntity {

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String code;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used", nullable = false)
    private boolean used = false;

    @Override
    public void prePersist() {
        super.prePersist();
        this.expireCode();
    }

    private void expireCode() {
        this.expiresAt = Instant.now().plus(1, ChronoUnit.DAYS);
    }

    public void markAsUsed() {
        this.used = true;
    }
}
