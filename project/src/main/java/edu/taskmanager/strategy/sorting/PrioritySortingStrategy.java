package edu.taskmanager.strategy.sorting;

import edu.taskmanager.model.Task;
import java.util.Comparator;
import java.util.List;

/**
 * Сортировка по приоритету (высокий → низкий) с использованием сортировки вставками.
 */
public class PrioritySortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        // Компаратор для сортировки по убыванию приоритета
        Comparator<Task> comparator = Comparator.comparing((Task task) -> task.getPriority().ordinal()).reversed();

        // Реализация сортировки вставками
        for (int i = 1; i < tasks.size(); i++) {
            Task key = tasks.get(i);
            int j = i - 1;

            // Сдвигаем элементы вправо, если они меньше текущего элемента
            while (j >= 0 && comparator.compare(tasks.get(j), key) > 0) {
                tasks.set(j + 1, tasks.get(j));
                j--;
            }

            // Вставляем текущий элемент на правильное место
            tasks.set(j + 1, key);
        }

        return tasks;
    }
}
