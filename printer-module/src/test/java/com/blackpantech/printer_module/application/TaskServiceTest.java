package com.blackpantech.printer_module.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.blackpantech.printer_module.domain.Task;
import com.blackpantech.printer_module.domain.ports.TaskPrinter;
import com.blackpantech.printer_module.domain.ports.TaskRepository;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("Printer Module Task Service")
public class TaskServiceTest {
  @Mock private final TaskPrinter taskPrinter = mock(TaskPrinter.class);
  @Mock private final TaskRepository taskRepository = mock(TaskRepository.class);
  private final TaskService taskService = new TaskService(taskPrinter, taskRepository);

  @Test
  @DisplayName("Should print task")
  void shouldPrintTask() {
    // GIVEN
    Task task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());
    when(taskPrinter.printTask(task)).thenReturn(true);

    // WHEN
    var result = taskService.printTask(task);

    // THEN
    assertTrue(result);
    verify(taskPrinter).printTask(task);
    verify(taskRepository).markTaskAsSuccessfullyPrinted(task);
    verifyNoMoreInteractions(taskRepository, taskPrinter);
  }

  @Test
  @DisplayName("Should fail to print task")
  void shouldFailToPrintTask() {
    // GIVEN
    Task task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());
    when(taskPrinter.printTask(task)).thenReturn(false);

    // WHEN
    var result = taskService.printTask(task);

    // THEN
    assertFalse(result);
    verify(taskPrinter).printTask(task);
    verify(taskRepository).markTaskAsFailedToPrint(task);
    verifyNoMoreInteractions(taskRepository, taskPrinter);
  }

}
