package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.Beach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeachRepository extends JpaRepository<Beach, Long> {}
