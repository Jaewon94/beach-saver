package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {}
