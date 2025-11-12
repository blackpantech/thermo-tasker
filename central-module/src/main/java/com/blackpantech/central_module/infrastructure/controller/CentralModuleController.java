package com.blackpantech.central_module.infrastructure.controller;

import com.blackpantech.central_module.application.TaskService;
import com.blackpantech.central_module.domain.Task;
import java.time.Instant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CentralModuleController {
  private final TaskService taskService;

  public CentralModuleController(final TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public String getHomePage() {
    return "index";
  }

  @GetMapping("/new")
  public String getNewTaskPage(Model model) {
    TaskForm newTaskForm = new TaskForm("", "");
    model.addAttribute("newTaskForm", newTaskForm);
    return "new";
  }

  @GetMapping("/tasks")
  public String getTasksPage(Model model) {
    model.addAttribute("tasks", taskService.getTasks());
    return "tasks";
  }

  @PostMapping("/tasks")
  public String postNewTask(@ModelAttribute("newTaskForm") TaskForm newTaskForm, Model model) {
    if (newTaskForm.topic().isBlank() || newTaskForm.description().isBlank()) {
      model.addAttribute("errorMessage", "Task's topic and description cannot be empty.");
      return "new";
    }
    var newTask = new Task(newTaskForm.topic(), newTaskForm.description(), Instant.now());
    taskService.createTask(newTask);
    return "redirect:/";
  }
}
