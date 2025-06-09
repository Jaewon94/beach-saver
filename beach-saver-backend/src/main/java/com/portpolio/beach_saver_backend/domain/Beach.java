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
@Table(name = "beach")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beach extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "org_id", nullable = false)
  private Organization organization;

  @Column(name = "beach_name", length = 50, nullable = false)
  private String beachName;

  @Column(length = 30, nullable = false)
  private String si;

  @Column(length = 30, nullable = false)
  private String guGun;

  @Column(length = 30, nullable = false)
  private String dongEub;

  @Column(length = 30, nullable = false)
  private String workplace;

  @Column(nullable = false, precision = 10, scale = 7)
  private BigDecimal latitude;

  @Column(nullable = false, precision = 10, scale = 7)
  private BigDecimal longitude;
}
