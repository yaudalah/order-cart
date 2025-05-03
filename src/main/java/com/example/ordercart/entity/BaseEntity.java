package com.example.ordercart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createDateTime;

    @Column(nullable = false)
    private LocalDateTime updatedDateTime;

    @Column(name = "create_by", nullable = false, updatable = false)
    private String createBy;
    @Column(name = "update_by", nullable = false)
    private String updateBy;

    @PrePersist
    protected void onCreate() {
        this.setCreateDateTime(LocalDateTime.now());
        this.setUpdatedDateTime(LocalDateTime.now());
        this.setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
        this.setUpdateBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreUpdate
    private void onUpdate() {
        this.setUpdatedDateTime(LocalDateTime.now());
        this.setUpdateBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
