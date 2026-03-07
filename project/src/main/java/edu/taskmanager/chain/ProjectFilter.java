package edu.taskmanager.chain;


import edu.taskmanager.model.Project;
import edu.taskmanager.model.Task;


/**
 * Фильтр, пропускающий задачи, принадлежащие заданному проекту.
 */
public class ProjectFilter implements TaskFilter {
    private final Project requiredProject;
    private TaskFilter next;

    public ProjectFilter(Project requiredProject) {
        this.requiredProject = requiredProject;
    }

    @Override
    public boolean filter(Task task) {
        boolean belongsToProject = (
            task.getProject() != null
            && task.getProject().equals(requiredProject)
        );

        if (!belongsToProject) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}
