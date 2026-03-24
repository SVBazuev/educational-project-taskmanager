package edu.taskmanager.backend.strategy.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.taskmanager.backend.model.Task;

/**
 * Сортировка задач по дате выполнения (от ближайшей к самой далёкой).
 */

public class InsertionTaskSortingStrategy implements TaskSortingStrategy {

    /**
     * Сортировка вставками (Insertion Sort).
     */

    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {

        List<Task> sortedTasks = new ArrayList<>(tasks);

        for (int i = 1; i < sortedTasks.size(); i++) {
            Task key = sortedTasks.get(i);
            int j = i - 1;

            // Сдвигаем элементы, которые больше key, вправо
            while (j >= 0 && comparator.compare(sortedTasks.get(j), key) > 0) {
                sortedTasks.set(j + 1, sortedTasks.get(j));
                j--;
            }

            // Вставляем key на правильную позицию
            sortedTasks.set(j + 1, key);
        }
        return sortedTasks;
    }
}
