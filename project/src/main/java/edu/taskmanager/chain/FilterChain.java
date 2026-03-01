package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import java.util.ArrayList;
import java.util.List;

/**
 * Управляет цепочкой фильтров и применяет её к списку задач.
 */
public class FilterChain {
    private final List<TaskFilter> filters = new ArrayList<>();

    /**
     * Добавляет фильтр в цепочку.
     * @param filter Фильтр для добавления.
     */
    public void addFilter(TaskFilter filter) {
        filters.add(filter);
    }

    /**
     * Применяет цепочку фильтров к списку задач.
     * @param tasks Список задач для фильтрации.
     * @return Список задач, прошедших все фильтры.
     */
    public List<Task> apply(List<Task> tasks) {
        List<Task> filteredTasks = new ArrayList<>(tasks);

        for (TaskFilter filter : filters) {
            filteredTasks = filter.apply(filteredTasks);
        }

        return filteredTasks;
    }
}
