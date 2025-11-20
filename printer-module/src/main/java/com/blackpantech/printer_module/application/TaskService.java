package com.blackpantech.printer_module.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackpantech.printer_module.domain.Task;
import com.blackpantech.printer_module.domain.ports.TaskPrinter;
import com.blackpantech.printer_module.domain.ports.TaskRepository;

public class TaskService {
  private final TaskPrinter taskPrinter;
  private final TaskRepository taskRepository;
  private final Logger logger = LoggerFactory.getLogger(TaskService.class);

  public TaskService(final TaskPrinter taskPrinter, final TaskRepository taskRepository) {
    this.taskPrinter = taskPrinter;
    this.taskRepository = taskRepository;
  }

  public boolean printTask(final Task task) {
    logger.debug("Printing task {} with topic \"{}\", description \"{}\" and due date \"{}\".",
        task.id(), task.topic(), task.description(), task.dueDate());
    final var printingResult = taskPrinter.printTask(task);

    if (printingResult) {
      logger.debug(
          "Marking task {} with topic \"{}\", description \"{}\" and due date \"{}\" as successfully printed.",
          task.id(), task.topic(), task.description(), task.dueDate());
      taskRepository.markTaskAsSuccessfullyPrinted(task);
    } else {
      logger.debug(
          "Marking task {} with topic \"{}\", description \"{}\" and due date \"{}\" as failed to print.",
          task.id(), task.topic(), task.description(), task.dueDate());
      taskRepository.markTaskAsFailedToPrint(task);
    }

    return printingResult;
  }
}
