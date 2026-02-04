package com.blackpantech.printer_module.domain.ports;

import com.blackpantech.printer_module.domain.Task;

public interface TaskRepository {
  void markTaskAsSuccessfullyPrinted(Task task);

  void markTaskAsFailedToPrint(Task task);

  boolean isTaskAlreadyPrinted(Task task);
}
