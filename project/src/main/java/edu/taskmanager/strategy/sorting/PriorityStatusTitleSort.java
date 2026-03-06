package edu.taskmanager.strategy.sorting;
import edu.taskmanager.builder.TaskBuilder;
import edu.taskmanager.model.Task;
import edu.taskmanager.util.Priority;
import edu.taskmanager.util.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
//СОРТИРОВКА ПО Priority Status И Title
public class PriorityStatusTitleSort {
    static Task task1 = new TaskBuilder()
            .setDescription("This is test")
            .setTitle("Test")
            .setPriority(Priority.HIGH)
            .setStatus(TaskStatus.TODO)
            .setDueDate(LocalDateTime.now())
            .build();

    static Task task2 = new TaskBuilder()
            .setTitle("Сдать отчет")
            .setDescription("Подготовить еженедельный отчет для руководства")
            .setPriority(Priority.CRITICAL)
            .setStatus(TaskStatus.IN_PROGRESS)
            .setDueDate(LocalDateTime.now().plusHours(5))
            .build();

    static Task task3 = new TaskBuilder()
            .setTitle("Почистить почту")
            .setDescription("Удалить старые письма и разобрать входящие")
            .setPriority(Priority.LOW)
            .setStatus(TaskStatus.TODO)
            .setDueDate(LocalDateTime.now().plusDays(3))
            .build();

    static Task task4 = new TaskBuilder()
            .setTitle("Подготовить презентацию")
            .setDescription("Создать слайды для встречи с заказчиком")
            .setPriority(Priority.HIGH)
            .setStatus(TaskStatus.TODO)
            .setDueDate(LocalDateTime.now().plusDays(2))
            .build();

    static Task task5 = new TaskBuilder()
            .setTitle("Провести код-ревью")
            .setDescription("Проверить пулл-реквесты команды")
            .setPriority(Priority.MEDIUM)
            .setStatus(TaskStatus.IN_PROGRESS)
            .setDueDate(LocalDateTime.now().plusHours(3))
            .build();

    static Task task6 = new TaskBuilder()
            .setTitle("Обновить зависимости")
            .setDescription("Обновить версии библиотек в pom.xml")
            .setPriority(Priority.LOW)
            .setStatus(TaskStatus.DONE)
            .setDueDate(LocalDateTime.now().minusDays(1))
            .build();

    static Task task7 = new TaskBuilder()
            .setTitle("Написать тесты")
            .setDescription("Покрыть новый функционал юнит-тестами")
            .setPriority(Priority.HIGH)
            .setStatus(TaskStatus.TODO)
            .setDueDate(LocalDateTime.now().plusDays(4))
            .build();

    static Task task8 = new TaskBuilder()
            .setTitle("Деплой на прод")
            .setDescription("Выкатить новую версию на production")
            .setPriority(Priority.CRITICAL)
            .setStatus(TaskStatus.TODO)
            .setDueDate(LocalDateTime.now().plusHours(1))
            .build();

    static Task task9 = new TaskBuilder()
            .setTitle("Обновить документацию")
            .setDescription("Актуализировать README и API документацию")
            .setPriority(Priority.MEDIUM)
            .setStatus(TaskStatus.IN_PROGRESS)
            .setDueDate(LocalDateTime.now().plusDays(5))
            .build();

    public static void bubbleSort(List<Task> tasks, Comparator<Task> comparator) {
        int n = tasks.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(tasks.get(j), tasks.get(j + 1)) > 0) {
                    Task temp = tasks.get(j);
                    tasks.set(j, tasks.get(j + 1));
                    tasks.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void main(String[] args) {

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
        tasks.add(task6);
        tasks.add(task7);
        tasks.add(task8);
        tasks.add(task9);
        System.out.println("\n" + "До сортировки");
        tasks.stream().forEach(task ->
                System.out.println("Title: " + task.getTitle() +
                        ", Status: " + task.getStatus() +
                        ", Priority: " + task.getPriority())
        );

        bubbleSort(tasks, Comparator
                .comparing(Task::getPriority).reversed()
                .thenComparing(Task::getStatus)
                .thenComparing(Task::getTitle)
                .reversed()
        );
        System.out.println("\n" + "После сортировки");

        tasks.stream().forEach(task ->
                System.out.println("Title: " + task.getTitle() +
                        ", Status: " + task.getStatus() +
                        ", Priority: " + task.getPriority())
        );
    }

}