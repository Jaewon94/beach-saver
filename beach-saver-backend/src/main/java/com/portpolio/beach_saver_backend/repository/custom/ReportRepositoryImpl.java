package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.QReport;
import com.portpolio.beach_saver_backend.domain.Report;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Report> searchReports(String keyword, String type, String status) {
    QReport report = QReport.report;
    return queryFactory
        .selectFrom(report)
        .where(
            keyword != null ? report.content.containsIgnoreCase(keyword) : null,
            type != null ? report.type.stringValue().eq(type) : null,
            status != null ? report.status.stringValue().eq(status) : null)
        .fetch();
  }
}
