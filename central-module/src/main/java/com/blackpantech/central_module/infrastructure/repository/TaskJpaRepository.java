package com.blackpantech.central_module.infrastructure.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
  List<TaskEntity> findAllByDueDateBeforeAndPrintingStatusNot(Instant dueDate,
      PrintingStatus printingStatus);

  @Query("SELECT t FROM TaskEntity t ORDER BY CASE WHEN t.printingStatus = 1 THEN 1 ELSE 0 END, t.dueDate ASC")
  List<TaskEntity> findAllOrderByPrintingStatusPrintedLastAndDueDateAsc();

  void deleteAllByDueDateBeforeAndPrintingStatus(Instant dueDate, PrintingStatus printingStatus);
}
