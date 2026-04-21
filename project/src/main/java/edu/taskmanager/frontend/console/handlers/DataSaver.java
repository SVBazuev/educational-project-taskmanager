package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.adapter.export.JsonTaskExporter;
import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.frontend.console.util.AppData;

import java.util.List;
import java.util.stream.Collectors;

public class DataSaver {
    private final ServisRepository servisRepository;

    public DataSaver(ServisRepository servisRepository) {
        this.servisRepository = servisRepository;
    }

    public void save(String resourcePath) {
        String outputPath = resolveOutputPath(resourcePath);

        // Только корневые задачи — подзадачи уже вложены внутри через поле subtasks
        List<Task> rootTasks = servisRepository.findAllTasks().stream()
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
