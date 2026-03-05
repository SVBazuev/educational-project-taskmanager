package edu.taskmanager.chain;


import edu.taskmanager.model.Task;
import edu.taskmanager.model.User;


/**
 * Фильтр, пропускающий задачи, принадлежащие заданному пользователю.
 */
public class CreatorFilter implements TaskFilter {
    private final User requiredUser;
    private TaskFilter next;

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
}
