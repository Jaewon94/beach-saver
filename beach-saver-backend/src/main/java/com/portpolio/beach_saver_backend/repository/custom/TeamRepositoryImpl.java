package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.QTeam;
import com.portpolio.beach_saver_backend.domain.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Team> searchTeams(String keyword, Long orgId, Boolean isActive) {
    QTeam team = QTeam.team;
    return queryFactory
        .selectFrom(team)
        .where(
            keyword != null ? team.name.containsIgnoreCase(keyword) : null,
            orgId != null ? team.organization.id.eq(orgId) : null,
            isActive != null
                ? (isActive ? team.deletedAt.isNull() : team.deletedAt.isNotNull())
                : null)
        .fetch();
  }
}
