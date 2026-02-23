package edu.taskmanager.repository;


import java.util.List;
import java.util.Optional;

import edu.taskmanager.model.TaskETD;


/**
 * Репозиторий для хранения и извлечения задач.
 */
public interface TaskRepositorySVB {
    TaskETD save(TaskETD task);
    Optional<TaskETD> findById(Long id);
    List<TaskETD> findAll();
    void deleteById(Long id);
    List<TaskETD> findSubtasksByParentId(Long parentId);
}
