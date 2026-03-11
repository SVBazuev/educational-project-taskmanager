package edu.taskmanager.backend.chain;


import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import edu.taskmanager.backend.model.Task;


/**
 * Управляет построением цепочки фильтров и её применением к списку задач.
 */
public class FilterChain {
    private final List<TaskFilter> filters = new ArrayList<>();
    private TaskFilter firstFilter;

    /**
     * Добавляет фильтр в конец цепочки.
     * Автоматически связывает фильтры между собой.
     * @param filter новый фильтр
     */
    public void addFilter(TaskFilter filter) {
        if (filters.isEmpty()) {
            firstFilter = filter;
        } else {
            // Связываем последний добавленный с новым
            filters.get(filters.size() - 1).setNext(filter);
        }
        filters.add(filter);
    }

    /**
     * Применяет цепочку фильтров к списку задач.
     * @param tasks исходный список задач
     * @return список задач, прошедших все фильтры
     */
    public List<Task> apply(List<Task> tasks) {
        if (firstFilter == null) {
            return tasks; // нет фильтров — возвращаем всё
        }
        return tasks.stream()
                .filter(task -> firstFilter.filter(task))
                .collect(Collectors.toList());
    }

    /**
     * Проверяет, есть ли фильтры в цепочке.
     */
    public boolean isEmpty() {
        return filters.isEmpty();
    }

    /**
     * Очищает цепочку.
     */
    public void clear() {
        filters.clear();
        firstFilter = null;
    }
}
