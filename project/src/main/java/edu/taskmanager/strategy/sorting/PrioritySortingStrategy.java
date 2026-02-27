package edu.taskmanager.strategy.sorting;

import edu.taskmanager.model.Task;
import edu.taskmanager.util.Priority;
import java.util.Comparator;
import java.util.List;

/**
 * Сортировка по приоритету (высокий → низкий).
 */
public class PrioritySortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        // Используем компаратор для сортировки по приоритету
    tasks.sort(Comparator.comparing((Task task) -> task.getPriority().ordinal()).reversed());
        return tasks;
    }
}

