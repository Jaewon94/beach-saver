package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}
