package com.blackpantech.printer_module.infrastructure;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blackpantech.printer_module.application.TaskService;
import com.blackpantech.printer_module.domain.ports.TaskPrinter;
import com.blackpantech.printer_module.domain.ports.TaskRepository;
import com.blackpantech.printer_module.infrastructure.message_broker.RabbitMqTaskMessageBroker;
import com.blackpantech.printer_module.infrastructure.printer.EpsonTaskPrinter;
import com.blackpantech.printer_module.infrastructure.printer.FileOutputStreamFactory;
import com.blackpantech.printer_module.infrastructure.printer.OutputStreamFactory;
import com.blackpantech.printer_module.infrastructure.repository.JpaTaskRepository;
import com.blackpantech.printer_module.infrastructure.repository.TaskJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class PrinterModuleConfiguration {
  @Value("${printer.path}")
  private String printerPath;

  @Bean
  public TaskService taskService(TaskPrinter taskPrinter, TaskRepository taskRepository) {
    return new TaskService(taskPrinter, taskRepository);
  }

  @Bean
  public TaskRepository taskRepository(TaskJpaRepository taskJpaRepository) {
    return new JpaTaskRepository(taskJpaRepository);
  }

  @Bean
  public TaskPrinter taskPrinter(OutputStreamFactory outputStreamFactory) throws IOException {
    return new EpsonTaskPrinter(outputStreamFactory);
  }

  @Bean
  public OutputStreamFactory outputStreamFactory() {
    return new FileOutputStreamFactory(printerPath);
  }

  @Bean
  public ObjectMapper objectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public RabbitMqTaskMessageBroker rabbitMqTaskMessageBroker(TaskService taskService,
      ObjectMapper objectMapper) {
    return new RabbitMqTaskMessageBroker(taskService, objectMapper);
  }
}
