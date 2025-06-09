package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.Team;
import com.portpolio.beach_saver_backend.repository.custom.TeamRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {}
