package org.example.service;

import org.example.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TaskService {

    // ИЗМЕНЕНИЕ: вынесено стартовое значение идентификатора в отдельную константу
    private static final long INITIAL_ID = 1L;

    private final Map<Long, Task> tasks = new HashMap<>();

    // ИЗМЕНЕНИЕ: nextId переименован в nextTaskId
    // ИЗМЕНЕНИЕ: вместо числа 1 используется константа INITIAL_ID
    private long nextTaskId = INITIAL_ID;

    public Task create(Task task) {
        // ИЗМЕНЕНИЕ: валидация задачи вынесена в отдельный метод
        validateTask(task);

        // ИЗМЕНЕНИЕ: используется nextTaskId вместо nextId
        task.setId(nextTaskId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public Task getById(Long id) {
        // ИЗМЕНЕНИЕ: проверка id вынесена в отдельный метод
        validateId(id);

        // ИЗМЕНЕНИЕ: поиск задачи вынесен в отдельный метод
        return findExistingTask(id);
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public Task update(Long id, Task updatedTask) {
        // ИЗМЕНЕНИЕ: проверка id вынесена в отдельный метод
        validateId(id);

        // ИЗМЕНЕНИЕ: в update добавлена общая валидация задачи
        validateTask(updatedTask);

        // ИЗМЕНЕНИЕ: логика поиска вынесена в findExistingTask()
        // ИЗМЕНЕНИЕ: existing переименован в existingTask
        Task existingTask = findExistingTask(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        return existingTask;
    }

    public boolean delete(Long id) {
        // ИЗМЕНЕНИЕ: проверка id вынесена в отдельный метод
        validateId(id);
        return tasks.remove(id) != null;
    }

    // ИЗМЕНЕНИЕ: добавлен отдельный метод для проверки id
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
    }

    // ИЗМЕНЕНИЕ: добавлен отдельный метод для проверки задачи
    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }
    }

    // ИЗМЕНЕНИЕ: добавлен отдельный метод для поиска существующей задачи
    private Task findExistingTask(Long id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
        return task;
    }
}