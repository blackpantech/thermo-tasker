package com.blackpantech.central_module.infrastructure.message_broker;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
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
  public void sendTask(Task newTask) throws TaskQueueingException {
    logger.debug(
        "Sending new task with topic \"{}\", description \"{}\" and due date \"{}\" to queue.",
        newTask.topic(),
        newTask.description(),
        newTask.dueDate());
    try {
      rabbitTemplate.convertAndSend(tasksQueue.getName(), newTask);
    } catch (AmqpException exception) {
      logger.error(
          "Failed to queue task: {} - {} - {}",
          newTask.topic(),
          newTask.description(),
          newTask.dueDate(),
          exception);
      throw new TaskQueueingException(
          String.format("Failed to queue task: %s", exception.getMessage()));
    }
  }
}
