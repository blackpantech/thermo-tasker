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
    var printingResult = taskPrinter.printTask(task);

    if (printingResult) {
      taskRepository.markTaskAsSuccessfullyPrinted(task);
    } else {
      taskRepository.markTaskAsFailedToPrint(task);
    }

    return printingResult;
  }
}
