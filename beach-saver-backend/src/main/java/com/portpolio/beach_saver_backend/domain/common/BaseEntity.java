package com.portpolio.beach_saver_backend.domain.common;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** 모든 엔티티의 공통 필드(생성/수정 일시, 생성/수정자) */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  protected LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  protected LocalDateTime updatedAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false, length = 50)
  protected String createdBy;

  @LastModifiedBy
  @Column(name = "updated_by", length = 50)
  protected String updatedBy;
}
