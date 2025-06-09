package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.QUserTeam;
import com.portpolio.beach_saver_backend.domain.UserTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTeamRepositoryImpl implements UserTeamRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<UserTeam> searchUserTeams(Long userId, Long teamId, String status) {
    QUserTeam userTeam = QUserTeam.userTeam;
    return queryFactory
        .selectFrom(userTeam)
        .where(
            userId != null ? userTeam.user.id.eq(userId) : null,
            teamId != null ? userTeam.team.id.eq(teamId) : null,
            status != null ? userTeam.status.stringValue().eq(status) : null)
        .fetch();
  }
}
