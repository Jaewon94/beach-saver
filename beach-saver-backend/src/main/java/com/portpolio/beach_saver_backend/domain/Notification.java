package com.portpolio.beach_saver_backend.domain;

import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import com.portpolio.beach_saver_backend.domain.enums.NotificationStatus;
import com.portpolio.beach_saver_backend.domain.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private NotificationType type;

    @Column(length = 100)
    private String title;

    @Lob
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private NotificationStatus status;
}