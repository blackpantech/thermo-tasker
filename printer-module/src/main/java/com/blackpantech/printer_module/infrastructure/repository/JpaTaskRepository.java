package com.blackpantech.printer_module.infrastructure.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackpantech.printer_module.domain.Task;
import com.blackpantech.printer_module.domain.ports.TaskRepository;

public class JpaTaskRepository implements TaskRepository {

  private final TaskJpaRepository taskJpaRepository;
  private final Logger logger = LoggerFactory.getLogger(JpaTaskRepository.class);

  public JpaTaskRepository(TaskJpaRepository taskJpaRepository) {
    this.taskJpaRepository = taskJpaRepository;
  }

  @Override
  public void markTaskAsSuccessfullyPrinted(Task task) {
    logger.debug(
        "Updating task {} with topic \"{}\", description \"{}\" and due date \"{}\" as successfully printed.",
        task.id(), task.topic(), task.description(), task.dueDate());
    taskJpaRepository.updatePrintingStatus(task.id(), PrintingStatus.SUCCESS);
  }

  @Override
  public void markTaskAsFailedToPrint(Task task) {
    logger.debug(
        "Updating task {} with topic \"{}\", description \"{}\" and due date \"{}\" as failed to print.",
        task.id(), task.topic(), task.description(), task.dueDate());
    taskJpaRepository.updatePrintingStatus(task.id(), PrintingStatus.FAILED);
  }

}
