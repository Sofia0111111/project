package org.example.service;


import org.example.model.Task;
import org.example.TelegramService;

import java.util.*;

public class TaskService {

    private final Map<Long, Task> tasks = new HashMap<>();
    private long nextId = 1;

    public Task create(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }

        task.setId(nextId++);
        tasks.put(task.getId(), task);

        // ИНТЕГРАЦИЯ С TELEGRAM
        TelegramService.sendMessage("Новая задача: " + task.getTitle());

        return task;
    }

    public Task getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        Task task = tasks.get(id);
        if (task == null) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
        return task;
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task update(Long id, Task updatedTask) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (updatedTask == null) {
            throw new IllegalArgumentException("Updated task must not be null");
        }
        Task existing = tasks.get(id);
        if (existing == null) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setCompleted(updatedTask.isCompleted());

        return existing;
    }

    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return tasks.remove(id) != null;
    }
}