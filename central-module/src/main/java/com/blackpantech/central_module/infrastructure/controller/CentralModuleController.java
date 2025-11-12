package com.blackpantech.central_module.infrastructure.controller;

import com.blackpantech.central_module.application.TaskService;
import com.blackpantech.central_module.domain.Task;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CentralModuleController {
  private final TaskService taskService;
  private final Logger logger = LoggerFactory.getLogger(CentralModuleController.class);

  public CentralModuleController(final TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public String getHomePage() {
    logger.debug("Serving home page.");
    return "index";
  }

  @GetMapping("/new")
  public String getNewTaskPage(Model model) {
    TaskForm newTaskForm = new TaskForm("", "");
    model.addAttribute("newTaskForm", newTaskForm);
    logger.debug("Serving task creation page.");
    return "new";
  }

  @GetMapping("/tasks")
  public String getTasksPage(Model model) {
    logger.debug("Getting all current tasks.");
    model.addAttribute("tasks", taskService.getTasks());
    logger.debug("Serving current tasks list page.");
    return "tasks";
  }

  @PostMapping("/tasks")
  public String postNewTask(@ModelAttribute("newTaskForm") TaskForm newTaskForm, Model model) {
    logger.debug("Validating new task.");
    if (newTaskForm.topic().isBlank() || newTaskForm.description().isBlank()) {
      logger.error("Task's topic and description cannot be empty.");
      model.addAttribute("errorMessage", "Task's topic and description cannot be empty.");
      return "new";
    }
    var newTask = new Task(newTaskForm.topic(), newTaskForm.description(), Instant.now());
    logger.debug(
        "Creating new task with topic \"{}\" and description \"{}\".",
        newTaskForm.topic(),
        newTaskForm.description());
    taskService.createTask(newTask);
    logger.debug("Redirecting to home page.");
    return "redirect:/";
  }
}
