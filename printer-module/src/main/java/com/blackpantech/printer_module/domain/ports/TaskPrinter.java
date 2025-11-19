package com.blackpantech.printer_module.domain.ports;

import com.blackpantech.printer_module.domain.Task;

public interface TaskPrinter {
  boolean printTask(Task task);
  boolean isPrinterConnectionOk();
}
