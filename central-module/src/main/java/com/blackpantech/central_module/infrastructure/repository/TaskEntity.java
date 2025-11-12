package com.blackpantech.central_module.infrastructure.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "topic", nullable = false)
  private String topic;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "due_date", nullable = false)
  private Instant dueDate;

  @Column(name = "task_status", nullable = false)
  private TaskStatus taskStatus;

  private TaskEntity() {
    // No arg constructor
  }

  public TaskEntity(String topic, String description, Instant dueDate, TaskStatus taskStatus) {
    this.topic = topic;
    this.description = description;
    this.dueDate = dueDate;
    this.taskStatus = taskStatus;
  }

  public UUID getId() {
    return id;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Instant getDueDate() {
    return dueDate;
  }

  public void setDueDate(Instant dueDate) {
    this.dueDate = dueDate;
  }

  public TaskStatus getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(TaskStatus taskStatus) {
    this.taskStatus = taskStatus;
  }
}
