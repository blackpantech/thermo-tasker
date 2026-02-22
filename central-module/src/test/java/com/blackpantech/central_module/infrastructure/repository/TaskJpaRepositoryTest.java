package com.blackpantech.central_module.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskJpaRepositoryTest {
  @Autowired
  private TaskJpaRepository taskJpaRepository;

  @SuppressWarnings("null")
  @BeforeAll
  public void setUp() {
    final List<TaskEntity> tasks = List.of(
        new TaskEntity(UUID.randomUUID(), "Groceries", "Buy milk and eggs",
            Instant.now().minus(Duration.ofDays(2)), PrintingStatus.PENDING),
        new TaskEntity(UUID.randomUUID(), "Admin", "Organize office supplies",
            Instant.now().minus(Duration.ofDays(5)), PrintingStatus.SUCCESS),
        new TaskEntity(UUID.randomUUID(), "Kitchen", "Clean the fridge",
            Instant.now().minus(Duration.ofDays(1)), PrintingStatus.FAILED),
        new TaskEntity(UUID.randomUUID(), "Kitchen", "Clean the fridge again",
            Instant.now().plus(Duration.ofDays(1)), PrintingStatus.FAILED),
        new TaskEntity(UUID.randomUUID(), "Finance", "Pay utility bills",
            Instant.now().plus(Duration.ofDays(3)), PrintingStatus.PENDING),
        new TaskEntity(UUID.randomUUID(), "Health", "Schedule doctor appointment",
            Instant.now().plus(Duration.ofDays(7)), PrintingStatus.SUCCESS),
        new TaskEntity(UUID.randomUUID(), "Home", "Fix the leaky faucet",
            Instant.now().minus(Duration.ofDays(4)), PrintingStatus.FAILED),
        new TaskEntity(UUID.randomUUID(), "Work", "Prepare presentation slides",
            Instant.now().plus(Duration.ofDays(1)), PrintingStatus.PENDING),
        new TaskEntity(UUID.randomUUID(), "Education", "Read chapter 3 of the book",
            Instant.now().minus(Duration.ofDays(3)), PrintingStatus.SUCCESS));
    tasks.forEach(task -> taskJpaRepository.save(task));
  }


  @Test
  @DisplayName("Should get all due tasks that were not printed successfully")
  void shouldGetDueTasks() {
    final List<TaskEntity> tasks = taskJpaRepository
        .findAllByDueDateBeforeAndPrintingStatusNot(Instant.now(), PrintingStatus.SUCCESS);
    assertEquals(3, tasks.size());
  }

  @Test
  @DisplayName("Should get all tasks ordered by printing status (printed last) and due date (asc)")
  void shouldGetAllTasksOrderedByPrintingStatusPrintedLastAndDueDateAsc() {
    final List<TaskEntity> tasks =
        taskJpaRepository.findAllOrderByPrintingStatusPrintedLastAndDueDateAsc();
    assertEquals(9, tasks.size());
    for (int i = 0; i < tasks.size() - 1; i++) {
      final var currentTask = tasks.get(i);
      final var nextTask = tasks.get(i + 1);
      if (currentTask.getPrintingStatus() == PrintingStatus.SUCCESS) {
        assertEquals(PrintingStatus.SUCCESS, nextTask.getPrintingStatus());
      }
      assertTrue((((currentTask.getPrintingStatus() == nextTask.getPrintingStatus())
          || (currentTask.getPrintingStatus() != nextTask.getPrintingStatus()
              && nextTask.getPrintingStatus() != PrintingStatus.SUCCESS))
          && (currentTask.getDueDate().isBefore(nextTask.getDueDate())
              || currentTask.getDueDate().equals(nextTask.getDueDate())))
          || (currentTask.getPrintingStatus() != nextTask.getPrintingStatus()
              && nextTask.getPrintingStatus() == PrintingStatus.SUCCESS));
    }
  }

  @Test
  @DisplayName("Should delete tasks with due date older than 7 days")
  void shouldDeleteOldTasks() {
    var task = new TaskEntity(UUID.randomUUID(), "Math", "Resolve exercise 9.",
        Instant.now().minus(Duration.ofDays(9)), PrintingStatus.SUCCESS);
    taskJpaRepository.save(task);
    final var initialTasks = taskJpaRepository.findAll();
    assertEquals(10, initialTasks.size());
    taskJpaRepository.deleteAllByDueDateBeforeAndPrintingStatus(
        Instant.now().minus(Duration.ofDays(7)), PrintingStatus.SUCCESS);
    final var updatedTasks = taskJpaRepository.findAll();
    assertEquals(9, updatedTasks.size());
  }
}
