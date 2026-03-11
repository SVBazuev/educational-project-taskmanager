package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class BubblesSortEvenIdsOnly {

    public static List<Task> bubbleSortEvenIds(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return tasks;
        }

        List<Task> result = new ArrayList<>(tasks);

        // Собираем все чётные задачи
        List<Task> evenTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getId() % 2 == 0) {
                evenTasks.add(task);
            }
        }

        sortEvenTasks(evenTasks);

        int evenIndex = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() % 2 == 0) {
                result.set(i, evenTasks.get(evenIndex++));
            }
        }

        return result;
    }

    private static void sortEvenTasks(List<Task> evenTasks) {
        int n = evenTasks.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (evenTasks.get(j).getId() > evenTasks.get(j + 1).getId()) {
                    // Обмен
                    Task temp = evenTasks.get(j);
                    evenTasks.set(j, evenTasks.get(j + 1));
                    evenTasks.set(j + 1, temp);
                }
            }
        }
    }
}


