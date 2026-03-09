package edu.taskmanager.strategy.sorting;
import java.util.Comparator;
import java.util.List;

import edu.taskmanager.model.Task;

/**
 * Сортировка по приоритету (высокий → низкий) с использованием пузырьковой сортировки.
 */
public class PrioritySortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        // Компаратор для сортировки по убыванию приоритета
        Comparator<Task> comparator = Comparator.comparing(task -> task.getPriority().ordinal(), Comparator.reverseOrder());

        // Реализация пузырьковой сортировки
        int n = tasks.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Сравниваем соседние элементы
                if (comparator.compare(tasks.get(j), tasks.get(j + 1)) > 0) {
                    // Меняем местами, если порядок неправильный
                    Task temp = tasks.get(j);
                    tasks.set(j, tasks.get(j + 1));
                    tasks.set(j + 1, temp);
                }
            }
        }

        return tasks;
    }
}