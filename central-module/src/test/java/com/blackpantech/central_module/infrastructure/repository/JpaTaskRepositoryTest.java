package com.blackpantech.central_module.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.blackpantech.central_module.domain.Task;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class JpaTaskRepositoryTest {
  @MockitoBean TaskJpaRepository taskJpaRepository;

  @Autowired JpaTaskRepository jpaTaskRepository;

  @Test
  @DisplayName("Should get tasks")
  void shouldGetTasks() {
    // GIVEN
    final List<TaskEntity> tasks =
        List.of(
            new TaskEntity("Groceries", "Get milk", Instant.now(), TaskStatus.PENDING),
            new TaskEntity("Admin", "Get the mail", Instant.now(), TaskStatus.SUCCESS),
            new TaskEntity("Kitchen", "Wash the dishes", Instant.now(), TaskStatus.FAILED));
    when(taskJpaRepository.findAll()).thenReturn(tasks);

    // WHEN
    final List<Task> result = jpaTaskRepository.getTasks();

    // THEN
    assertEquals(tasks.size(), result.size());
    verify(taskJpaRepository).findAll();
    verifyNoMoreInteractions(taskJpaRepository);
  }

  @Test
  @DisplayName("Should create a task")
  void shouldCreateTask() {
    // GIVEN
    var dueDate = Instant.now();
    final var task = new Task("Groceries", "Get milk", dueDate);

    // WHEN
    assertDoesNotThrow(() -> jpaTaskRepository.createTask(task));

    // THEN
    TaskEntity expectedTask =
        new TaskEntity(task.topic(), task.description(), task.dueDate(), TaskStatus.PENDING);
    verify(taskJpaRepository).save(refEq(expectedTask, "id"));
    verifyNoMoreInteractions(taskJpaRepository);
  }
}
