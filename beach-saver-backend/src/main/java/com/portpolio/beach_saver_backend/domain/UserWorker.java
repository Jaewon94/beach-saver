package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * 근로자(조사자/청소자/수거자) 특화 정보
 */
@Entity
@Table(name = "user_worker")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserWorker {
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private Double vehicleCapacity;

    private LocalDate birth;

    private LocalDate startDate;

    private LocalDate endDate;
}