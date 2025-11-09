package com.blackpantech.central_module.infrastructure;

import com.blackpantech.central_module.application.TaskService;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import com.blackpantech.central_module.infrastructure.repository.JpaTaskRepository;
import com.blackpantech.central_module.infrastructure.repository.TaskJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CentralModuleConfiguration {
  @Bean
  public TaskRepository taskRepository(TaskJpaRepository taskJpaRepository) {
    return new JpaTaskRepository(taskJpaRepository);
  }

  @Bean
  public TaskService taskService(TaskRepository taskRepository) {
    // TODO
    return new TaskService(taskRepository, null);
  }
}
