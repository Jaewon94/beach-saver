package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 근로자(조사자/청소자/수거자) 특화 정보 */
@Entity
@Table(name = "user_worker")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWorker {
  @Id private Long userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  private Double vehicleCapacity;

  private LocalDate birth;

  private LocalDate startDate;

  private LocalDate endDate;
}
