package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import com.portpolio.beach_saver_backend.domain.common.BaseEntity;
import com.portpolio.beach_saver_backend.domain.enums.ReportStatus;
import com.portpolio.beach_saver_backend.domain.enums.ReportType;

@Entity
@Table(name = "report")
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor 
@Builder
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReportStatus status;

    @Column(name = "gps_lat", precision = 10, scale = 7)
    private BigDecimal gpsLat;

    @Column(name = "gps_lng", precision = 10, scale = 7)
    private BigDecimal gpsLng;

    @Lob
    private String content;
}