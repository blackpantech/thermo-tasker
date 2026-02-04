package com.blackpantech.central_module.infrastructure.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;

public class SendDueTasksJob {
  private final Logger logger = LoggerFactory.getLogger(SendDueTasksJob.class);

  private TaskMessageBroker taskMessageBroker;

  private TaskRepository taskRepository;

  public SendDueTasksJob(final TaskMessageBroker taskMessageBroker,
      final TaskRepository taskRepository) {
    this.taskMessageBroker = taskMessageBroker;
    this.taskRepository = taskRepository;
  }

  @Scheduled(cron = "0 * * * * *")
  public void sendDueTasksJob() {
    logger.info("Oh, I'm running! A new minute has started!");
  }
}
