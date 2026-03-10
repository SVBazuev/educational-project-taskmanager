package edu.taskmanager.strategy.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.taskmanager.model.Task;

/**
 * Сортировка с использованием шейкерной сортировки (Cocktail Sort)
 * с возможностью передачи компаратора.
 */
public class CocktailTaskSortingStrategy {

    /**
     * Сортирует список задач с использованием шейкерной сортировки.
     *
     * @param tasks      исходный список задач
     * @param comparator компаратор для сравнения задач
     * @return новый отсортированный список
     */
    public static List<Task> cocktailSort(List<Task> tasks, Comparator<Task> comparator) {
        // Создаем копию списка, чтобы не изменять исходный
        List<Task> sortedTasks = new ArrayList<>(tasks);
        
        int start = 0;
        int end = sortedTasks.size() - 1;
        boolean swapped;

        do {
            swapped = false;

            // Проход слева направо
            for (int i = start; i < end; i++) {
                if (comparator.compare(sortedTasks.get(i), sortedTasks.get(i + 1)) > 0) {
                    Task temp = sortedTasks.get(i);
                    sortedTasks.set(i, sortedTasks.get(i + 1));
                    sortedTasks.set(i + 1, temp);
                    swapped = true;
                }
            }

            if (!swapped) break;

            end--;
            swapped = false;

            // Проход справа налево
            for (int i = end - 1; i >= start; i--) {
                if (comparator.compare(sortedTasks.get(i), sortedTasks.get(i + 1)) > 0) {
                    Task temp = sortedTasks.get(i);
                    sortedTasks.set(i, sortedTasks.get(i + 1));
                    sortedTasks.set(i + 1, temp);
                    swapped = true;
                }
            }

            start++;

        } while (swapped);

        return sortedTasks;
    }
}