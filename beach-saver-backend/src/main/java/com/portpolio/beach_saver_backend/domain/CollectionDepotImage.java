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

/**
 * 집하장/수거 이미지 - collection_depot(집하장/포인트)별로 여러 이미지 첨부 가능 - type: ImageType Enum(COLLECTION_DEPOT,
 * COLLECTION_COMPLETED 등) - S3 파일명, 순서, 설명 등 관리
 */
@Entity
@Table(name = "collection_depot_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionDepotImage extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "collection_depot_id", nullable = false)
  private CollectionDepot collectionDepot;

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
