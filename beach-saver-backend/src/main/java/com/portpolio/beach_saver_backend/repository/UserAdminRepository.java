package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdminRepository extends JpaRepository<UserAdmin, Long> {}
