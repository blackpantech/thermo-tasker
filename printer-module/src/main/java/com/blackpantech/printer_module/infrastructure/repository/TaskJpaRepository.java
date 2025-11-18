package com.blackpantech.printer_module.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
  @Modifying
  @Transactional
  @Query("UPDATE TaskEntity t SET t.printingStatus = :printingStatus WHERE t.id = :id")
  void updatePrintingStatus(UUID id, PrintingStatus printingStatus);
}
