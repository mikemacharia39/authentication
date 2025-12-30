package com.mikehenry.authentication.domain.entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

@SuperBuilder
@Entity
@Table(name = "login_attempt")
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, updatable = false)
    private String email;

    @Column(name = "successful", nullable = false)
    private boolean successful;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false, columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Instant dateCreated;

    @PrePersist
    void prePersist() {
        if (this.dateCreated == null) {
            this.dateCreated = Instant.now();
        }
    }
}
