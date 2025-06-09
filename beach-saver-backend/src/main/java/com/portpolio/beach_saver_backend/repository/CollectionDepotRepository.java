package com.portpolio.beach_saver_backend.repository;

import com.portpolio.beach_saver_backend.domain.CollectionDepot;
import com.portpolio.beach_saver_backend.repository.custom.CollectionDepotRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionDepotRepository
    extends JpaRepository<CollectionDepot, Long>, CollectionDepotRepositoryCustom {}
