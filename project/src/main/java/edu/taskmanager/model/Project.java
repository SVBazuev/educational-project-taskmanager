package edu.taskmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс представляет проект — группу задач.
 * Поля:
 * - id
 * - name
 * - description
 * - tasks: список задач, входящих в проект
 */
public class Project {
    private Long id;
    private String name;
    private String description;
    private List<Task> tasks;

    /**
     * Конструктор по умолчанию. Инициализирует список задач.
     */
    public Project() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Конструктор с названием.
     *
     * @param name название проекта
     */
    public Project(String name) {
        this();
        this.name = name;
    }

    /**
     * Конструктор со всеми полями для .
     *
     * @param id          идентификатор
     * @param name        название
     * @param description описание
     * @param tasks       список задач
     */
    public Project(Long id, String name, String description, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    /**
     * Конструктор копирования.
     * Основное предназначение заполнение поля id,
     * после сохранения в репозитории.
     *
     * @param other       экземпляр Project
     */
    public Project(Project other) {
        this.name = other.name;
        this.description = other.description;
        this.tasks = other.tasks != null ? other.tasks : new ArrayList<>();
    }

    // --- Геттеры ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Task> getTasks() { return tasks; }

    // --- Cеттеры ---

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    // Метод withId, использующий конструктор копирования
    public Project withId(Long newId) {
        Project copy = new Project(this);
        copy.id = newId;
        return copy;
    }

    // --- Вспомогательные методы для управления задачами ---

    /**
     * Добавляет задачу в проект. Если задача уже принадлежит другому проекту,
     * её связь с тем проектом будет заменена на текущий.
     *
     * @param task задача для добавления
     */
    public void addTask(Task task) {
        if (task != null && !tasks.contains(task)) {
            tasks.add(task);
            task.setProject(this);
        }
    }

    /**
     * Удаляет задачу из проекта. У задачи сбрасывается ссылка на проект.
     *
     * @param task задача для удаления
     */
    public void removeTask(Task task) {
        if (tasks.remove(task)) {
            if (task.getProject() == this) {
                task.setProject(null);
            }
        }
    }

    /**
     * Удаляет все задачи из проекта. У всех задач сбрасывается ссылка на проект.
     */
    public void clearTasks() {
        for (Task task : tasks) {
            if (task.getProject() == this) {
                task.setProject(null);
            }
        }
        tasks.clear();
    }

    // --- equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id != null && Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Project{")
            .append("id=").append(id)
            .append(", name='").append(name).append('\'')
            .append(", description='").append(description).append('\'')
            .append(", tasksCount=")
            .append((tasks != null ? tasks.size() : 0))
            .append('}');

        return builder.toString();
    }

    public static class ProjectBuilder {
        private String name;
        private String description;
        private List<Task> tasks = new ArrayList<>();

        public ProjectBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProjectBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ProjectBuilder addTask(Task task) {
            this.tasks.add(task);
            return this;
        }

        public Project build() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException(
                    "Название проекта (name) не может быть пустым"
                );
            }
            Project project = new Project();
            project.setName(name);
            project.setDescription(description);
            project.setTasks(tasks);

            return project;
        }
    }
}
