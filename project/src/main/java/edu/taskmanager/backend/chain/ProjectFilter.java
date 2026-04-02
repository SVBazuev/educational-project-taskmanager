package edu.taskmanager.backend.chain;


import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.UserRepository;

import java.util.Map;
import java.util.Optional;


/**
 * Фильтр, пропускающий задачи, принадлежащие заданному проекту.
 */
public class ProjectFilter implements TaskFilter, FilterFactory {
    private TaskFilter next;
    private Long  id;
    private  String name;


    public ProjectFilter() {
    }

    public ProjectFilter(Long  id){
        this.id = id;
    }

    public ProjectFilter(String name){
        this.name = name;
    }

    @Override
    public boolean filter(Task task) {
        boolean belongsToProject = (
            task.getProject() != null
            && (task.getProject().getId().equals(id)
                || task.getProject().getName().equals(name))
        );

        if (!belongsToProject) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter createFilter(Map.Entry<String, String> entry) throws IllegalArgumentException {
        try {
            long projectId = Long.parseLong(entry.getValue());
            return new ProjectFilter(projectId);//filter;
        } catch (NumberFormatException e) {
            return new ProjectFilter(entry.getValue());
        }
    }
}
