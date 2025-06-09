package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {}
