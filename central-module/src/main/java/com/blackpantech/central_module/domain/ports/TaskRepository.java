package com.blackpantech.central_module.domain.ports;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import java.util.List;

public interface TaskRepository {
  List<Task> getTasks();

  void createTask(Task newTask) throws TaskPersistenceException;
}
