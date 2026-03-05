package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import java.util.List;

/**
 * Интерфейс для фильтрации задач.
 */
public interface TaskFilter {
    /**
     * Применяет фильтр к списку задач.
     * @param tasks Список задач для фильтрации.
     * @return Список задач, прошедших фильтр.
     */
    List<Task> apply(List<Task> tasks);
}
