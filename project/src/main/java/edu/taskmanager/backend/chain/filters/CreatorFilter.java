package edu.taskmanager.backend.chain.filters;


import java.util.Map;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.util.Priority;


/**
 * Фильтр, пропускающий задачи, принадлежащие заданному пользователю.
 */
public class CreatorFilter implements TaskFilter, FilterFactory {
    private final User requiredUser;
    private TaskFilter next;

    public CreatorFilter() {
        this.requiredUser = null;
    }

    public CreatorFilter(User requiredUser) {
        this.requiredUser = requiredUser;
    }

    @Override
    public boolean filter(Task task) {
        boolean belongsToUser = (
            task.getCreator() != null
            && task.getCreator().equals(requiredUser)
        );

        if (!belongsToUser) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter create(Map.Entry<String, String> criteria)
    throws IllegalArgumentException {
        Priority priority = Priority.valueOf(criteria.getValue().toUpperCase());
        TaskFilter filter = new PriorityFilter(priority);
        return filter;
    }
}
