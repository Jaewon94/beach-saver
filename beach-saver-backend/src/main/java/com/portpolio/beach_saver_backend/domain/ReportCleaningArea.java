package com.portpolio.beach_saver_backend.domain;

import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report_cleaning_area")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportCleaningArea extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "report_id")
  private Report report;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "investigation_area_id")
  private ReportInvestigationArea investigationArea;

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

  private Integer trashAmount;

  @Column(length = 50)
  private String mainTrashType;

  @Column(length = 255)
  private String note;
}
