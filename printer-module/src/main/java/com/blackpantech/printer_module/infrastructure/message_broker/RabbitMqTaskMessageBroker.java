package com.blackpantech.printer_module.infrastructure.message_broker;

import com.blackpantech.printer_module.application.TaskService;
import com.blackpantech.printer_module.domain.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public class RabbitMqTaskMessageBroker {
  private final TaskService taskService;
  private final ObjectMapper objectMapper;
  private final Logger logger = LoggerFactory.getLogger(RabbitMqTaskMessageBroker.class);

  public RabbitMqTaskMessageBroker(TaskService taskService, ObjectMapper objectMapper) {
    this.taskService = taskService;
    this.objectMapper = objectMapper;
  }

  @RabbitListener(queues = "${message-broker.tasks-queue.name}")
  public void receiveTask(String taskString, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
    try {
      Task task = objectMapper.readValue(taskString, Task.class);
      logger.debug(
          "Received task {} with topic \"{}\", description \"{}\" and due date \"{}\" from queue.",
          task.id(),
          task.topic(),
          task.description(),
          task.dueDate());
      if (taskService.printTask(task)) {
        logger.debug(
          "Sending acknowledgment for task {} with topic \"{}\", description \"{}\" and due date \"{}\".",
          task.id(),
          task.topic(),
          task.description(),
          task.dueDate());
        channel.basicAck(tag, false);
      } else {
        logger.debug(
          "Sending negative acknowledgment for task {} with topic \"{}\", description \"{}\" and due date \"{}\".",
          task.id(),
          task.topic(),
          task.description(),
          task.dueDate());
        channel.basicNack(tag, false, false);
      }
    } catch (Exception exception) {
      logger.error("Failed to deserialize task from JSON: {}", taskString, exception);
      channel.basicNack(tag, false, false);
    }
  }
}
