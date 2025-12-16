package com.blackpantech.central_module.infrastructure.scheduler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;

@SpringBootTest
@ActiveProfiles("test")
public class QuartzTaskSchedulerTest {
  @MockitoBean
  Scheduler scheduler;
  @Autowired
  TaskMessageBroker taskMessageBroker;
  @Autowired
  QuartzTaskScheduler quartzTaskScheduler;

  @Test
  @DisplayName("Should schedule a task")
  void shouldScheduleTask() throws SchedulerException {
    // GIVEN
    LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(30);
    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    final var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", instant);

    // WHEN
    assertDoesNotThrow(() -> quartzTaskScheduler.scheduleTask(task));

    // THEN
    JobDetail sendTaskJobDetail =
        newJob(SendTaskJob.class).withIdentity(task.id().toString(), "sendTaskGroup").build();
    sendTaskJobDetail.getJobDataMap().put("task", task);
    sendTaskJobDetail.getJobDataMap().put("taskMessageBroker", taskMessageBroker);
    SimpleTrigger sendTaskTrigger = (SimpleTrigger) newTrigger().forJob(sendTaskJobDetail)
        .withIdentity(task.id().toString(), "sendTaskGroup").startAt(task.dueDate()).build();
    verify(scheduler).scheduleJob(sendTaskJobDetail, sendTaskTrigger);
    verifyNoMoreInteractions(scheduler);
  }
}
