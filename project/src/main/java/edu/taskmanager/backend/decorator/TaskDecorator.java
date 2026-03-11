package edu.taskmanager.backend.decorator;

import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;

import java.time.LocalDateTime;

public abstract class TaskDecorator {
    protected Task wrappedTask;

    protected TaskDecorator(Task task){
        if(task == null){
            throw new IllegalArgumentException("Task cannot be null");
        }
        this.wrappedTask = task;
    }

    // Делегируем геттеры и сеттеры к обернутой задаче

    public Long getId(){
        return wrappedTask.getId();
    }

    public String getTitle() {
        return wrappedTask.getTitle();
    }

    public String getDescription() {
        return wrappedTask.getDescription();
    }

    public TaskStatus getStatus() {
        return wrappedTask.getStatus();
    }

    public Priority getPriority() {
        return wrappedTask.getPriority();
    }

    public LocalDateTime getCreatedAt() {
        return wrappedTask.getCreatedAt();
    }

    public LocalDateTime getDueDate() {
        return wrappedTask.getDueDate();
    }

    public User getCreator() {
        return wrappedTask.getCreator();
    }

    public Project getProject() {
        return wrappedTask.getProject();
    }

    // Сеттеры

    public void setId(Long id) {
        wrappedTask.setId(id);
    }

    public void setTitle(String title) {
        wrappedTask.setTitle(title);
    }

    public void setDescription(String description) {
        wrappedTask.setDescription(description);
    }

    public void setStatus(TaskStatus status) {
        wrappedTask.setStatus(status);
    }

    public void setPriority(Priority priority) {
        wrappedTask.setPriority(priority);
    }

    public void setDueDate(LocalDateTime deadline) {
        wrappedTask.setDueDate(deadline);
    }

    public void setCreator(User creator) {
        wrappedTask.setCreator(creator);
    }

    public void setProjectId(Project project) {
        wrappedTask.setProject(project);
    }

    /**
     * Получаем сам Task из обертки
     */
    public Task getWrappedTask() {
        return wrappedTask;
    }

}
