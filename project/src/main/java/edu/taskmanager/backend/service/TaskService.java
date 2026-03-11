package edu.taskmanager.backend.service;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с задачами.
 */
public interface TaskService {
    /**
     * Создаёт новую задачу.
     * @param task задача для создания (без id)
     * @param user пользователь, выполняющий операцию
     * @return созданная задача с установленным id
     */
    Task createTask(Task task, User user);

    /**
     * Возвращает задачу по идентификатору.
     * @param id идентификатор задачи
     * @param user пользователь
     * @return Optional с задачей, если она существует и доступна пользователю
     */
    Optional<Task> getTask(Long id, User user);

    /**
     * Возвращает все задачи, доступные пользователю.
     * @param user пользователь
     * @return список задач
     */
    List<Task> getAllTasks(User user);

    /**
     * Обновляет существующую задачу.
     * @param task задача с обновлёнными полями (должен быть установлен id)
     * @param user пользователь
     * @return обновлённая задача
     * @throws IllegalArgumentException если задача не найдена или нет прав
     */
    Task updateTask(Task task, User user);

    /**
     * Удаляет задачу по идентификатору.
     * @param id идентификатор задачи
     * @param user пользователь
     * @throws IllegalArgumentException если задача не найдена или нет прав
     */
    void deleteTask(Long id, User user);
}
