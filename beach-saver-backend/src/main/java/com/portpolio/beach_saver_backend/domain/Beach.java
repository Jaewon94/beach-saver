package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;

@Entity
@Table(name = "beach")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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