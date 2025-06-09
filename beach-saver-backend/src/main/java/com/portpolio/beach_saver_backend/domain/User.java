package com.portpolio.beach_saver_backend.domain;


import java.time.LocalDateTime;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import com.portpolio.beach_saver_backend.domain.enums.UserRole;
import com.portpolio.beach_saver_backend.domain.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자
 */
@Entity
@Table(name = "user")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_id", nullable = false, unique = true, length = 50)
    private String systemId;

    @Column(length = 50)
    private String nickname;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void suspend() {
        this.status = UserStatus.SUSPENDED;
    }
    public void withdraw() {
        this.status = UserStatus.WITHDRAWN;
        this.deletedAt = LocalDateTime.now();
    }
}