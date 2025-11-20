package com.blackpantech.printer_module.infrastructure.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskEntity {
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "topic", nullable = false)
  private String topic;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "due_date", nullable = false)
  private Instant dueDate;

  @Column(name = "printing_status", nullable = false)
  private PrintingStatus printingStatus;

  @SuppressWarnings("unused")
  private TaskEntity() {
    // No arg constructor
  }

  public TaskEntity(UUID id, String topic, String description, Instant dueDate,
      PrintingStatus printingStatus) {
    this.id = id;
    this.topic = topic;
    this.description = description;
    this.dueDate = dueDate;
    this.printingStatus = printingStatus;
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

  public PrintingStatus getPrintingStatus() {
    return printingStatus;
  }

  public void setPrintingStatus(PrintingStatus printingStatus) {
    this.printingStatus = printingStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    TaskEntity that = (TaskEntity) o;

    return id.equals(that.id) && topic.equals(that.topic) && description.equals(that.description)
        && dueDate.equals(that.dueDate) && printingStatus == that.printingStatus;
  }
}
