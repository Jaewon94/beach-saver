package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.UserTeam;
import com.portpolio.beach_saver_backend.domain.UserTeamId;
import com.portpolio.beach_saver_backend.repository.custom.UserTeamRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository
    extends JpaRepository<UserTeam, UserTeamId>, UserTeamRepositoryCustom {}
