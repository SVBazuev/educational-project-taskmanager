package edu.taskmanager.backend.builder;


import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;


/**
 * Построитель для объекта Task.
 * Позволяет пошагово задавать параметры задачи и создаёт готовый объект.
 * Обязательные поля: title, dueDate.
 * Если они не заданы, build() выбросит исключение.
 */
public class TaskBuilder {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority = Priority.MEDIUM; // значение по умолчанию
    private TaskStatus status = TaskStatus.TODO; // значение по умолчанию
    private Project project;
    private List<Tag> tags = new ArrayList<>();
    private List<Task> subtasks = new ArrayList<>();
    private Long parentId;

    // ===== Методы для установки полей =====

    public TaskBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskBuilder setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public TaskBuilder setStatus(TaskStatus status) {
        this.status = status;
        return this;
    }

    public TaskBuilder setProject(Project project) {
        this.project = project;
        return this;
    }

    /**
     * Заменяет список тегов целиком.
     */
    public TaskBuilder setTags(List<Tag> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
        return this;
    }

    /**
     * Добавляет один тег к задаче.
     */
    public TaskBuilder addTag(Tag tag) {
        if (tag != null && !this.tags.contains(tag)) {
            this.tags.add(tag);
        }
        return this;
    }

    /**
     * Заменяет список подзадач целиком.
     */
    public TaskBuilder setSubtasks(List<Task> subtasks) {
        this.subtasks = subtasks != null ? subtasks : new ArrayList<>();
        return this;
    }

    /**
     * Добавляет одну подзадачу.
     * Связь parentId будет установлена позже (при сохранении родителя).
     */
    public TaskBuilder addSubtask(Task subtask) {
        if (subtask != null && !this.subtasks.contains(subtask)) {
            this.subtasks.add(subtask);
        }
        return this;
    }

    /**
     * Устанавливает идентификатор родительской задачи
     * (если задача создаётся как подзадача).
     */
    public TaskBuilder setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    // ===== Сборка объекта =====

    /**
     * Создаёт объект Task на основе заданных параметров.
     * Перед созданием проверяет наличие обязательных полей: title и dueDate.
     *
     * @return готовый объект Task
     * @throws IllegalStateException если title или dueDate не заданы
     */
    public Task build() {
        // Проверка обязательных полей
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalStateException(
                "Название задачи (title) не может быть пустым"
            );
        }
        if (dueDate == null) {
            throw new IllegalStateException(
                "Срок выполнения (dueDate) должен быть задан"
            );
        }

        // Создаём задачу
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setPriority(priority);
        task.setStatus(status);
        task.setProject(project);
        task.setTags(tags);
        task.setSubtasks(subtasks);
        task.setParentId(parentId);

        // Устанавливаем временные метки
        task.setCreatedAt(LocalDateTime.now());
        // updatedAt оставляем null (заполняется при первом изменении)

        // Для подзадач, добавленных через билдер,
        // parentId пока не установлен,
        // так как родитель ещё не имеет id.
        // Это будет сделано в репозитории.
        // Однако если мы явно задали parentId через билдер
        // (например, при создании подзадачи),
        // то он уже проставлен.

        return task;
    }
}
