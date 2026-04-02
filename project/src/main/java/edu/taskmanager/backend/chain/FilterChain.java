package edu.taskmanager.backend.chain;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import edu.taskmanager.backend.model.Task;


/**
 * Управляет построением цепочки фильтров и её применением к списку задач.
 */
public class FilterChain {
    private final List<TaskFilter> filters = new ArrayList<>();
    private TaskFilter firstFilter;
    private final Map<String, FilterFactory> filterFactories = new HashMap<>();

    public FilterChain() {
        initializeFilterFactories();
    }

    /**
     * Инициализирует маппу фильтр-фабрик.
     * Регистрирует все доступные фабрики по ключам.
     */
    private void initializeFilterFactories() {

        filterFactories.put("project", new ProjectFilter());
        filterFactories.put("user", new CreatorFilter());
        filterFactories.put("priority", new PriorityFilter());
        filterFactories.put("status", new StatusFilter());
        filterFactories.put("description", new DescriptionFilter());
        //filterFactories.put("tag", new TagFilter());
        //filterFactories.put("duestartdate", new DueDateFilter());
        //filterFactories.put("upstartdate", new UpdateDateFilter());
    }

    /**
     * Создает и добавляет фильтр в цепочку на основе ключа и значения.
     * @param entry пара ключ-значение для создания фильтра
     */
    public void create(Map.Entry<String, String> entry) {
        String key = entry.getKey().toLowerCase();
        FilterFactory factory = filterFactories.get(key);
        if (factory == null) {
            return;
        }
        try {
            TaskFilter filter = factory.create(entry);
            addFilter(filter);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при создании фильтра для ключа '" + key + "': " + e.getMessage());
        }
    }

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
