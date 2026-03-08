package edu.taskmanager.strategy.sorting;

import edu.taskmanager.model.Task;
import java.util.Comparator;
import java.util.List;

/**
 * Сортировка задач по дате выполнения (от ближайшей к самой далёкой).
 */

public class DueDateSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        // Компаратор для сортировки по дате выполнения
        Comparator<Task> comparator = Comparator.comparing(Task::getDueDate);

        insertionSort(tasks, comparator);

        return tasks;
    }

    /**
     * Сортировка вставками (Insertion Sort).
     */

    private void insertionSort(List<Task> tasks, Comparator<Task> comparator) {
        for (int i = 1; i < tasks.size(); i++) {
            Task key = tasks.get(i);
            int j = i - 1;

            // Сдвигаем элементы, которые больше key, вправо
            while (j >= 0 && comparator.compare(tasks.get(j), key) > 0) {
                tasks.set(j + 1, tasks.get(j));
                j--;
            }

            // Вставляем key на правильную позицию
            tasks.set(j + 1, key);
        }
    }
}
