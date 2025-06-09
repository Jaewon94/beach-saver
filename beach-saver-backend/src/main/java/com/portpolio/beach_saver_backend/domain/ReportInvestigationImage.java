package com.portpolio.beach_saver_backend.domain;

import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import com.portpolio.beach_saver_backend.domain.enums.ImageType;
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

/** 조사 보고서 이미지 - report(조사 메인 보고서), area(구역별)와 연관 - type: ImageType Enum - S3 파일명, 순서, 설명 등 관리 */
@Entity
@Table(name = "report_investigation_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportInvestigationImage extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "report_id", nullable = false)
  private Report report;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "area_id")
  private ReportInvestigationArea area;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = false)
  private ImageType type;

  @Column(name = "file_name", length = 255, nullable = false)
  private String fileName;

  @Column(columnDefinition = "integer default 0")
  private Integer ord;

  @Column(length = 255)
  private String description;
}
