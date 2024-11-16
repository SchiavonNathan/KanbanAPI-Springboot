package com.example.kanbanJava.service;

import com.example.kanbanJava.model.Task;
import com.example.kanbanJava.model.TaskPriority;
import com.example.kanbanJava.model.TaskStatus;
import com.example.kanbanJava.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, Task taskDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setStatus(taskDetails.getStatus());
            existingTask.setPriority(taskDetails.getPriority());
            existingTask.setDeadline(taskDetails.getDeadline());

            return taskRepository.save(existingTask);
        }
        return null; // Task not found
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false; // Task not found
    }

    public Task moveTaskStatus(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            // Verificar o status atual e mover para o próximo
            if (task.getStatus() == TaskStatus.TO_DO) {
                task.setStatus(TaskStatus.IN_PROGRESS);
            } else if (task.getStatus() == TaskStatus.IN_PROGRESS) {
                task.setStatus(TaskStatus.COMPLETED);
            } else {
                // Não é possível mover de COMPLETED
                return null;
            }

            // Salvar a tarefa com o novo status
            return taskRepository.save(task);
        }

        // Tarefa não encontrada
        return null;
    }

    // Ordenar tarefas por prioridade decrescente dentro de um status
    public List<Task> getTasksByStatusOrderedByPriority(TaskStatus status) {
        return taskRepository.findByStatusOrderByPriorityDesc(status);
    }

    // Filtrar tarefas por prioridade e data limite
    public List<Task> getTasksByPriorityAndDeadline(TaskPriority priority, LocalDate deadline) {
        return taskRepository.findByPriorityAndDeadlineBefore(priority, deadline);
    }
}