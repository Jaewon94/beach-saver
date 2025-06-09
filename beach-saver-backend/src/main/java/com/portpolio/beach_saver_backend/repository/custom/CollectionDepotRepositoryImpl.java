package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.CollectionDepot;
import com.portpolio.beach_saver_backend.domain.QCollectionDepot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CollectionDepotRepositoryImpl implements CollectionDepotRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<CollectionDepot> searchDepots(String keyword, String status) {
    QCollectionDepot depot = QCollectionDepot.collectionDepot;
    return queryFactory
        .selectFrom(depot)
        .where(
            keyword != null ? depot.name.containsIgnoreCase(keyword) : null,
            status != null ? depot.status.stringValue().eq(status) : null)
        .fetch();
  }
}
