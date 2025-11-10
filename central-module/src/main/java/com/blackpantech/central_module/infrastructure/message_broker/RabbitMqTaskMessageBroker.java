package com.blackpantech.central_module.infrastructure.message_broker;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMqTaskMessageBroker implements TaskMessageBroker {
  private final Queue tasksQueue;
  private final RabbitTemplate rabbitTemplate;

  public RabbitMqTaskMessageBroker(Queue tasksQueue, RabbitTemplate rabbitTemplate) {
    this.tasksQueue = tasksQueue;
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void sendTask(Task newTask) {
    rabbitTemplate.convertAndSend(tasksQueue.getName(), newTask);
  }
}
