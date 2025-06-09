package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 관리자(행정 담당자) 특화 정보 */
@Entity
@Table(name = "user_admin")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdmin {
  @Id private Long userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(length = 20)
  private String workCity;

  @Column(length = 40)
  private String workPlace;

  @Column(length = 20)
  private String department;

  @Column(length = 20)
  private String position;

  @Column(length = 20, unique = true)
  private String contact;
}
