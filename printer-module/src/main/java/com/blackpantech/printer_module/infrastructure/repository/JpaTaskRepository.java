package com.blackpantech.printer_module.infrastructure.repository;

import com.blackpantech.printer_module.domain.Task;
import com.blackpantech.printer_module.domain.ports.TaskRepository;

public class JpaTaskRepository implements TaskRepository {

  private final TaskJpaRepository taskJpaRepository;

  public JpaTaskRepository(TaskJpaRepository taskJpaRepository) {
    this.taskJpaRepository = taskJpaRepository;
  }

  @Override
  public void markTaskAsSuccessfullyPrinted(Task task) {
    taskJpaRepository.updatePrintingStatus(task.id(), PrintingStatus.SUCCESS);
  }

  @Override
  public void markTaskAsFailedToPrint(Task task) {
    taskJpaRepository.updatePrintingStatus(task.id(), PrintingStatus.FAILED);
  }

}
