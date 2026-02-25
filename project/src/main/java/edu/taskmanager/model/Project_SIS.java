package edu.taskmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project_SIS {
    private Long id;
    private String name;
    private String description;
    private List<Project_SIS> tasks;

    // Конструкторы
    public Project_SIS() {
        this.tasks = new ArrayList<>();
    }
    public Project_SIS(Long id, String name, String description, List<Project_SIS> tasks) {
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

    public List<Project_SIS> getTasks() {
        return tasks;
    }

    public void setTasks(List<Project_SIS> tasks) {
        this.tasks = tasks;
    }


    // addTask и removeTask
    public void addTask(Project_SIS task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    public void removeTask(Project_SIS task) {
        tasks.remove(task);
    }

    // equals и hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Project_SIS)) return false;
        Project_SIS project = (Project_SIS) obj;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) && Objects.equals(tasks, project.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, tasks);
    }

    // toString для ввода
    public String toString() {
        return "Project_SIS{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    // Метод main для тестирования
    public static void main(String[] args) {
        Project_SIS mainProject = new Project_SIS(
                1L,
                "Main Project",
                "Root project",
                null
        );
        Project_SIS task1 = new Project_SIS(2L, "Task 1", "First task", null);
        Project_SIS task2 = new Project_SIS(3L, "Task 2", "Second task", null);

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