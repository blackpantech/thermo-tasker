package com.blackpantech.printer_module.domain.ports;

import com.blackpantech.printer_module.domain.Task;

public interface TaskRepository {
  void markAsSuccessfullyPrinted(Task task);

  void markAsFailedToPrint(Task task);
}
