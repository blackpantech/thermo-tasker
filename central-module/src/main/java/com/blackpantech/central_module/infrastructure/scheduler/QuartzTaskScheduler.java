package com.blackpantech.central_module.infrastructure.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskSchedulingException;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskScheduler;

public class QuartzTaskScheduler implements TaskScheduler {
  private final TaskMessageBroker taskMessageBroker;
  private final Scheduler scheduler;
  private final Logger logger = LoggerFactory.getLogger(QuartzTaskScheduler.class);

  public QuartzTaskScheduler(final TaskMessageBroker taskMessageBroker, final Scheduler scheduler) {
    this.taskMessageBroker = taskMessageBroker;
    this.scheduler = scheduler;
  }

  @Override
  public void scheduleTask(Task newTask) throws TaskSchedulingException {
    logger.debug("Scheduling new task with topic \"{}\", description \"{}\" and due date \"{}\".",
        newTask.topic(), newTask.description(), newTask.dueDate());
    JobDetail sendTaskJobDetail = newJob(SendTaskJob.class)
        .withIdentity(newTask.id().toString(), SchedulerConstants.SEND_TASK_GROUP).build();
    sendTaskJobDetail.getJobDataMap().put(SchedulerConstants.NEW_TASK, newTask);
    sendTaskJobDetail.getJobDataMap().put(SchedulerConstants.TASK_MESSAGE_BROKER,
        taskMessageBroker);
    SimpleTrigger sendTaskTrigger = (SimpleTrigger) newTrigger().forJob(sendTaskJobDetail)
        .withIdentity(newTask.id().toString(), SchedulerConstants.SEND_TASK_GROUP)
        .startAt(newTask.dueDate()).build();
    try {
      scheduler.scheduleJob(sendTaskJobDetail, sendTaskTrigger);
    } catch (SchedulerException schedulerException) {
      logger.error("Failed to schedule task: {} - {} - {}", newTask.topic(), newTask.description(),
          newTask.dueDate(), schedulerException);
      throw new TaskSchedulingException(
          String.format("Failed to schedule task: %s", schedulerException.getMessage()));
    }
  }
}
