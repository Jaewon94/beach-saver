package com.portpolio.beach_saver_backend.repository.custom;

import com.portpolio.beach_saver_backend.domain.CollectionDepot;
import java.util.List;

public interface CollectionDepotRepositoryCustom {
  List<CollectionDepot> searchDepots(String keyword, String status);
}
