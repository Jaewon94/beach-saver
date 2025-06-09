package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.User;
import com.portpolio.beach_saver_backend.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {}
