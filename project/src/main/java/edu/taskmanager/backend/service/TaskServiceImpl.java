package edu.taskmanager.backend.service;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса задач, работающая напрямую с репозиторием.
 * Здесь может быть дополнительная бизнес-логика (валидация, вычисления).
 */
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task, User user) {
        // Можно добавить валидацию
        if (task.getCreator() == null) {
            task.setCreator(user); // по умолчанию создатель — текущий пользователь
        }
        task.setCreatedAt(LocalDateTime.now());
        // Обновлённая дата пока null
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getTask(Long id, User user) {
        // Здесь можно было бы фильтровать по правам, но пока просто возвращаем
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getAllTasks(User user) {
        // Аналогично, можно фильтровать по правам
        return taskRepository.findAll();
    }

    @Override
    public Task updateTask(Task task, User user) {
        // Проверим, что задача существует
        Optional<Task> existingOpt = taskRepository.findById(task.getId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Задача с id " + task.getId() + " не найдена");
        }
        Task existing = existingOpt.get();
        // Обновляем только переданные поля? В данном примере просто сохраняем переданный объект
        // Но нужно сохранить дату создания
        task.setCreatedAt(existing.getCreatedAt());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id, User user) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Задача с id " + id + " не найдена");
        }
        taskRepository.deleteById(id);
    }
}
