package com.blackpantech.central_module.domain.ports;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import com.blackpantech.central_module.infrastructure.repository.PrintingStatus;
import java.time.Instant;
import java.util.List;

public interface TaskRepository {
  List<Task> getTasks();

  void createTask(Task newTask) throws TaskPersistenceException;

  List<Task> getDueTasks();

  void updateTaskPrintingStatus(Task task, PrintingStatus printingStatus);

  void deleteOldTasks(Instant dateTimeBeforeWhichTasksAreDeleted);
}
