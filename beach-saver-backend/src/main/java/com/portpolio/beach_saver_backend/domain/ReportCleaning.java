package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;

@Entity
@Table(name = "report_cleaning")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ReportCleaning extends BaseEntity {
    @Id
    private Long reportId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investigation_report_id")
    private Report investigationReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beach_id")
    private Beach beach;

    private LocalDateTime cleaningAt;

    @Column(length = 20)
    private String weather;

    @Column(length = 255)
    private String note;
}