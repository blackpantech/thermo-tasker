package com.blackpantech.printer_module.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import com.blackpantech.printer_module.domain.Task;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
  void shouldMarkTaskAsFailedToPrint() {
    // GIVEN
    var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());

    // WHEN
    jpaTaskRepository.markTaskAsFailedToPrint(task);

    // THEN
    verify(taskJpaRepository).updatePrintingStatus(task.id(), PrintingStatus.FAILED);
    verifyNoMoreInteractions(taskJpaRepository);
  }

  @SuppressWarnings("null")
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  @DisplayName("Should check if task is already printed")
  void shouldCheckIfTaskIsAlreadyPrinted(boolean isTaskAlreadyPrintedInDB) {
    // GIVEN
    var printingStatus = isTaskAlreadyPrintedInDB ? PrintingStatus.SUCCESS : PrintingStatus.PENDING;
    var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());
    var foundTaskEntity =
        new TaskEntity(UUID.randomUUID(), "Groceries", "Get milk", Instant.now(), printingStatus);
    when(taskJpaRepository.findById(task.id())).thenReturn(Optional.of(foundTaskEntity));

    // WHEN
    var result = jpaTaskRepository.isTaskAlreadyPrinted(task);

    // THEN
    assertEquals(isTaskAlreadyPrintedInDB, result);
    verify(taskJpaRepository).findById(task.id());
    verifyNoMoreInteractions(taskJpaRepository);
  }
}
