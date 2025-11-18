package com.blackpantech.central_module.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.blackpantech.central_module.domain.Task;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class JpaTaskRepositoryTest {
  @MockitoBean TaskJpaRepository taskJpaRepository;

  @Autowired JpaTaskRepository jpaTaskRepository;

  @Test
  @DisplayName("Should get tasks")
  void shouldGetTasks() {
    // GIVEN
    final List<TaskEntity> tasks =
        List.of(
            new TaskEntity(UUID.randomUUID(), "Groceries", "Get milk", Instant.now(), PrintingStatus.PENDING),
            new TaskEntity(UUID.randomUUID(), "Admin", "Get the mail", Instant.now(), PrintingStatus.SUCCESS),
            new TaskEntity(UUID.randomUUID(), "Kitchen", "Wash the dishes", Instant.now(), PrintingStatus.FAILED));
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
    final var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", dueDate);

    // WHEN
    assertDoesNotThrow(() -> jpaTaskRepository.createTask(task));

    // THEN
    TaskEntity expectedTask = new TaskEntity(task.id(), task.topic(), task.description(), task.dueDate(), PrintingStatus.PENDING);
    verify(taskJpaRepository).save(expectedTask);
    verifyNoMoreInteractions(taskJpaRepository);
  }
}
