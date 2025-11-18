package com.blackpantech.printer_module.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blackpantech.printer_module.application.TaskService;
import com.blackpantech.printer_module.domain.ports.TaskRepository;
import com.blackpantech.printer_module.infrastructure.message_broker.RabbitMqTaskMessageBroker;
import com.blackpantech.printer_module.infrastructure.repository.JpaTaskRepository;
import com.blackpantech.printer_module.infrastructure.repository.TaskJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class PrinterModuleConfiguration {
  @Bean
  public TaskService taskService(TaskRepository taskRepository) {
    return new TaskService(null, taskRepository);
  }

  @Bean
  public TaskRepository taskRepository(TaskJpaRepository taskJpaRepository) {
    return new JpaTaskRepository(taskJpaRepository);
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
