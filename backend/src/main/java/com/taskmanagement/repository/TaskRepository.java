package com.taskmanagement.repository;

import com.taskmanagement.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByCompleted(Boolean isCompleted, Pageable pageable);
    Page<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
