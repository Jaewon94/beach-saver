package com.portpolio.beach_saver_backend.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import com.portpolio.beach_saver_backend.domain.enums.CollectionDepotStatus;
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

@Entity
@Table(name = "collection_depot")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class CollectionDepot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private CollectionDepotStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id")
    private User collector;

    private LocalDateTime collectionCompletedAt;

    private Integer collectedAmount;

    @Column(length = 255)
    private String note;

    private LocalDateTime deletedAt;
}