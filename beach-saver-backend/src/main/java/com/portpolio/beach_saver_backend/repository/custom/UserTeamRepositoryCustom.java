package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.UserTeam;
import java.util.List;

public interface UserTeamRepositoryCustom {
  List<UserTeam> searchUserTeams(Long userId, Long teamId, String status);
}
