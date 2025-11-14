package com.blackpantech.central_module.infrastructure.messsage_broker;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.infrastructure.message_broker.RabbitMqTaskMessageBroker;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public class RabbitMqTaskMessageBrokerTest {
  @MockitoBean Queue tasksQueue;
  @MockitoBean RabbitTemplate rabbitTemplate;

  @Autowired RabbitMqTaskMessageBroker rabbitMqTaskMessageBroker;

  @Test
  @DisplayName("Should send task")
  void shouldSendTask() {
    // GIVEN
    final var task = new Task("Groceries", "Get milk", Instant.now());

    // WHEN
    assertDoesNotThrow(() -> rabbitMqTaskMessageBroker.sendTask(task));

    // THEN
    verify(rabbitTemplate).getMessageConverter();
    verify(rabbitTemplate).convertAndSend(tasksQueue.getName(), task);
    verifyNoMoreInteractions(rabbitTemplate);
  }
}
