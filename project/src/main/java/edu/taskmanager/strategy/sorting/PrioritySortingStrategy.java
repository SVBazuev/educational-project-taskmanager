package edu.taskmanager.strategy.sorting;

import edu.taskmanager.model.Task;
import java.util.Comparator;
import java.util.List;

/**
 * Сортировка по приоритету (высокий → низкий) с использованием пузырьковой сортировки.
 */
public class PrioritySortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        // Компаратор для сортировки по убыванию приоритета
        Comparator<Task> comparator = Comparator.comparing((Task task) -> task.getPriority().ordinal()).reversed();

        // Выполняем пузырьковую сортировку
        bubbleSort(tasks, comparator);

        return tasks;
    }

    /**
     * Метод пузырьковой сортировки.
     */
    private static void bubbleSort(List<Task> tasks, Comparator<Task> comparator) {
        int n = tasks.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(tasks.get(j), tasks.get(j + 1)) > 0) {
                    // Меняем местами задачи
                    Task temp = tasks.get(j);
                    tasks.set(j, tasks.get(j + 1));
                    tasks.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break; // Если не было обменов, список уже отсортирован
        }
    }
}
