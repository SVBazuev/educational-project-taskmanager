package edu.taskmanager.backend.strategy.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.taskmanager.backend.model.Task;

/**
 * Сортировка задач с использованием быстрой сортировки (Quick Sort).
 * По умолчанию сортирует по приоритету (от высокого к низкому).
 * Также поддерживает передачу произвольного компаратора через статический метод.
 */
public class QuickTaskSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        if (tasks.size() <= 1) {
            return tasks;
        }
        // Копия, чтоб не изменять исходный список
        List<Task> sortedTasks = new ArrayList<>(tasks);
        quickSortRecursive(sortedTasks, 0, sortedTasks.size() - 1, comparator);

        return sortedTasks;
    }

    /*public static List<Task> quickSort(List<Task> tasks, Comparator<Task> comparator) {
        if (tasks.size() <= 1) {
            return tasks;
        }
        // Копия, чтоб не изменять исходный список
        List<Task> sortedTasks = new ArrayList<>(tasks);
        quickSortRecursive(sortedTasks, 0, sortedTasks.size() - 1, comparator);

        return sortedTasks;
    }/*

    /*
     * Рекурсивная функция для быстрой сортировки.
     *
     * @param tasks      список задач для сортировки
     * @param low        левая граница подмассива
     * @param high       права граница подмассива
     * @param comparator компаратор для сравнения задач
     */
    private static void quickSortRecursive(List<Task> tasks, int low, int high, Comparator<Task> comparator) {
        if (low < high) {
            int pivotIndex = partition(tasks, low, high, comparator);
            quickSortRecursive(tasks, low, pivotIndex - 1, comparator);
            quickSortRecursive(tasks, pivotIndex + 1, high, comparator);
        }
    }

    /*
     * Функция для разбиения подмассива относительно опорного элемента.
     * В качестве pivot выберем последний элемент подмассива.
     *
     * @param tasks      список задач для сортировки
     * @param low        левая граница подмассива
     * @param high       правая граница подмассива
     * @param comparator компаратор для сравнения задач
     * @return индекс опорного элемента после разбиения
     */
    private static int partition(List<Task> tasks, int low, int high, Comparator<Task> comparator) {
        Task pivot = tasks.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(tasks.get(j), pivot) <= 0) {
                i++;
                Task temp = tasks.get(i);
                tasks.set(i, tasks.get(j));
                tasks.set(j, temp);
            }
        }

        // Возвращаем pivot на его правильную позицию
        Task temp = tasks.get(i + 1);
        tasks.set(i + 1, tasks.get(high));
        tasks.set(high, temp);

        return i + 1;
    }
}
