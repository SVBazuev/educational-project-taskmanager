package edu.taskmanager.backend.adapter.export;

import java.util.List;

import edu.taskmanager.backend.model.Task;

public interface TaskExporter {
    /**
     * Экспортирует задачи в файл.
     * @param tasks список задач для экспорта
     */
    String export(List<Task> tasks);
}
