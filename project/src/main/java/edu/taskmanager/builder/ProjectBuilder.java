package edu.taskmanager.builder;

import edu.taskmanager.model.Project;
import java.util.ArrayList;
import java.util.List;

public class ProjectBuilder {
    private String name;
    private String description;
    private List<Project> tasks = new ArrayList<>();

    public ProjectBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProjectBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProjectBuilder addTask(Project task) {
        this.tasks.add(task);
        return this;
    }

    // Проверка обязательного поля name
    private void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name is required");
        }
        return new Project(name, description, new ArrayList<>(tasks));
    }

    // Метод build() для создания объекта Project
    public Project build() {
        validate();
        return new Project(name, description, new ArrayList<>(tasks));
    }

    // Метод Main() для тестирования
    public static void main(String[] args) {

        // Создаем подзадачу
        Project subTask = new ProjectBuilder()
                .setName("Subtask 1")
                .setDescription("First subtask")
                .build();

        // Создаем основной проект
        Project mainProject = new ProjectBuilder()
                .setName("Main project")
                .setDescription("Main project description")
                .addTask(subTask)
                .build();

        System.out.println(mainProject);

        // Проверка валидации (ниже необходимо раскомментировать для проверки)
        /*
        Project invalidProject = new ProjectBuilder()
                .setDescription("No name project")
                .build();

         */
    }
}
