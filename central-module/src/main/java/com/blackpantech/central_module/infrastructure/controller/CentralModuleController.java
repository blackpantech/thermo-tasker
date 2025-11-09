package com.blackpantech.central_module.infrastructure.controller;

import com.blackpantech.central_module.application.TaskService;
import com.blackpantech.central_module.domain.Task;
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
    Task newTask = new Task("", "");
    model.addAttribute("newTask", newTask);
    return "new";
  }

  @GetMapping("/tasks")
  public String getTasksPage(Model model) {
    model.addAttribute("tasks", taskService.getTasks());
    return "tasks";
  }

  @PostMapping("/tasks")
  public String postNewTask(@ModelAttribute("newTask") Task newTask) {
    taskService.createTask(newTask);
    return "redirect:/";
  }
}
