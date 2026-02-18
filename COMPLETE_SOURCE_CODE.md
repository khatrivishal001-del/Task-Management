# Complete Source Code for Task Management Application

This document contains all the source code files you need to copy locally and push to complete the implementation.

## Time Remaining: ~2 hours 40 minutes

## Quick Action Steps:

1. Clone your repository:
```bash
git clone https://github.com/khatrivishal001-del/Task-Management.git
cd Task-Management
```

2. Copy all files below into the correct locations
3. Test backend and frontend
4. Push to GitHub
5. Submit the assessment

---

## BACKEND JAVA FILES

### File: `backend/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: task-management-backend
  datasource:
    url: jdbc:h2:mem:taskdb
    driverClassName: org.h2.Driver
    username: sa
    password: 
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true

server:
  port: 8080

logging:
  level:
    com.taskmanagement: DEBUG
```

### File: `backend/src/main/java/com/taskmanagement/model/Task.java`

```java
package com.taskmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean isCompleted = false;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### File: `backend/src/main/java/com/taskmanagement/repository/TaskRepository.java`

```java
package com.taskmanagement.repository;

import com.taskmanagement.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task> findByIsCompleted(Boolean isCompleted, Pageable pageable);
    Page<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
```

### File: `backend/src/main/java/com/taskmanagement/service/TaskService.java`

```java
package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskService {
    
    private final TaskRepository taskRepository;
    
    public Page<Task> getTasks(Boolean isCompleted, String sortBy, Pageable pageable) {
        log.debug("Getting tasks with isCompleted={}, sortBy={}", isCompleted, sortBy);
        if (isCompleted != null) {
            return taskRepository.findByIsCompleted(isCompleted, pageable);
        }
        return taskRepository.findAll(pageable);
    }
    
    public Optional<Task> getTaskById(Integer id) {
        log.debug("Getting task by id={}", id);
        return taskRepository.findById(id);
    }
    
    public Task saveTask(Task task) {
        log.debug("Saving task: {}", task.getTitle());
        return taskRepository.save(task);
    }
    
    public Optional<Task> updateTask(Integer id, Task taskDetails) {
        log.debug("Updating task id={}", id);
        return taskRepository.findById(id).map(task -> {
            if (taskDetails.getTitle() != null) {
                task.setTitle(taskDetails.getTitle());
            }
            if (taskDetails.getDescription() != null) {
                task.setDescription(taskDetails.getDescription());
            }
            if (taskDetails.getIsCompleted() != null) {
                task.setIsCompleted(taskDetails.getIsCompleted());
            }
            if (taskDetails.getDueDate() != null) {
                task.setDueDate(taskDetails.getDueDate());
            }
            return taskRepository.save(task);
        });
    }
    
    public void deleteTask(Integer id) {
        log.debug("Deleting task id={}", id);
        taskRepository.deleteById(id);
    }
    
    public Optional<Task> toggleTaskCompletion(Integer id) {
        log.debug("Toggling task completion for id={}", id);
        return taskRepository.findById(id).map(task -> {
            task.setIsCompleted(!task.getIsCompleted());
            return taskRepository.save(task);
        });
    }
}
```

### File: `backend/src/main/java/com/taskmanagement/controller/TaskController.java`

```java
package com.taskmanagement.controller;

import com.taskmanagement.model.Task;
import com.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class TaskController {
    
    private final TaskService taskService;
    
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(required = false) String sortBy,
            Pageable pageable) {
        log.info("GET /api/tasks - isCompleted={}, sortBy={}", isCompleted, sortBy);
        return ResponseEntity.ok(taskService.getTasks(isCompleted, sortBy, pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id) {
        log.info("GET /api/tasks/{}", id);
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        log.info("POST /api/tasks - Creating task: {}", task.getTitle());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.saveTask(task));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Integer id,
            @Valid @RequestBody Task taskDetails) {
        log.info("PUT /api/tasks/{}", id);
        return taskService.updateTask(id, taskDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        log.info("DELETE /api/tasks/{}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTaskCompletion(@PathVariable Integer id) {
        log.info("PATCH /api/tasks/{}/toggle", id);
        return taskService.toggleTaskCompletion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
```

### File: `backend/src/test/java/com/taskmanagement/service/TaskServiceTest.java`

```java
package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @InjectMocks
    private TaskService taskService;
    
    private Task testTask;
    
    @BeforeEach
    void setUp() {
        testTask = Task.builder()
                .id(1)
                .title("Test Task")
                .description("Test Description")
                .isCompleted(false)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();
    }
    
    @Test
    void testGetTaskById_Success() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        
        Optional<Task> result = taskService.getTaskById(1);
        
        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1);
    }
    
    @Test
    void testToggleTaskCompletion() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        
        Optional<Task> result = taskService.toggleTaskCompletion(1);
        
        assertTrue(result.isPresent());
        assertTrue(result.get().getIsCompleted());
        verify(taskRepository, times(1)).save(testTask);
    }
}
```

---

## Repository Link

**GitHub Repository:** https://github.com/khatrivishal001-del/Task-Management

---

## Next Steps:

1. **Copy all backend files** into the correct directory structure
2. **Create frontend** using the React TypeScript template (instructions in SETUP_GUIDE.md)
3. **Test the application** thoroughly
4. **Push to GitHub**
5. **Submit assessment** with repository link

You have ~2.5 hours remaining - plenty of time to complete everything!
