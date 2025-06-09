package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.Report;
import com.portpolio.beach_saver_backend.repository.custom.ReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {}
