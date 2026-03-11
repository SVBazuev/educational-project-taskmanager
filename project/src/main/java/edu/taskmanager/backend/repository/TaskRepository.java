package edu.taskmanager.backend.repository;

import java.util.List;
import java.util.Optional;

import edu.taskmanager.backend.model.Task;

/**
 * Интерфейс репозитория для хранения и извлечения задач.
 * Определяет базовые CRUD операции.
 */
public interface TaskRepository {

    /**
     * Сохраняет задачу в хранилище.
     * Если у задачи нет id (null), генерирует новый уникальный идентификатор.
     * Если id задан, обновляет существующую задачу.
     *
     * @param task задача для сохранения
     * @return сохранённая задача с установленным id (если создавалась новая)
     */
    Task save(Task task);

    /**
     * Находит задачу по идентификатору.
     *
     * @param id идентификатор задачи
     * @return Optional с задачей, если она найдена, иначе пустой Optional
     */
    Optional<Task> findById(Long id);

    /**
     * Возвращает список всех задач.
     *
     * @return список всех задач (может быть пустым)
     */
    List<Task> findAll();

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи
     */
    void deleteById(Long id);

    /**
     * Находит подзадачи для заданной родительской задачи.
     * Необходимо для реализации ленивой загрузки подзадач (паттерн Proxy).
     *
     * @param parentId идентификатор родительской задачи
     * @return список подзадач (может быть пустым)
     */
    List<Task> findSubtasksByParentId(Long parentId);
}
