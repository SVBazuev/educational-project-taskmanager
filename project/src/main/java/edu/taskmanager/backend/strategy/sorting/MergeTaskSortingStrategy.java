package edu.taskmanager.backend.strategy.sorting;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.taskmanager.backend.model.Task;

/**
 * Сортировка с использованием метода слияния (Merge Sort)
 * с возможностью передачи компаратора
 */
public class MergeTaskSortingStrategy implements TaskSortingStrategy {

    /**
     * Сортирует список задач с использованием сортировки слиянием.
     *
     * @param tasks - исходный список задач
     * @param comparator - компаратор для сравнения задач
     * @return - новый отсортированный список
     */
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        List<Task> sortedTasks = new ArrayList<>(tasks);

        // Вызываем рекурсивную сортировку
        return mergeSortRecursive(sortedTasks, comparator, 0, sortedTasks.size() - 1);
    }

    /**
     * Рекурсивный метод сортировки слиянием
     */
    private static List<Task> mergeSortRecursive(
    List<Task> tasks, Comparator<Task> comparator, int left, int right) {
        if (left < right) {
            // Находим середину массива
            int middle = (left + right) / 2;

            // Рекурсивно сортируем левую и правую части
            mergeSortRecursive(tasks, comparator, left, middle);
            mergeSortRecursive(tasks, comparator, middle + 1, right);

            // Сливаем отсортированные части
            merge(tasks, comparator, left, middle, right);
        }
        return tasks;
    }

    /**
     * Метод слияния двух отсортированных частей
     */
    private static void merge(
    List<Task> tasks, Comparator<Task> comparator,
    int left, int middle, int right) {
        // Вычисляем размеры частей массива (левой и правой части)
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // Создаем временные списки
        List<Task> leftHalf = new ArrayList<>();
        List<Task> rightHalf = new ArrayList<>();

        // Копируем данные во временные списки
        for (int i = 0; i < n1; i++) {
            leftHalf.add(tasks.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightHalf.add(tasks.get(middle + 1 + j));
        }

        // Индексы для обхода временных списков
        int i = 0, j = 0;
        int k = left; // Индекс для основного списка

        // Слияние двух левой и правой части массива
        while (i < n1 && j < n2) {
            if (comparator.compare(leftHalf.get(i), rightHalf.get(j)) <= 0) {
                tasks.set(k, leftHalf.get(i));
                i++;
            } else {
                tasks.set(k, rightHalf.get(j));
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы из левой части массива
        while (i < n1) {
            tasks.set(k, leftHalf.get(i));
            i++;
            k++;
        }

        // Копируем оставшиеся элементы из правой части массива
        while (j < n2) {
            tasks.set(k, rightHalf.get(j));
            j++;
            k++;
        }
    }
}
