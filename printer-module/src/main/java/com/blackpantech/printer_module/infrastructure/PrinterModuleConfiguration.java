package com.blackpantech.printer_module.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blackpantech.printer_module.application.TaskService;
import com.blackpantech.printer_module.infrastructure.message_broker.RabbitMqTaskMessageBroker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class PrinterModuleConfiguration {
  @Bean
  public TaskService taskService() {
    return new TaskService(null, null);
  }

  @Bean
  public ObjectMapper objectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public RabbitMqTaskMessageBroker rabbitMqTaskMessageBroker(TaskService taskService, ObjectMapper objectMapper) {
    return new RabbitMqTaskMessageBroker(taskService, objectMapper);
  }
}
