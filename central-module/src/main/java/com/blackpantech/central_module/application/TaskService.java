package com.blackpantech.central_module.application;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
  private final TaskRepository taskRepository;

  private final TaskMessageBroker taskMessageBroker;

  public TaskService(
      final TaskRepository taskRepository, final TaskMessageBroker taskMessageBroker) {
    this.taskRepository = taskRepository;
    this.taskMessageBroker = taskMessageBroker;
  }

  public List<Task> getTasks() {
    return new ArrayList<>();
  }

  public void createTask(Task newTask) {}
}
