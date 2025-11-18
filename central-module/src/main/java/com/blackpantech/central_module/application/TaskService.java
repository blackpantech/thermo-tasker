package com.blackpantech.central_module.application;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskService {
  private final TaskRepository taskRepository;
  private final TaskMessageBroker taskMessageBroker;
  private final Logger logger = LoggerFactory.getLogger(TaskService.class);

  public TaskService(
      final TaskRepository taskRepository, final TaskMessageBroker taskMessageBroker) {
    this.taskRepository = taskRepository;
    this.taskMessageBroker = taskMessageBroker;
  }

  public List<Task> getTasks() {
    logger.debug("Getting all current tasks.");
    return taskRepository.getTasks();
  }

  public void createTask(Task newTask) throws TaskPersistenceException, TaskQueueingException {
    logger.debug(
        "Sending new task {} with topic \"{}\", description \"{}\" and due date \"{}\" to queue.",
        newTask.id(),
        newTask.topic(),
        newTask.description(),
        newTask.dueDate());
    taskMessageBroker.sendTask(newTask);
    logger.debug(
        "Persisting new task {} with topic \"{}\", description \"{}\" and due date \"{}\" to queue.",
        newTask.id(),
        newTask.topic(),
        newTask.description(),
        newTask.dueDate());
    taskRepository.createTask(newTask);
  }
}
