package edu.taskmanager.console.handlers;

import edu.taskmanager.adapter.export.JsonTaskExporter;
import edu.taskmanager.console.util.AppData;
import edu.taskmanager.model.Task;
import edu.taskmanager.repository.ProjectRepository;
import edu.taskmanager.repository.TagRepository;
import edu.taskmanager.repository.TaskRepository;
import edu.taskmanager.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DataSaver {
    private final TaskRepository taskRepository;

    public DataSaver(TaskRepository taskRepository, ProjectRepository projectRepository,
                     UserRepository userRepository, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(String resourcePath) {
        String outputPath = resolveOutputPath(resourcePath);

        // Только корневые задачи — подзадачи уже вложены внутри через поле subtasks
        List<Task> rootTasks = taskRepository.findAll().stream()
                .filter(task -> task.getParentId() == null)
                .collect(Collectors.toList());

        AppData data = new AppData();
        data.setTasks(rootTasks);

        JsonTaskExporter exporter = new JsonTaskExporter(outputPath);
        exporter.exportAppData(data);
    }

    private String resolveOutputPath(String resourcePath) {
        var url = getClass().getClassLoader().getResource(resourcePath);
        if (url != null) {
            return url.getFile();
        }
        return resourcePath;
    }
}
