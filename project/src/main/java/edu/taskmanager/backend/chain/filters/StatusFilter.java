package edu.taskmanager.backend.chain.filters;


import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.TaskStatus;

import java.util.Map;


/**
 * Фильтр, пропускающий задачи только с заданным статусом.
 */
public class StatusFilter implements TaskFilter, FilterFactory {
    private final TaskStatus requiredStatus;
    private TaskFilter next;

    public StatusFilter(TaskStatus requiredStatus) {
        this.requiredStatus = requiredStatus;
    }

    public StatusFilter() {
        this.requiredStatus = null;
    }

    @Override
    public boolean filter(Task task) {
        boolean statusMatches = task.getStatus() == requiredStatus;

        if (!statusMatches) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter createFilter(Map.Entry<String, String> value) throws IllegalArgumentException {
        TaskStatus statusFilter = TaskStatus.valueOf(value.getValue().toUpperCase());
        TaskFilter filter = new StatusFilter(statusFilter);
        return filter;
    }
}
