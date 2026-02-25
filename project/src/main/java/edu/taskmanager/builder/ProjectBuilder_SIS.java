package edu.taskmanager.builder;

import edu.taskmanager.model.Project_SIS;
import java.util.ArrayList;
import java.util.List;

public class ProjectBuilder_SIS {
    private String name;
    private String description;
    private List<Project_SIS> tasks = new ArrayList<>();

    public ProjectBuilder_SIS setName(String name) {
        this.name = name;
        return this;
    }

    public ProjectBuilder_SIS setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProjectBuilder_SIS addTask(Project_SIS task) {
        this.tasks.add(task);
        return this;
    }

    // Проверка обязательного поля name
    private void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name is required");
        }
        return new Project_SIS(name, description, new ArrayList<>(tasks));
        }

    public void Project_SIS(String name, String description, List<Project_SIS> tasks) {
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }
}
