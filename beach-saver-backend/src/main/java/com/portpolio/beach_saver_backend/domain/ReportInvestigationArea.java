package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;

@Entity
@Table(name = "report_investigation_area")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ReportInvestigationArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(length = 100)
    private String areaName;

    @Column(precision = 10, scale = 7)
    private BigDecimal startLatitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal startLongitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal endLatitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal endLongitude;

    private Integer trashAmountEst;

    @Column(length = 50)
    private String mainTrashType;

    @Column(length = 255)
    private String note;
}