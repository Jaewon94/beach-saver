package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.QUser;
import com.portpolio.beach_saver_backend.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<User> searchUsers(String keyword, String role, String status) {
    QUser user = QUser.user;
    return queryFactory
        .selectFrom(user)
        .where(
            keyword != null
                ? user.nickname
                    .containsIgnoreCase(keyword)
                    .or(user.systemId.containsIgnoreCase(keyword))
                : null,
            role != null ? user.role.stringValue().eq(role) : null,
            status != null ? user.status.stringValue().eq(status) : null)
        .fetch();
  }
}
