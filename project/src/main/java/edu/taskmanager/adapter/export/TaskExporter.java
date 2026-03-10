package edu.taskmanager.adapter.export;

import edu.taskmanager.model.Task;

import java.util.List;

public interface TaskExporter {
    /**
     * Экспортирует задачи в файл.
     * @param tasks список задач для экспорта
     */
    String export(List<Task> tasks);
}
