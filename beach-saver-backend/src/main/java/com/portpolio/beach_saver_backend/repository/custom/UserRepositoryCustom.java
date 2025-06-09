package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.User;
import java.util.List;

public interface UserRepositoryCustom {
  List<User> searchUsers(String keyword, String role, String status);
}
