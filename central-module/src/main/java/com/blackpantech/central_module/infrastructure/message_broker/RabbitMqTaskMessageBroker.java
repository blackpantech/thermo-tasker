package com.blackpantech.central_module.infrastructure.message_broker;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMqTaskMessageBroker implements TaskMessageBroker {
  private final Queue tasksQueue;
  private final RabbitTemplate rabbitTemplate;
  private final Logger logger = LoggerFactory.getLogger(RabbitMqTaskMessageBroker.class);

  public RabbitMqTaskMessageBroker(Queue tasksQueue, RabbitTemplate rabbitTemplate) {
    this.tasksQueue = tasksQueue;
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void sendTask(Task newTask) {
    logger.debug(
        "Sending new task with topic \"{}\", description \"{}\" and due date \"{}\" to queue.",
        newTask.topic(),
        newTask.description(),
        newTask.dueDate());
    rabbitTemplate.convertAndSend(tasksQueue.getName(), newTask);
  }
}
