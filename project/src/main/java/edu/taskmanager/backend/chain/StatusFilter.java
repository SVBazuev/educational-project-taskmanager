package edu.taskmanager.backend.chain;


import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.TaskStatus;


/**
 * Фильтр, пропускающий задачи только с заданным статусом.
 */
public class StatusFilter implements TaskFilter {
    private final TaskStatus requiredStatus;
    private TaskFilter next;

    public StatusFilter(TaskStatus requiredStatus) {
        this.requiredStatus = requiredStatus;
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

}
