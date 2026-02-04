package com.blackpantech.central_module.infrastructure.scheduler;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
public class SendDueTasksJobTest {
  @MockitoSpyBean
  SendDueTasksJob sendDueTasksJob;

  @Test
  public void shouldCallSendDueTasksJob() {
    await().atMost(Durations.ONE_MINUTE).untilAsserted(() -> {
      verify(sendDueTasksJob, atLeast(1)).sendDueTasksJob();
    });
  }
}
