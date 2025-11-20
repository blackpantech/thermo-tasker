package com.blackpantech.central_module.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("Central Module Task Service")
public class TaskServiceTest {
  @Mock
  private final TaskRepository taskRepository = mock(TaskRepository.class);
  @Mock
  private final TaskMessageBroker taskMessageBroker = mock(TaskMessageBroker.class);
  private final TaskService taskService = new TaskService(taskRepository, taskMessageBroker);
  private final List<Task> tasks =
      List.of(new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now()),
          new Task(UUID.randomUUID(), "Kitchen", "Wash the dishes", Instant.now()));

  @Test
  @DisplayName("Should get tasks")
  void shouldGetTasks() {
    // GIVEN
    when(taskRepository.getTasks()).thenReturn(tasks);

    // WHEN
    final var result = taskService.getTasks();

    // THEN
    assertThat(result).isEqualTo(tasks);
    verify(taskRepository).getTasks();
    verifyNoMoreInteractions(taskRepository);
  }

  @Test
  @DisplayName("Should create new task")
  void shouldCreateTask() throws TaskQueueingException, TaskPersistenceException {
    // GIVEN
    final var newTask = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());

    // WHEN
    assertDoesNotThrow(() -> taskService.createTask(newTask));

    // THEN
    verify(taskMessageBroker).sendTask(newTask);
    verify(taskRepository).createTask(newTask);
    verifyNoMoreInteractions(taskRepository, taskMessageBroker);
  }
}
