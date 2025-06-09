package com.portpolio.beach_saver_backend.domain;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * user_team 복합키
 */
@Embeddable
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class UserTeamId implements Serializable {
    private Long userId;
    private Long teamId;
    private LocalDateTime joinedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTeamId that)) return false;
        return Objects.equals(userId, that.userId)
                && Objects.equals(teamId, that.teamId)
                && Objects.equals(joinedAt, that.joinedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, teamId, joinedAt);
    }
}