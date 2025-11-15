package com.blackpantech.printer_module.application;

import com.blackpantech.printer_module.domain.Task;
import com.blackpantech.printer_module.domain.ports.TaskPrinter;
import com.blackpantech.printer_module.domain.ports.TaskRepository;

public class TaskService {
  private final TaskPrinter taskPrinter;
  private final TaskRepository taskRepository;

  public TaskService(TaskPrinter taskPrinter, TaskRepository taskRepository) {
    this.taskPrinter = taskPrinter;
    this.taskRepository = taskRepository;
  }

  public boolean printTask(Task task) {
    /*
     * TODO:
     * try to print
     * update task in DB as SUCCESS or FAILED
     */
    return !task.topic().contains("test");
  }
}
