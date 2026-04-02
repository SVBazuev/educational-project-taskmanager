package edu.taskmanager.backend.chain;


import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;


/**
 * Фильтр, пропускающий задачи, содержащие заданный тег.
 */
public class TagFilter implements TaskFilter {
    //private final Tag requiredTag;
    private TaskFilter next;
    private Long id;
    private  String name;


    public TagFilter() {
    }

    public TagFilter( Long  id){
        this.id = id;
    }

    public TagFilter( String name){
        this.name = name;
    }

    @Override
    public boolean filter(Task task) {
        boolean hasTag = (
            task.getTags() != null
            && task.getTags().stream()
                    .anyMatch(tag -> tag.getId().equals(id))
                || task.getTags() != null
                    && task.getTags().stream()
                    .anyMatch(tag -> tag.getName().equals(name))
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
