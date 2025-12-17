package com.blackpantech.central_module.infrastructure.controller;

import com.blackpantech.central_module.application.TaskService;
import com.blackpantech.central_module.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@WebMvcTest(CentralModuleController.class)
@DisplayName("Central Module Controller")
public class CentralModuleControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private TaskService taskService;

  @SuppressWarnings("null")
  @Test
  @DisplayName("Should get home page")
  void shouldGetHomePage() throws Exception {
    mockMvc.perform(get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(view().name("index"));
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("Should get new task page")
  void shouldGetNewTaskPage() throws Exception {
    mockMvc.perform(get("/new").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(view().name("new"));
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("Should get tasks page")
  void shouldGetTasks() throws Exception {
    final var tasks = List.of(new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now()),
        new Task(UUID.randomUUID(), "Kitchen", "Wash the dishes", Instant.now()));
    when(taskService.getTasks()).thenReturn(tasks);
    mockMvc.perform(get("/tasks").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(view().name("tasks")).andExpect(model().attributeExists("tasks"))
        .andExpect(model().attribute("tasks", equalTo(tasks)))
        .andExpect(xpath("/html/body/div/ul/li").nodeCount(2));

    verify(taskService).getTasks();
    verifyNoMoreInteractions(taskService);
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("Should post new task")
  void shouldPostNewTask() throws Exception {
    final var newTaskForm = new TaskForm("Groceries", "Get milk", null);
    mockMvc
        .perform(post("/tasks").flashAttr("newTaskForm", newTaskForm)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isFound()).andExpect(view().name("redirect:/"));

    final Task expectedTask = new Task(null, newTaskForm.topic(), newTaskForm.description(), null);
    verify(taskService).createTask(refEq(expectedTask, "id"));
    verifyNoMoreInteractions(taskService);
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("Should post new task with due date")
  void shouldPostNewTaskWithDueDate() throws Exception {
    final var newTaskForm = new TaskForm("Groceries", "Get milk", LocalDateTime.now());
    mockMvc
        .perform(post("/tasks").flashAttr("newTaskForm", newTaskForm)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isFound()).andExpect(view().name("redirect:/"));

    final Task expectedTask = new Task(null, newTaskForm.topic(), newTaskForm.description(),
        newTaskForm.dueDate().atZone(ZoneId.systemDefault()).toInstant());
    verify(taskService).createTask(refEq(expectedTask, "id"));
    verifyNoMoreInteractions(taskService);
  }
}
