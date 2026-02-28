package edu.taskmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project {
    private Long id;
    private String name;
    private String description;
    private List<Project> tasks;

    // Конструкторы
    public Project() {
        this.tasks = new ArrayList<>();
    }
    public Project(Long id, String name, String description, List<Project> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Project> getTasks() {
        return tasks;
    }

    public void setTasks(List<Project> tasks) {
        this.tasks = tasks;
    }


    // addTask и removeTask
    public void addTask(Project task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    public void removeTask(Project task) {
        tasks.remove(task);
    }

    // equals и hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Project)) return false;
        Project project = (Project) obj;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) && Objects.equals(tasks, project.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, tasks);
    }

    // toString для ввода
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Project {")
                .append("\n id=").append(id)
                .append("\n name").append(name).append('\'')
                .append("\n description").append(description).append('\'')
                .append("\n tasks").append(tasks)
                .append("\n}");
        return builder.toString();
    }

    // Метод main для тестирования
    public static void main(String[] args) {
        Project mainProject = new Project(
                1L,
                ": Main Project",
                ": Root project",
                new ArrayList<Project>()
        );
        Project task1 = new Project(2L, ": Task 1", ": First task", null);
        Project task2 = new Project(3L, ": Task 2", ": Second task", null);

        // Добавляем задачи Task 1 и Task 2
        mainProject.addTask(task1);
        mainProject.addTask(task2);

        System.out.println("После добавления задач: ");
        System.out.println(mainProject);

        // Удаляем задачу Task 1
        mainProject.removeTask(task1);

        System.out.println("\nПосле удаления Task 1: ");
        System.out.println(mainProject);
    }
}
