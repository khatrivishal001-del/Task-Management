package com.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.model.Task;
import com.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setCompleted(false);
    }

    @Test
    public void testGetAllTasks() throws Exception {
        // Arrange
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCompleted(true);

        List<Task> tasks = Arrays.asList(testTask, task2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    public void testGetTaskById_Found() throws Exception {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(testTask));

        // Act & Assert
        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        // Arrange
        when(taskService.getTaskById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/tasks/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(99L);
    }

    @Test
    public void testCreateTask() throws Exception {
        // Arrange
        when(taskService.createTask(any(Task.class))).thenReturn(testTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    public void testUpdateTask_Success() throws Exception {
        // Arrange
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setCompleted(true);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        // Arrange
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        when(taskService.updateTask(eq(99L), any(Task.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(99L), any(Task.class));
    }

    @Test
    public void testDeleteTask() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }
}
