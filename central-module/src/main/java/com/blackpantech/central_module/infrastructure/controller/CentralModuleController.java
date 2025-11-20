package com.blackpantech.central_module.infrastructure.controller;

import com.blackpantech.central_module.application.TaskService;
import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskPersistenceException;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.UUID;

@Controller
public class CentralModuleController {
  private static final String NEW_VIEW = "new";
  private static final String INDEX_VIEW = "index";
  private static final String TASKS_VIEW = "tasks";
  private static final String REDIRECT_INDEX_VIEW = "redirect:/";
  private final TaskService taskService;
  private final Logger logger = LoggerFactory.getLogger(CentralModuleController.class);

  public CentralModuleController(final TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public String getHomePage() {
    logger.debug("Serving home page.");
    return INDEX_VIEW;
  }

  @GetMapping("/new")
  public String getNewTaskPage(Model model) {
    TaskForm newTaskForm = new TaskForm("", "");
    model.addAttribute("newTaskForm", newTaskForm);
    logger.debug("Serving task creation page.");
    return NEW_VIEW;
  }

  @GetMapping("/tasks")
  public String getTasksPage(Model model) {
    logger.debug("Getting all current tasks.");
    model.addAttribute("tasks", taskService.getTasks());
    logger.debug("Serving current tasks list page.");
    return TASKS_VIEW;
  }

  @PostMapping("/tasks")
  public String postNewTask(@ModelAttribute("newTaskForm") TaskForm newTaskForm, Model model) {
    logger.debug("Validating new task.");
    if (newTaskForm.topic().isBlank() || newTaskForm.description().isBlank()) {
      logger.error("Task's topic and description cannot be empty.");
      model.addAttribute("errorMessage", "Task's topic and description cannot be empty.");
      return NEW_VIEW;
    }
    var newTask =
        new Task(UUID.randomUUID(), newTaskForm.topic(), newTaskForm.description(), Instant.now());
    logger.debug("Creating new task with topic \"{}\" and description \"{}\".", newTaskForm.topic(),
        newTaskForm.description());
    try {
      taskService.createTask(newTask);
    } catch (TaskQueueingException e) {
      logger.error("Could not queue task with topic \"{}\", description \"{}\" and due date {}.",
          newTask.topic(), newTask.description(), newTask.dueDate());
      model.addAttribute("errorMessage",
          String.format(
              "Could not queue task with topic \"%s\", description \"%s\" and due date %s.",
              newTask.topic(), newTask.description(), newTask.dueDate()));
      return NEW_VIEW;
    } catch (TaskPersistenceException exception) {
      logger.error("Could not persist task with topic \"{}\", description \"{}\" and due date {}.",
          newTask.topic(), newTask.description(), newTask.dueDate());
      model.addAttribute("errorMessage",
          String.format(
              "Could not persist task with topic \"%s\", description \"%s\" and due date %s.",
              newTask.topic(), newTask.description(), newTask.dueDate()));
      return NEW_VIEW;
    }
    logger.debug("Redirecting to home page.");
    return REDIRECT_INDEX_VIEW;
  }
}
