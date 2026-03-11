package edu.taskmanager.backend.proxy;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.service.TaskService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Защитный прокси для TaskService.
 * Проверяет права доступа перед вызовом реального сервиса.
 */
public class TaskServiceProxy implements TaskService {
    private final TaskService realService;

    public TaskServiceProxy(TaskService realService) {
        this.realService = realService;
    }

    private boolean canRead(User user, Task task) {
        // Админ может всё
        if (user.getRole() == User.Role.ADMIN) return true;
        // Создатель задачи может читать
        if (task.getCreator() != null && task.getCreator().equals(user)) return true;
        // Исполнитель может читать
        if (task.getContractors() != null && task.getContractors().contains(user)) return true;
        return false;
    }

    private boolean canWrite(User user, Task task) {
        // Админ может всё
        if (user.getRole() == User.Role.ADMIN) return true;
        // Создатель может изменять
        if (task.getCreator() != null && task.getCreator().equals(user)) return true;
        return false;
    }

    private boolean canCreate(User user) {
        // Все могут создавать (кроме гостя)
        return user.getRole() != User.Role.GUEST;
    }

    private boolean canDelete(User user, Task task) {
        // Админ или создатель
        return user.getRole() == User.Role.ADMIN ||
                (task.getCreator() != null && task.getCreator().equals(user));
    }

    @Override
    public Task createTask(Task task, User user) {
        if (!canCreate(user)) {
            throw new SecurityException("Пользователь " + user.getUsername() + " не имеет прав на создание задачи");
        }
        return realService.createTask(task, user);
    }

    @Override
    public Optional<Task> getTask(Long id, User user) {
        Optional<Task> opt = realService.getTask(id, user);
        if (opt.isPresent()) {
            Task task = opt.get();
            if (canRead(user, task)) {
                return opt;
            } else {
                throw new SecurityException("Пользователь " + user.getUsername() + " не имеет прав на чтение задачи id=" + id);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Task> getAllTasks(User user) {
        List<Task> all = realService.getAllTasks(user);
        // Фильтруем только те, которые пользователь может читать
        return all.stream()
                .filter(task -> canRead(user, task))
                .collect(Collectors.toList());
    }

    @Override
    public Task updateTask(Task task, User user) {
        // Проверим существование задачи и права
        Optional<Task> existingOpt = realService.getTask(task.getId(), user);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Задача с id " + task.getId() + " не найдена");
        }
        Task existing = existingOpt.get();
        if (!canWrite(user, existing)) {
            throw new SecurityException("Пользователь " + user.getUsername() + " не имеет прав на изменение задачи id=" + task.getId());
        }
        return realService.updateTask(task, user);
    }

    @Override
    public void deleteTask(Long id, User user) {
        Optional<Task> existingOpt = realService.getTask(id, user);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Задача с id " + id + " не найдена");
        }
        Task existing = existingOpt.get();
        if (!canDelete(user, existing)) {
            throw new SecurityException("Пользователь " + user.getUsername() + " не имеет прав на удаление задачи id=" + id);
        }
        realService.deleteTask(id, user);
    }
}
