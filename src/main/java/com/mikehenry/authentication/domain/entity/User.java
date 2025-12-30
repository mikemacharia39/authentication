package com.mikehenry.authentication.domain.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "user_key", nullable = false, unique = true, updatable = false)
    private String userKey;
    @Column(name = "has_verified_email", nullable = false)
    private boolean hasVerifiedEmail;

    @Override
    public void prePersist() {
        super.prePersist();
        userKey = UUID.randomUUID().toString();
        hasVerifiedEmail = false;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
