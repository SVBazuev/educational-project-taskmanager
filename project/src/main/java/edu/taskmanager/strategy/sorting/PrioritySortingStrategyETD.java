package edu.taskmanager.strategy.sorting;
import java.util.Comparator;
import java.util.List;
import edu.taskmanager.util.PriorityETD;

/**
 * Сортировка по приоритету (высокий → низкий).
 */
public class PrioritySortingStrategyETD implements TaskSortingStrategy {

    @Override
    public List<TaskETD> sort(List<TaskETD> tasks) {
        // Используем компаратор для сортировки по приоритету
        tasks.sort(Comparator.comparing(task -> task.getPriority().ordinal()));
        return tasks;
    }
}
