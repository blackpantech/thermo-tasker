package com.blackpantech.printer_module.infrastructure.repository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import com.blackpantech.printer_module.domain.Task;
import java.time.Instant;
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
  @MockitoBean
  TaskJpaRepository taskJpaRepository;

  @Autowired
  JpaTaskRepository jpaTaskRepository;

  @Test
  @DisplayName("Should mark task as successfully printed")
  void shouldMarkTaskAsSuccessfullyPrinted() {
    // GIVEN
    var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());

    // WHEN
    jpaTaskRepository.markTaskAsSuccessfullyPrinted(task);

    // THEN
    verify(taskJpaRepository).updatePrintingStatus(task.id(), PrintingStatus.SUCCESS);
    verifyNoMoreInteractions(taskJpaRepository);
  }

  @Test
  @DisplayName("Should mark task as failed to print")
  void shouldmarkTaskAsFailedToPrint() {
    // GIVEN
    var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());

    // WHEN
    jpaTaskRepository.markTaskAsFailedToPrint(task);

    // THEN
    verify(taskJpaRepository).updatePrintingStatus(task.id(), PrintingStatus.FAILED);
    verifyNoMoreInteractions(taskJpaRepository);
  }
}
