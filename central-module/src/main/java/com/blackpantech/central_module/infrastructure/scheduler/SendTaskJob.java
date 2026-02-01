package com.blackpantech.central_module.infrastructure.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;

public class SendTaskJob implements Job {
  private final Logger logger = LoggerFactory.getLogger(SendTaskJob.class);

  @Autowired
  private TaskMessageBroker taskMessageBroker;

  public SendTaskJob() {
    // Instances of Job must have a public no-argument constructor.
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    var task = (Task) context.getJobDetail().getJobDataMap().get(SchedulerConstants.NEW_TASK);
    logger.debug(
        "Executing job to send task with topic \"{}\", description \"{}\" and due date {} to queue.",
        task.topic(), task.description(), task.dueDate());
    try {
      taskMessageBroker.sendTask(task);
    } catch (TaskQueueingException taskQueueingException) {
      logger.error("Could not queue task with topic \"{}\", description \"{}\" and due date {}.",
          task.topic(), task.description(), task.dueDate());
    }
  }
}
