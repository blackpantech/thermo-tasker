package com.blackpantech.central_module.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("Central Module Task Service")
public class TaskServiceTest {
  @Mock private final TaskRepository taskRepository = mock(TaskRepository.class);
  @Mock private final TaskMessageBroker taskMessageBroker = mock(TaskMessageBroker.class);
  private final TaskService taskService = new TaskService(taskRepository, taskMessageBroker);
  private final List<Task> tasks =
      List.of(
          new Task("Groceries", "Get milk", Instant.now()),
          new Task("Kitchen", "Wash the dishes", Instant.now()));

  @Test
  @DisplayName("Should get tasks")
  void shouldGetTasks() {
    // GIVEN
    when(taskRepository.getTasks()).thenReturn(tasks);

    // WHEN
    var result = taskService.getTasks();

    // THEN
    assertThat(result).isEqualTo(tasks);
    verify(taskRepository).getTasks();
    verifyNoMoreInteractions(taskRepository);
  }

  @Test
  @DisplayName("Should create new task")
  void shouldCreateTask() {
    // GIVEN
    var newTask = new Task("Groceries", "Get milk", Instant.now());

    // WHEN
    taskService.createTask(newTask);

    // THEN
    verify(taskMessageBroker).sendTask(newTask);
    verify(taskRepository).createTask(newTask);
    verifyNoMoreInteractions(taskRepository, taskMessageBroker);
  }
}
