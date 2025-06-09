package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.Report;
import java.util.List;

public interface ReportRepositoryCustom {
  List<Report> searchReports(String keyword, String type, String status);
}
