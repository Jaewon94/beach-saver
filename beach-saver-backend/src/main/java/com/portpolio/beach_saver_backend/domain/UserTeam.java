package com.portpolio.beach_saver_backend.domain;

import java.time.LocalDateTime;
import com.portpolio.beach_saver_backend.domain.enums.UserTeamStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자-팀 이력
 */
@Entity
@Table(name = "user_team")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserTeam {
    @EmbeddedId
    private UserTeamId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "left_at")
    private LocalDateTime leftAt;

    @Column(name = "role_in_team", length = 20)
    private String roleInTeam;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private UserTeamStatus status = UserTeamStatus.ACTIVE;
}