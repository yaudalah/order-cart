package com.example.belajarspringboot.models;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseModel {
    @Column(nullable = false)
    private Instant createDateTime;

    @Column(nullable = false)
    private Instant updateDateTime;

    @PrePersist
    protected void onCreate() {
        this.createDateTime = new Date().toInstant();
        this.updateDateTime = this.createDateTime;
    }

    @PreUpdate
    private void onUpdate() {
        this.updateDateTime = new Date().toInstant();
    }
}
