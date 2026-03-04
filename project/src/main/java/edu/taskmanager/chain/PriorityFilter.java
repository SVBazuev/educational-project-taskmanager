package edu.taskmanager.chain;


import edu.taskmanager.model.Task;
import edu.taskmanager.util.Priority;


/**
 * Фильтр, пропускающий задачи только с заданным приоритетом.
 */
public class PriorityFilter implements TaskFilter {
    private final Priority requiredPriority;
    private TaskFilter next;

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
}
