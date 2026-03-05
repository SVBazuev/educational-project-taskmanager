package edu.taskmanager.strategy.sorting;

import edu.taskmanager.model.Task;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * Сортировка по приоритету (высокий → низкий) с использованием массива и итератора.
 */
public class PrioritySortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        // Преобразуем список в массив
        Task[] taskArray = tasks.toArray(new Task[0]);

        // Сортируем массив с использованием компаратора
        Arrays.sort(taskArray, Comparator.comparing((Task task) -> task.getPriority().ordinal()).reversed());

        // Обновляем оригинальный список через итератор
        ListIterator<Task> iterator = tasks.listIterator();
        for (Task task : taskArray) {
            iterator.next();
            iterator.set(task);
        }

        return tasks;
    }
}
