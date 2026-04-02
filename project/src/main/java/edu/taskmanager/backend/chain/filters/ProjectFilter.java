package edu.taskmanager.backend.chain.filters;


import java.util.Map;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Task;


/**
 * Фильтр, пропускающий задачи, принадлежащие заданному проекту.
 */
public class ProjectFilter implements TaskFilter, FilterFactory {
    private final Long projectId;
    private final String projectName;
    private TaskFilter next;

    public ProjectFilter() {
        this.projectId = null;
        this.projectName = null;
    }

    public ProjectFilter(Long projectId) {
        this.projectId = projectId;
        this.projectName = null;
    }

    public ProjectFilter(String projectName) {
        this.projectId = null;
        this.projectName = projectName;
    }

    @Override
    public boolean filter(Task task) {
        boolean belongsToProject;
        if (projectId == null) {
            belongsToProject = (
                task.getProject() != null
                && task.getProject().getName().equals(projectName)
            );
        } else {
            belongsToProject = (
                task.getProject() != null
                && task.getProject().getId() == projectId
            );
        }

        if (!belongsToProject) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter create(Map.Entry<String, String> criteria)
    throws IllegalArgumentException  {
        if (criteria.getValue().)
        Priority priority = Priority.valueOf(
            criteria.getValue().toUpperCase()
        );

        return new PriorityFilter(priority);
    }
}
