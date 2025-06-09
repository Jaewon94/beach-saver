package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;

@Entity
@Table(name = "report_investigation")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ReportInvestigation extends BaseEntity {
    @Id
    private Long reportId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beach_id")
    private Beach beach;

    private LocalDateTime investigationAt;

    @Column(length = 20)
    private String disasterType;

    @Column(length = 20)
    private String weather;

    @Column(length = 255)
    private String note;
}