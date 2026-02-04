package com.blackpantech.central_module.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
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
  void shouldGetDueTasks() {
    final List<TaskEntity> tasks = taskJpaRepository
        .findAllByDueDateBeforeAndPrintingStatusNot(Instant.now(), PrintingStatus.SUCCESS);
    assertEquals(3, tasks.size());
  }

}
