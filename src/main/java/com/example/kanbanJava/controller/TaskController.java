package com.example.kanbanJava.controller;

import com.example.kanbanJava.model.Task;
import com.example.kanbanJava.model.TaskPriority;
import com.example.kanbanJava.model.TaskStatus;
import com.example.kanbanJava.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}/sorted-by-priority")
    public ResponseEntity<List<Task>> getTasksByStatusOrderedByPriority(@PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatusOrderedByPriority(status);
        return ResponseEntity.ok(tasks);
    }

    // Filtrar tarefas por prioridade e data limite
    @GetMapping("/filter")
    public ResponseEntity<List<Task>> getTasksByPriorityAndDeadline(
            @RequestParam TaskPriority priority,
            @RequestParam String deadline) {
        LocalDate parsedDeadline = LocalDate.parse(deadline);
        List<Task> tasks = taskService.getTasksByPriorityAndDeadline(priority, parsedDeadline);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.status(201).body(savedTask);
    }

    // Rota para mover o status da tarefa
    @PutMapping("/{id}/move")
    public ResponseEntity<Task> moveTaskStatus(@PathVariable Long id) {
        Task updatedTask = taskService.moveTaskStatus(id);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.badRequest().body(null); // Status inválido ou tarefa não encontrada
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(id, taskDetails);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean isDeleted = taskService.deleteTask(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}