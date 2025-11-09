package com.blackpantech.central_module.application;

import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;

public class TaskService {
  private final TaskRepository taskRepository;

  private final TaskMessageBroker taskMessageBroker;

  public TaskService(
      final TaskRepository taskRepository, final TaskMessageBroker taskMessageBroker) {
    this.taskRepository = taskRepository;
    this.taskMessageBroker = taskMessageBroker;
  }
}
