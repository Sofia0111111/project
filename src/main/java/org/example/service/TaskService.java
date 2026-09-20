package org.example.service;

import org.example.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TaskService {

    private static final long INITIAL_ID = 1L;

    private final Map<Long, Task> tasks = new HashMap<>();
    private long nextTaskId = INITIAL_ID;

    public Task create(Task task) {
        validateTask(task);

        task.setId(nextTaskId++);
        tasks.put(task.getId(), task);

        return task;
    }

    public Task getById(Long id) {
        validateId(id);
        return findExistingTask(id);
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task update(Long id, Task updatedTask) {
        validateId(id);
        validateTask(updatedTask);

        Task existingTask = findExistingTask(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        return existingTask;
    }

    public boolean delete(Long id) {
        validateId(id);
        return tasks.remove(id) != null;
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
    }

    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }
    }

    private Task findExistingTask(Long id) {
        Task task = tasks.get(id);

        if (task == null) {
            throw new NoSuchElementException(
                    "Task not found with id: " + id
            );
        }

        return task;
    }
}