package com.blackpantech.central_module.infrastructure.repository;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import jakarta.persistence.PersistenceException;
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
    final var taskEntities = taskJpaRepository.findAll();
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
}
