package edu.taskmanager.backend.chain.filters;


import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;

import java.util.Map;


/**
 * Фильтр, пропускающий задачи только с заданным приоритетом.
 */
public class PriorityFilter implements TaskFilter, FilterFactory {
    private final Priority requiredPriority;
    private TaskFilter next;

    public PriorityFilter() {
        this.requiredPriority = null;
    }

    public PriorityFilter(Priority requiredPriority) {
        this.requiredPriority = requiredPriority;
    }

    @Override
    public boolean filter(Task task) {
        boolean priorityMatches = task.getPriority() == requiredPriority;

        if (!priorityMatches) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter createFilter(Map.Entry<String, String> value)
    throws IllegalArgumentException {
        Priority priority = Priority.valueOf(value.getValue().toUpperCase());
        TaskFilter filter = new PriorityFilter(priority);
        return filter;
    }
}
