package org.example.service;

import org.example.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TaskService {

    // НОВОЕ: вынесено стартовое значение идентификатора в константу
    private static final long INITIAL_ID = 1L;

    private final Map<Long, Task> tasks = new HashMap<>();

    // ИЗМЕНЕНО: nextId -> nextTaskId
    // ИЗМЕНЕНО: используется константа INITIAL_ID
    private long nextTaskId = INITIAL_ID;

    public Task create(Task task) {
        // НОВОЕ: общая валидация вынесена в отдельный метод
        validateTask(task);

        // ИЗМЕНЕНО: nextId++ -> nextTaskId++
        task.setId(nextTaskId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public Task getById(Long id) {
        // НОВОЕ: проверка id вынесена в отдельный метод
        validateId(id);

        // НОВОЕ: поиск существующей задачи вынесен в отдельный метод
        return findExistingTask(id);
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task update(Long id, Task updatedTask) {
        // НОВОЕ: вынесенная проверка id
        validateId(id);

        // НОВОЕ: теперь в update тоже есть полная валидация задачи
        validateTask(updatedTask);

        // ИЗМЕНЕНО: existing -> existingTask
        // НОВОЕ: поиск вынесен в findExistingTask()
        Task existingTask = findExistingTask(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        return existingTask;
    }

    public boolean delete(Long id) {
        // НОВОЕ: вынесенная проверка id
        validateId(id);
        return tasks.remove(id) != null;
    }

    // НОВОЕ: отдельный метод валидации id
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
    }

    // НОВОЕ: отдельный метод общей валидации задачи
    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }
    }

    // НОВОЕ: отдельный метод поиска задачи по id
    private Task findExistingTask(Long id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
        return task;
    }
}