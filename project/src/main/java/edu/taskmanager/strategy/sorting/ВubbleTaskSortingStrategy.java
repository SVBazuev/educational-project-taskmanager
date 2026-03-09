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
public class ВubbleTaskSortingStrategy {

    public static List<Task> bubbleSort(List<Task> tasks, Comparator<Task> comparator) {
        // Создаем копию списка, чтобы не изменять исходный
        List<Task> sortedTasks = new ArrayList<>(tasks);
        int n = sortedTasks.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(sortedTasks.get(j), sortedTasks.get(j + 1)) > 0) {
                    Task temp = sortedTasks.get(j);
                    sortedTasks.set(j, sortedTasks.get(j + 1));
                    sortedTasks.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        return sortedTasks;
    }
/*
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
*/
}