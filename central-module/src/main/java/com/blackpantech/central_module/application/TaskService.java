package com.blackpantech.central_module.application;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskService {
  private final TaskRepository taskRepository;
  private final TaskMessageBroker taskMessageBroker;
  private final Logger logger = LoggerFactory.getLogger(TaskService.class);

  public TaskService(final TaskRepository taskRepository,
      final TaskMessageBroker taskMessageBroker) {
    this.taskRepository = taskRepository;
    this.taskMessageBroker = taskMessageBroker;
  }

  public List<Task> getTasks() {
    logger.debug("Getting all current tasks.");
    return taskRepository.getTasks();
  }

  public void createTask(final Task newTask)
      throws TaskPersistenceException, TaskQueueingException {
    if (newTask.dueDate() == null) {
      createTaskWithoutDueDate(newTask);
    } else {
      createTaskWithDueDate(newTask);
    }
  }

  private void createTaskWithoutDueDate(final Task newTask)
      throws TaskQueueingException, TaskPersistenceException {
    logger.debug("Creating new task {} with topic \"{}\" and description \"{}\".", newTask.id(),
        newTask.topic(), newTask.description());
    final var sentTask = sendTask(newTask);
    persistTask(sentTask);
  }

  private void createTaskWithDueDate(final Task newTask) throws TaskPersistenceException {
    logger.debug("Creating new task {} with topic \"{}\", description \"{}\" and due date \"{}\".",
        newTask.id(), newTask.topic(), newTask.description(), newTask.dueDate());
    persistTask(newTask);
  }

  private Task sendTask(final Task newTask) throws TaskQueueingException {
    final var newTaskWithDueDate = getNewTaskToCreate(newTask);
    logger.debug(
        "Sending new task {} with topic \"{}\", description \"{}\" and due date \"{}\" to queue.",
        newTaskWithDueDate.id(), newTaskWithDueDate.topic(), newTaskWithDueDate.description(),
        newTaskWithDueDate.dueDate());
    taskMessageBroker.sendTask(newTaskWithDueDate);
    return newTaskWithDueDate;
  }

  private void persistTask(final Task newTask) throws TaskPersistenceException {
    logger.debug(
        "Persisting new task {} with topic \"{}\", description \"{}\" and due date \"{}\".",
        newTask.id(), newTask.topic(), newTask.description(), newTask.dueDate());
    taskRepository.createTask(newTask);
  }

  private Task getNewTaskToCreate(final Task newTask) {
    return newTask.dueDate() == null
        ? new Task(newTask.id(), newTask.topic(), newTask.description(), Instant.now())
        : newTask;
  }
}
