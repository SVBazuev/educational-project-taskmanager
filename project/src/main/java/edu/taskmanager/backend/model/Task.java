package edu.taskmanager.backend.model;


import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.frontend.console.util.LenientObjectIdResolver;


/**
 * Класс представляет задачу в системе.
 * Поля:
 * - id: уникальный идентификатор
 * - title: название задачи
 * - description: описание (может быть null)
 * - dueDate: срок выполнения
 * - priority: приоритет (enum Priority)
 * - status: статус (enum TaskStatus)
 * - project: проект, к которому относится задача (может быть null)
 * - tags: список тегов
 * - subtasks: список подзадач (других объектов Task)
 * - createdAt: дата создания
 * - updatedAt: дата последнего обновления
 * - parentId: идентификатор родительской задачи (если текущая задача является подзадачей)
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Task.class, resolver = LenientObjectIdResolver.class)
public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private User creator;
    private Priority priority;
    private TaskStatus status;
    private Project project;
    private List<Tag> tags;
    private List<Task> subtasks;
    private List<User> contractors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentId;


    /**
     * Конструктор по умолчанию. Инициализирует коллекции.
     */
    public Task() {
        this.tags = new ArrayList<>();
        this.subtasks = new ArrayList<>();
        this.contractors = new ArrayList<>();
    }

    /**
     * Конструктор с основными полями. Остальные поля остаются со значениями по умолчанию.
     *
     * @param title    название задачи (обязательное)
     * @param dueDate  срок выполнения
     * @param priority приоритет
     * @param status   статус
     */
    public Task(
            String title, LocalDateTime dueDate,
            User creator,
            Priority priority, TaskStatus status) {
        this();
        this.title = title;
        this.dueDate = dueDate;
        this.creator = creator;
        this.priority = priority;
        this.status = status;
    }

    // Конструктор копирования
    public Task(Task other) {
        this.title = other.title;
        this.description = other.description;
        this.dueDate = other.dueDate;
        this.creator = other.creator;
        this.priority = other.priority;
        this.status = other.status;
        this.project = other.project;
        this.tags = other.tags;
        this.subtasks = other.subtasks;
        this.contractors = other.contractors;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.parentId = other.parentId;
    }

    // --- Геттеры ---

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public User getCreator() { return creator; }
    public Priority getPriority() { return priority; }
    public TaskStatus getStatus() { return status; }
    public Project getProject() { return project; }
    public List<Tag> getTags() { return tags; }
    public List<Task> getSubtasks() { return subtasks; }
    public List<User> getContractors() { return contractors; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getParentId() { return parentId; }

    // --- Cеттеры ---

    public void setId(Long id) { this.id = id; }

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public void setSubtasks(List<Task> subtasks) {
        this.subtasks = subtasks != null ? subtasks : new ArrayList<>();
    }

    public void setContractors(List<User> contractors) {
        this.contractors = contractors != null ? contractors : new ArrayList<>();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    // --- Вспомогательные методы для работы с коллекциями ---

    /**
     * Добавляет тег к задаче.
     *
     * @param tag тег для добавления
     */
    public void addTag(Tag tag) {
        if (tag != null && !tags.contains(tag)) {
            tags.add(tag);
        }
    }

    /**
     * Удаляет тег из задачи.
     *
     * @param tag тег для удаления
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * Добавляет подзадачу. Устанавливает у подзадачи parentId
     * равный идентификатору текущей задачи,
     * если у текущей задачи уже есть id.
     * В противном случае parentId остаётся null (его можно будет
     * установить позже при сохранении родителя).
     *
     * @param subtask подзадача для добавления
     */
    public void addSubtask(Task subtask) {
        if (subtask != null && !subtasks.contains(subtask)) {
            subtasks.add(subtask);
            // Если у текущей задачи уже есть id,
            if (this.id != null) {
                // связываем подзадачу с родителем
                subtask.setParentId(this.id);
            }
        }
    }

    /**
     * Удаляет подзадачу.
     *
     * @param subtask подзадача для удаления
     */
    public void removeSubtask(Task subtask) {
        if (subtasks.remove(subtask)) {
            subtask.setParentId(null);
        }
    }

    /**
     * Добавляет исполнителя к задаче.
     *
     * @param user тег для добавления
     */
    public void addСontractor(User user) {
        if (user != null && !contractors.contains(user)) {
            contractors.add(user);
        }
    }

    /**
     * Удаляет тег из задачи.
     *
     * @param user тег для удаления
     */
    public void removeContractor(User user) {
        contractors.remove(user);
    }

    // Метод withId, использующий конструктор копирования
    public Task withId(Long newId) {
        Task copy = new Task(this);
        copy.id = newId;
        return copy;
    }

    // --- equals, hashCode, toString ---

    /**
     * Сравнение задач по идентификатору.
     * Если идентификатор не задан (null), задачи считаются разными,
     * если только это не один и тот же объект.
     *
     * @param o объект для сравнения
     * @return true, если идентификаторы равны и не null,
     * либо если это один и тот же объект
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id != null && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }

    /**
     * Возвращает строковое представление задачи.
     * Во избежание рекурсии подзадачи не выводятся подробно,
     * указывается только их количество.
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Task{")
            .append("id=").append(id)
            .append(", title='").append(title).append('\'')
            .append(", description='").append(description).append('\'')
            .append(", dueDate=").append(dueDate)
            .append(", creator=").append(creator)
            .append(", priority=").append(priority)
            .append(", status=").append(status)
            .append(", project=").append((project != null ? project.getName() : "null"))
            .append(", tags=").append(tags.size())
            .append(", subtasks=").append(subtasks.size())
            //TODO выводить список логинов через joined
            .append(", contractors=").append(contractors.size())
            .append(", createdAt=").append(createdAt)
            .append(", updatedAt=").append(updatedAt)
            .append(", parentId=").append(parentId)
            .append('}');

        return builder.toString();
    }

}
