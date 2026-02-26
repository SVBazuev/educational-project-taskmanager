package edu.taskmanager.repository;


import java.util.List;
import java.util.Optional;

import edu.taskmanager.model.Task;

/**
 * Репозиторий для хранения и извлечения задач.
 */
public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    void deleteById(Long id);
    List<Task> findSubtasksByParentId(Long parentId); // для прокси
}
