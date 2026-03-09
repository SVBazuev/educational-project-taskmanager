package edu.taskmanager.strategy.sorting;
import java.util.Comparator;
import java.util.List;

import edu.taskmanager.model.Task;


/**
 * Сортировка по приоритету (высокий → низкий) с использованием шейкерной сортировки (Cocktail Sort).
 */
public class PrioritySortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        // Компаратор для сортировки по убыванию приоритета
        Comparator<Task> comparator = Comparator.comparing(task -> task.getPriority().ordinal(), Comparator.reverseOrder());

        // Реализация шейкерной сортировки (Cocktail Sort)
        boolean swapped;
        int start = 0;
        int end = tasks.size() - 1;

        do {
            swapped = false;

            // Проход слева направо (как в пузырьковой сортировке)
            for (int i = start; i < end; i++) {
                if (comparator.compare(tasks.get(i), tasks.get(i + 1)) > 0) {
                    // Меняем элементы местами
                    Task temp = tasks.get(i);
                    tasks.set(i, tasks.get(i + 1));
                    tasks.set(i + 1, temp);
                    swapped = true;
                }
            }

            // Если не было обменов, массив уже отсортирован
            if (!swapped) {
                break;
            }

            // Уменьшаем правую границу, так как последний элемент уже на своем месте
            end--;
            swapped = false;

            // Проход справа налево
            for (int i = end - 1; i >= start; i--) {
                if (comparator.compare(tasks.get(i), tasks.get(i + 1)) > 0) {
                    // Меняем элементы местами
                    Task temp = tasks.get(i);
                    tasks.set(i, tasks.get(i + 1));
                    tasks.set(i + 1, temp);
                    swapped = true;
                }
            }

            // Увеличиваем левую границу
            start++;

        } while (swapped);

        return tasks;
    }
}
