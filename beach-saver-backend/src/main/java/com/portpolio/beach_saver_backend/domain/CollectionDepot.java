package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import com.portpolio.beach_saver_backend.domain.enums.CollectionDepotStatus;

@Entity
@Table(name = "collection_depot")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CollectionDepot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_request_report_id")
    private Report collectionRequestReport;

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
}