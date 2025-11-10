package com.blackpantech.central_module.infrastructure;

import com.blackpantech.central_module.application.TaskService;
import com.blackpantech.central_module.domain.ports.TaskMessageBroker;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import com.blackpantech.central_module.infrastructure.message_broker.RabbitMqTaskMessageBroker;
import com.blackpantech.central_module.infrastructure.repository.JpaTaskRepository;
import com.blackpantech.central_module.infrastructure.repository.TaskJpaRepository;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CentralModuleConfiguration {
  @Value("${message-broker.dead-letter.exchange.name}")
  private String deadLetterExchangeName;

  @Value("${message-broker.dead-letter.queue.name}")
  private String deadLetterQueueName;

  @Value("${message-broker.dead-letter.queue.routing-key}")
  private String deadLetterRoutingKey;

  @Value("${message-broker.tasks-queue.name}")
  private String tasksQueueName;

  @Bean
  public TaskRepository taskRepository(TaskJpaRepository taskJpaRepository) {
    return new JpaTaskRepository(taskJpaRepository);
  }

  @Bean
  public TaskMessageBroker taskMessageBroker(Queue tasksQueue, RabbitTemplate rabbitTemplate) {
    return new RabbitMqTaskMessageBroker(tasksQueue, rabbitTemplate);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    return rabbitTemplate;
  }

  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange(deadLetterExchangeName);
  }

  @Bean
  public Queue deadLetterQueue() {
    return new Queue(deadLetterQueueName);
  }

  @Bean
  public Binding deadLetterQueueBinding(DirectExchange deadLetterExchange, Queue deadLetterQueue) {
    return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(deadLetterRoutingKey);
  }

  @Bean
  public Queue tasksQueue() {
    return QueueBuilder.durable(tasksQueueName)
        .deadLetterExchange(deadLetterExchangeName)
        .deadLetterRoutingKey(deadLetterRoutingKey)
        .build();
  }

  @Bean
  public TaskService taskService(
      TaskRepository taskRepository, TaskMessageBroker taskMessageBroker) {
    return new TaskService(taskRepository, taskMessageBroker);
  }
}
