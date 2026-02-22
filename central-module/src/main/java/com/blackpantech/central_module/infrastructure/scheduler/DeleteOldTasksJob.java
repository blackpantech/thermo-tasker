package com.blackpantech.central_module.infrastructure.scheduler;

import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.blackpantech.central_module.domain.ports.TaskRepository;

public class DeleteOldTasksJob {
  private final Logger logger = LoggerFactory.getLogger(DeleteOldTasksJob.class);

  private final TaskRepository taskRepository;

  private final long daysBeforeDeletion;

  public DeleteOldTasksJob(final TaskRepository taskRepository, final long daysBeforeDeletion) {
    this.taskRepository = taskRepository;
    this.daysBeforeDeletion = daysBeforeDeletion;
  }

  @Scheduled(cron = "0 0 0 * * 1")
  public void deleteOldTasksJob() {
    logger.debug("Deleting old tasks.");
    var dateTimeBeforeWhichTasksAreDeleted =
        Instant.now().minus(Duration.ofDays(daysBeforeDeletion));
    taskRepository.deleteOldTasks(dateTimeBeforeWhichTasksAreDeleted);
  }
}
