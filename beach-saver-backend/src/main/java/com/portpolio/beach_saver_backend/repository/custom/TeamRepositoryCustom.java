package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.Team;
import java.util.List;

public interface TeamRepositoryCustom {
  List<Team> searchTeams(String keyword, Long orgId, Boolean isActive);
}
