package edu.taskmanager.backend.chain.filters;


import java.util.Map;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;


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
    public TaskFilter create(Map.Entry<String, String> criteria)
    throws IllegalArgumentException  {
        Priority priority = Priority.valueOf(
            criteria.getValue().toUpperCase()
        );

        return new PriorityFilter(priority);
    }
}
