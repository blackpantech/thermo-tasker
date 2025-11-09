package com.blackpantech.central_module.infrastructure.repository;

import com.blackpantech.central_module.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
            new TaskEntity("Groceries", "Get milk", TaskStatus.PENDING),
            new TaskEntity("Admin", "Get the mail", TaskStatus.SUCCESS),
            new TaskEntity("Kitchen", "Wash the dishes", TaskStatus.FAILED));
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
    final var task = new Task("Groceries", "Get milk");

    // WHEN
    jpaTaskRepository.createTask(task);

    // THEN
    verify(taskJpaRepository).save(any());
    verifyNoMoreInteractions(taskJpaRepository);
  }
}
