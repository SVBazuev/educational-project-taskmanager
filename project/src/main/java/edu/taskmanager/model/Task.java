package edu.taskmanager.model;
import edu.taskmanager.util.Priority;
import java.time.LocalDateTime;
import java.util.Arrays; 
import java.util.List;
import java.util.Objects;
public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority; 
    private String status;   
    private String project;
    private List<String> tags;
    private List<Task> subtasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Конструкторы
    public Task() {
    }

    public Task(Long id, String title, String description, LocalDateTime dueDate, String priority, String status,
                   String project, List<String> tags, List<Task> subtasks, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.project = project;
        this.tags = tags;
        this.subtasks = subtasks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Task> subtasks) {
        this.subtasks = subtasks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) &&
               Objects.equals(description, task.description) && Objects.equals(dueDate, task.dueDate) &&
               Objects.equals(priority, task.priority) && Objects.equals(status, task.status) &&
               Objects.equals(project, task.project) && Objects.equals(tags, task.tags) &&
               Objects.equals(subtasks, task.subtasks) && Objects.equals(createdAt, task.createdAt) &&
               Objects.equals(updatedAt, task.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, dueDate, priority, status, project, tags, subtasks, createdAt, updatedAt);
    }

    // Новый метод toStringBuilder
    public String toStringBuilder() {
        StringBuilder builder = new StringBuilder();
        builder.append("Task {")
               .append("\n  id=").append(id)
               .append(",\n  title='").append(title).append('\'')
               .append(",\n  description='").append(description).append('\'')
               .append(",\n  dueDate=").append(dueDate)
               .append(",\n  priority='").append(priority).append('\'')
               .append(",\n  status='").append(status).append('\'')
               .append(",\n  project='").append(project).append('\'')
               .append(",\n  tags=").append(tags)
               .append(",\n  subtasks=").append(subtasks)
               .append(",\n  createdAt=").append(createdAt)
               .append(",\n  updatedAt=").append(updatedAt)
               .append("\n}");
        return builder.toString();
    }

    // Метод main для тестирования
    public static void main(String[] args) {
        // Создаем объект Task
        Task task = new Task(
            1L,
            "Test Task",
            "This is a test task.",
            LocalDateTime.now().plusDays(1),
            "High",
            "Open",
            "Project A",
            Arrays.asList("tag1", "tag2"), // Используем Arrays.asList
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Вывод объекта на экран с использованием toStringBuilder
        System.out.println(task.toStringBuilder());
    }
}

