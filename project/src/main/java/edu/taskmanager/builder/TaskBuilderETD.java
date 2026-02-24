package edu.taskmanager.builder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

public class TaskBuilderETD {
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

    // Метод для установки id
    public TaskBuilderETD setId(Long id) {
        this.id = id;
        return this;
    }

    // Метод для установки title
    public TaskBuilderETD setTitle(String title) {
        this.title = title;
        return this;
    }

    // Метод для установки description
    public TaskBuilderETD setDescription(String description) {
        this.description = description;
        return this;
    }

    // Метод для установки dueDate
    public TaskBuilderETD setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    // Метод для установки priority
    public TaskBuilderETD setPriority(String priority) {
        this.priority = priority;
        return this;
    }

    // Метод для установки status
    public TaskBuilderETD setStatus(String status) {
        this.status = status;
        return this;
    }

    // Метод для установки project
    public TaskBuilderETD setProject(String project) {
        this.project = project;
        return this;
    }

    // Метод для установки tags
    public TaskBuilderETD setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    // Метод для установки subtasks
    public TaskBuilderETD setSubtasks(List<TaskETD> subtasks) {
        this.subtasks = subtasks;
        return this;
    }

    // Метод для установки createdAt
    public TaskBuilderETD setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    // Метод для установки updatedAt
    public TaskBuilderETD setUpdatedAt(LocalDateTime updatedAt) {
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

    // Метод build() для создания объекта TaskETD
    public TaskETD build() {
        validate(); // Проверяем обязательные поля
        return new TaskETD(
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
        // Используем TaskBuilderETD для создания объекта TaskETD
        TaskETD task = new TaskBuilderETD()
                .setId(1L)
                .setTitle("Example Task")
                .setDescription("This is an example task.")
                .setDueDate(LocalDateTime.now().plusDays(2))
                .setPriority("High")
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

