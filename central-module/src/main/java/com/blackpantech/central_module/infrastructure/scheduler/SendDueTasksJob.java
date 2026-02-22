package com.blackpantech.central_module.infrastructure.scheduler;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import com.blackpantech.central_module.infrastructure.repository.PrintingStatus;

public class SendDueTasksJob {
  private final Logger logger = LoggerFactory.getLogger(SendDueTasksJob.class);

  private final TaskMessageBroker taskMessageBroker;

  private final TaskRepository taskRepository;

  public SendDueTasksJob(final TaskMessageBroker taskMessageBroker,
      final TaskRepository taskRepository) {
    this.taskMessageBroker = taskMessageBroker;
    this.taskRepository = taskRepository;
  }

  @Scheduled(cron = "0 * * * * *")
  public void sendDueTasksJob() {
    logger.debug("Fetching all due tasks.");
    List<Task> dueTasks = taskRepository.getDueTasks();
    dueTasks.forEach(task -> {
      logger.debug("Updating printing status of due tasks to pending.");
      taskRepository.updateTaskPrintingStatus(task, PrintingStatus.PENDING);
      logger.debug("Sending due tasks to message broker.");
      try {
        taskMessageBroker.sendTask(task);
      } catch (TaskQueueingException taskQueueingException) {
        logger.error("Error while sending task to message broker.", taskQueueingException);
      }
    });
  }
}
