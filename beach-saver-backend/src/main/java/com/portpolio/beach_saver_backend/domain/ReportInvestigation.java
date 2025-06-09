package com.portpolio.beach_saver_backend.domain;

import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report_investigation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportInvestigation extends BaseEntity {
  @Id private Long reportId;

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
