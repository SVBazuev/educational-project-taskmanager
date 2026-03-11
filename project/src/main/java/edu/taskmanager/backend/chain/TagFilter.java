package edu.taskmanager.backend.chain;


import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;


/**
 * Фильтр, пропускающий задачи, содержащие заданный тег.
 */
public class TagFilter implements TaskFilter {
    private final Tag requiredTag;
    private TaskFilter next;

    public TagFilter(Tag requiredTag) {
        this.requiredTag = requiredTag;
    }

    @Override
    public boolean filter(Task task) {
        boolean hasTag = (
            task.getTags() != null
            && task.getTags().contains(requiredTag)
        );

        if (!hasTag) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}
