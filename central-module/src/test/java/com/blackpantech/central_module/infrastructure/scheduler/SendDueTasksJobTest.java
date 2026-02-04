package com.blackpantech.central_module.infrastructure.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import com.blackpantech.central_module.infrastructure.repository.PrintingStatus;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
public class SendDueTasksJobTest {
  @MockitoSpyBean
  private SendDueTasksJob sendDueTasksJob;

  @MockitoBean
  private TaskMessageBroker taskMessageBroker;

  @MockitoBean
  private TaskRepository taskRepository;

  @Test
  public void shouldCallSendDueTasksJob() {
    await().atMost(Durations.ONE_MINUTE).untilAsserted(() -> {
      verify(taskRepository).getDueTasks();
      verify(taskRepository, atMost(0)).updateTaskPrintingStatus(any(), eq(PrintingStatus.PENDING));
      verify(taskMessageBroker, atMost(0)).sendTask(any());
      verify(sendDueTasksJob, atLeast(1)).sendDueTasksJob();
      verifyNoMoreInteractions(taskRepository, taskMessageBroker, sendDueTasksJob);
    });
  }
}
