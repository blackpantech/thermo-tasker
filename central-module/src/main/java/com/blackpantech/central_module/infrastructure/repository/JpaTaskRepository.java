package com.blackpantech.central_module.infrastructure.repository;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import jakarta.persistence.PersistenceException;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaTaskRepository implements TaskRepository {
  private final TaskJpaRepository taskJpaRepository;
  private final Logger logger = LoggerFactory.getLogger(JpaTaskRepository.class);

  public JpaTaskRepository(final TaskJpaRepository taskJpaRepository) {
    this.taskJpaRepository = taskJpaRepository;
  }

  @Override
  public List<Task> getTasks() {
    logger.debug("Getting all current tasks entities.");
    final var taskEntities =
        taskJpaRepository.findAllOrderByPrintingStatusPrintedLastAndDueDateAsc();
    logger.debug("Mapping all current tasks entities to task domain objects.");
    return taskEntities.stream().map(taskEntity -> new Task(taskEntity.getId(),
        taskEntity.getTopic(), taskEntity.getDescription(), taskEntity.getDueDate())).toList();
  }

  @Override
  public void createTask(final Task newTask) throws TaskPersistenceException {
    logger.debug(
        "Persisting new task {} with topic \"{}\", description \"{}\" and due date \"{}\" to queue.",
        newTask.id(), newTask.topic(), newTask.description(), newTask.dueDate());
    final var newTaskEntity = new TaskEntity(newTask.id(), newTask.topic(), newTask.description(),
        newTask.dueDate(), PrintingStatus.PENDING);
    try {
      taskJpaRepository.save(newTaskEntity);
    } catch (final PersistenceException exception) {
      logger.error("Failed to create task: {} - {}", newTask.topic(), newTask.description(),
          exception);
      throw new TaskPersistenceException(
          String.format("Failed to save task %s", exception.getMessage()));
    }
  }

  @Override
  public List<Task> getDueTasks() {
    logger.debug("Getting all due tasks entities.");
    final var currentTimestamp = Instant.now();
    final var taskEntities = taskJpaRepository
        .findAllByDueDateBeforeAndPrintingStatusNot(currentTimestamp, PrintingStatus.SUCCESS);
    logger.debug("Mapping all due tasks entities to task domain objects.");
    return taskEntities.stream().map(taskEntity -> new Task(taskEntity.getId(),
        taskEntity.getTopic(), taskEntity.getDescription(), taskEntity.getDueDate())).toList();
  }

  @SuppressWarnings("null")
  @Override
  public void updateTaskPrintingStatus(Task task, PrintingStatus printingStatus) {
    logger.debug("Updating printing status of task {} to {}.", task.id(), printingStatus);
    final var optionalTaskEntity = taskJpaRepository.findById(task.id());
    if (optionalTaskEntity.isPresent()
        && optionalTaskEntity.get().getPrintingStatus() != printingStatus) {
      final var taskEntity = optionalTaskEntity.get();
      taskEntity.setPrintingStatus(printingStatus);
      logger.debug("Saving updated task {}.", taskEntity.getId());
      taskJpaRepository.save(taskEntity);
    }
  }
}
