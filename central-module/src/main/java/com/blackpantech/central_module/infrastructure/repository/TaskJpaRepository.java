package com.blackpantech.central_module.infrastructure.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
  List<TaskEntity> findAllByDueDateBeforeAndPrintingStatusNot(Instant dueDate,
      PrintingStatus printingStatus);
}
