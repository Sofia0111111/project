package org.example;

import org.example.model.Task;
import org.example.service.TaskService;

public class Main {

    public static void main(String[] args) {

        TaskService service = new TaskService();

        // Создание задач
        Task task1 = new Task();
        task1.setTitle("Сделать практическую");
        task1.setDescription("Закончить все задания");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setTitle("Подготовиться к защите");
        task2.setDescription("Повторить материал");
        task2.setCompleted(false);

        service.create(task1);
        service.create(task2);

        // Вывод всех задач
        System.out.println("Список задач:");
        service.getAll().forEach(task ->
                System.out.println(task.getId() + ": " + task.getTitle())
        );

        // Обновление задачи
        task1.setCompleted(true);
        service.update(task1.getId(), task1);

        System.out.println("\nПосле обновления:");
        service.getAll().forEach(task ->
                System.out.println(task.getId() + ": " + task.getTitle() + " | выполнено: " +
                        task.isCompleted())
        );

        // Удаление задачи
        service.delete(task2.getId());

        System.out.println("\nПосле удаления:");
        service.getAll().forEach(task ->
                System.out.println(task.getId() + ": " + task.getTitle())
        );
    }
}