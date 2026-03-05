package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import edu.taskmanager.util.Priority;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Фильтр задач по приоритету.
 */
public class PriorityFilter implements TaskFilter {
    private final Priority priority;

    public PriorityFilter(Priority priority) {
        this.priority = priority;
    }

    @Override
    public List<Task> apply(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }
}
