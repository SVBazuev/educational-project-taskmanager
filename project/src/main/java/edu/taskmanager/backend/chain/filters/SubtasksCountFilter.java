package edu.taskmanager.backend.chain.filters;

import java.util.List;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;

/**
 * Фильтр, пропускающий задачи с количеством подзадач больше или равным заданному числу.
 */
public class SubtasksCountFilter implements TaskFilter, FilterFactory {
    private final int minSubtasks;
    private TaskFilter next;

    public SubtasksCountFilter(int minSubtasks) {
        this.minSubtasks = minSubtasks;
    }

    @Override
    public boolean filter(Task task) {
        List<Task> subtasks = task.getSubtasks();
        boolean subtasksMatch = subtasks != null && subtasks.size() >= minSubtasks;

        if (!subtasksMatch) {
            return false;
        }

        if (next != null) {
            return next.filter(task);
        }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}
