package com.blackpantech.central_module.infrastructure.repository;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.ports.TaskRepository;
import java.util.List;

public class JpaTaskRepository implements TaskRepository {
  private final TaskJpaRepository taskJpaRepository;

  public JpaTaskRepository(TaskJpaRepository taskJpaRepository) {
    this.taskJpaRepository = taskJpaRepository;
  }

  @Override
  public List<Task> getTasks() {
    var taskEntities = taskJpaRepository.findAll();
    return taskEntities.stream()
        .map(
            taskEntity ->
                new Task(
                    taskEntity.getTopic(), taskEntity.getDescription(), taskEntity.getDueDate()))
        .toList();
  }

  @Override
  public void createTask(Task newTask) {
    var newTaskEntity =
        new TaskEntity(
            newTask.topic(), newTask.description(), newTask.dueDate(), TaskStatus.PENDING);
    taskJpaRepository.save(newTaskEntity);
  }
}
