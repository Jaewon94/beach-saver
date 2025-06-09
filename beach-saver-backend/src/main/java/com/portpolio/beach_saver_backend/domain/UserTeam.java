package com.portpolio.beach_saver_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.portpolio.beach_saver_backend.domain.enums.UserStatus;

/**
 * 사용자-팀 이력
 */
@Entity
@Table(name = "user_team")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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
    private UserStatus status = UserStatus.ACTIVE;
}