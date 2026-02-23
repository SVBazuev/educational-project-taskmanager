import java.time.LocalDateTime;
import java.util.Arrays; 
import java.util.List;
import java.util.Objects;

public class TaskETD {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority; 
    private String status;   
    private String project;
    private List<String> tags;
    private List<TaskETD> subtasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Конструкторы
    public TaskETD() {
    }

    public TaskETD(Long id, String title, String description, LocalDateTime dueDate, String priority, String status,
                   String project, List<String> tags, List<TaskETD> subtasks, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
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

    public List<TaskETD> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<TaskETD> subtasks) {
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
        TaskETD task = (TaskETD) o;
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

    // Для удобства можно добавить toString
    @Override
    public String toString() {
        return "TaskETD{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", project='" + project + '\'' +
                ", tags=" + tags +
                ", subtasks=" + subtasks +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Метод main для тестирования
    public static void main(String[] args) {
        // Создаем объект TaskETD
        TaskETD task = new TaskETD(
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

        // Вывод объекта на экран
        System.out.println(task);
    }
}
