package org.example.service;

import org.example.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    // ---- Create ----

    @Test
    void create_validTask_assignsIdAndStoresTask() {
        Task task = new Task(null, "Test Task", "Description", false);

        Task created = taskService.create(task);

        assertNotNull(created.getId());
        assertEquals("Test Task", created.getTitle());
        assertEquals("Description", created.getDescription());
        assertFalse(created.isCompleted());
        assertEquals(created, taskService.getById(created.getId()));
    }

    @Test
    void create_multipleTasks_assignsUniqueIds() {
        Task task1 = taskService.create(new Task(null, "Task 1", null, false));
        Task task2 = taskService.create(new Task(null, "Task 2", null, false));
        Task task3 = taskService.create(new Task(null, "Task 3", null, false));

        assertNotEquals(task1.getId(), task2.getId());
        assertNotEquals(task2.getId(), task3.getId());
        assertNotEquals(task1.getId(), task3.getId());
    }

    @Test
    void create_nullTask_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> taskService.create(null));
    }

    @Test
    void create_taskWithBlankTitle_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> taskService.create(new Task(null, "", "desc", false)));
        assertThrows(IllegalArgumentException.class,
                () -> taskService.create(new Task(null, "   ", "desc", false)));
        assertThrows(IllegalArgumentException.class,
                () -> taskService.create(new Task(null, null, "desc", false)));
    }

    // ---- GetById ----

    @Test
    void getById_existingId_returnsTask() {
        Task created = taskService.create(new Task(null, "Find Me", "desc", false));

        Task found = taskService.getById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals("Find Me", found.getTitle());
    }

    @Test
    void getById_nonExistingId_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> taskService.getById(999L));
    }

    @Test
    void getById_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> taskService.getById(null));
    }

    // ---- GetAll ----

    @Test
    void getAll_emptyStore_returnsEmptyList() {
        List<Task> all = taskService.getAll();

        assertTrue(all.isEmpty());
    }

    @Test
    void getAll_multipleTasks_returnsAllTasks() {
        taskService.create(new Task(null, "Task 1", null, false));
        taskService.create(new Task(null, "Task 2", null, false));
        taskService.create(new Task(null, "Task 3", null, false));

        List<Task> all = taskService.getAll();

        assertEquals(3, all.size());
    }

    // ---- Update ----

    @Test
    void update_existingTask_updatesFields() {
        Task created = taskService.create(new Task(null, "Original", "old desc", false));
        Task updates = new Task(null, "Updated", "new desc", true);

        Task updated = taskService.update(created.getId(), updates);

        assertEquals("Updated", updated.getTitle());
        assertEquals("new desc", updated.getDescription());
        assertTrue(updated.isCompleted());
        assertEquals(created.getId(), updated.getId());
    }

    @Test
    void update_nonExistingId_throwsNoSuchElementException() {
        Task updates = new Task(null, "Updated", "desc", false);

        assertThrows(NoSuchElementException.class, () -> taskService.update(999L, updates));
    }

    @Test
    void update_nullId_throwsIllegalArgumentException() {
        Task updates = new Task(null, "Updated", "desc", false);

        assertThrows(IllegalArgumentException.class, () -> taskService.update(null, updates));
    }

    @Test
    void update_nullTask_throwsIllegalArgumentException() {
        Task created = taskService.create(new Task(null, "Task", "desc", false));

        assertThrows(IllegalArgumentException.class, () -> taskService.update(created.getId(), null));
    }

    // ---- Delete ----

    @Test
    void delete_existingId_removesTaskAndReturnsTrue() {
        Task created = taskService.create(new Task(null, "Delete Me", "desc", false));

        boolean result = taskService.delete(created.getId());

        assertTrue(result);
        assertThrows(NoSuchElementException.class, () -> taskService.getById(created.getId()));
    }

    @Test
    void delete_nonExistingId_returnsFalse() {
        boolean result = taskService.delete(999L);

        assertFalse(result);
    }

    @Test
    void delete_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> taskService.delete(null));
    }

    // ---- Task model coverage ----

    @Test
    void task_equalsAndHashCode_behavesCorrectly() {
        Task task1 = taskService.create(new Task(null, "Task", "desc", false));
        Task task2 = taskService.create(new Task(null, "Other", "other", true));

        // same object
        assertEquals(task1, task1);
        // null
        assertNotEquals(null, task1);
        // different class
        assertNotEquals("not a task", task1);
        // different id
        assertNotEquals(task1, task2);
        assertNotEquals(task1.hashCode(), task2.hashCode());

        // same id means equal
        Task copy = new Task();
        copy.setId(task1.getId());
        copy.setTitle("Different title");
        copy.setDescription("Different desc");
        copy.setCompleted(true);
        assertEquals(task1, copy);
        assertEquals(task1.hashCode(), copy.hashCode());
    }

    @Test
    void task_toString_containsFields() {
        Task created = taskService.create(new Task(null, "My Task", "My Desc", false));

        String str = created.toString();

        assertTrue(str.contains("My Task"));
        assertTrue(str.contains("My Desc"));
        assertTrue(str.contains(created.getId().toString()));
    }
}
