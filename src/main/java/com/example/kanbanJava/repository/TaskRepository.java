package com.example.kanbanJava.repository;

import com.example.kanbanJava.model.Task;
import com.example.kanbanJava.model.TaskPriority;
import com.example.kanbanJava.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.status = :status ORDER BY CASE " +
            "WHEN t.priority = 'HIGH' THEN 1 " +
            "WHEN t.priority = 'MEDIUM' THEN 2 " +
            "WHEN t.priority = 'LOW' THEN 3 END")
    List<Task> findByStatusOrderByPriorityDesc(@Param("status") TaskStatus status);

    // Filtrar tarefas por prioridade e data limite
    List<Task> findByPriorityAndDeadlineBefore(TaskPriority priority, LocalDate deadline);
}