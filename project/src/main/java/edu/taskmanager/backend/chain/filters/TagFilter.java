package edu.taskmanager.backend.chain.filters;


import java.util.Map.Entry;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;


/**
 * Фильтр, пропускающий задачи, содержащие заданный тег.
 */
public class TagFilter implements TaskFilter, FilterFactory {
    private final Tag requiredTag;
    private TaskFilter next;

    public TagFilter() {
        this.requiredTag = null;
    }

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

    // @Override
    // public TaskFilter create(Entry<String, String> criteria) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'create'");
    // }
}
