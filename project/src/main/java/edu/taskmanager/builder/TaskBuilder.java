package edu.taskmanager.builder;
import edu.taskmanager.model.Task;
import edu.taskmanager.util.Priority;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

public class TaskBuilder {
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

    // Метод для установки id
    public TaskBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    // Метод для установки title
    public TaskBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    // Метод для установки description
    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    // Метод для установки dueDate
    public TaskBuilder setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    // Метод для установки priority
    public TaskBuilder setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    // Метод для установки status
    public TaskBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    // Метод для установки project
    public TaskBuilder setProject(String project) {
        this.project = project;
        return this;
    }

    // Метод для установки tags
    public TaskBuilder setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    // Метод для установки subtasks
    public TaskBuilder setSubtasks(List<Task> subtasks) {
        this.subtasks = subtasks;
        return this;
    }

    // Метод для установки createdAt
    public TaskBuilder setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    // Метод для установки updatedAt
    public TaskBuilder setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    // Проверка обязательных полей
    private void validate() {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date is required.");
        }
        if (priority == null || priority.isEmpty()) {
            throw new IllegalArgumentException("Priority is required.");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status is required.");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created date is required.");
        }
    }

    // Метод build() для создания объекта Task
    public Task build() {
        validate(); // Проверяем обязательные поля
        return new Task(
                id,
                title,
                description,
                dueDate,
                priority,
                status,
                project,
                tags,
                subtasks,
                createdAt,
                updatedAt
        );
    }

    // Метод main() для тестирования
    public static void main(String[] args) {
        // Используем TaskBuilder для создания объекта Task
        Task task = new TaskBuilder()
                .setId(1L)
                .setTitle("Example Task")
                .setDescription("This is an example task.")
                .setDueDate(LocalDateTime.now().plusDays(2))
                .setPriority(Priority.HIGH)
                .setStatus("Open")
                .setProject("Project X")
                .setTags(Arrays.asList("example", "task"))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .build();

        // Вывод объекта на экран
        System.out.println(task.toStringBuilder());
    }
}


