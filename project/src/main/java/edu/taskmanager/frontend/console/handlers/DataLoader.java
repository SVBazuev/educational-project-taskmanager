package edu.taskmanager.frontend.console.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.TagRepository;
import edu.taskmanager.backend.repository.TaskRepository;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.frontend.console.util.AppData;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataLoader {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public DataLoader(
            TaskRepository taskRepository, ProjectRepository projectRepository,
            TagRepository tagRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public void load(String resourcePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.out.println("Ресурс не найден: " + resourcePath);
                return;
            }

            AppData data = mapper.readValue(is, AppData.class);

            if (data.getTasks() != null) {
                Map<Long, Project> projectMap = new LinkedHashMap<>();
                Map<Long, Tag> tagMap = new LinkedHashMap<>();
                Map<Long, User> userMap = new LinkedHashMap<>();

                for (Task task : data.getTasks()) {
                    collectFromTask(task, projectMap, tagMap, userMap);
                }

                projectMap.values().forEach(projectRepository::save);
                tagMap.values().forEach(tagRepository::save);
                userMap.values().forEach(userRepository::save);
                for (Task task : data.getTasks()) {
                    saveTaskRecursively(task);
                }

                // Проставляем задачи в проекты (в JSON tasks у project всегда пустой)
                for (Task task : taskRepository.findAll()) {
                    if (task.getProject() != null && task.getProject().getId() != null) {
                        projectRepository.findById(task.getProject().getId()).ifPresent(project -> {
                            if (!project.getTasks().contains(task)) {
                                project.getTasks().add(task);
                            }
                        });
                    }
                }

            }
            System.out.println("Данные успешно загружены из " + resourcePath);
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }

    }

    private void saveTaskRecursively(Task task) {
        taskRepository.save(task);
        if (task.getSubtasks() != null) {
            for (Task subtask : task.getSubtasks()) {
                if (subtask.getParentId() == null && task.getId() != null) {
                    subtask.setParentId(task.getId());
                }
                saveTaskRecursively(subtask);
            }
        }
    }

    private void collectFromTask(Task task,
                                 Map<Long, Project> projects,
                                 Map<Long, Tag> tags,
                                 Map<Long, User> users) {

        if (task.getProject() != null && task.getProject().getId() != null) {
            projects.putIfAbsent(task.getProject().getId(), task.getProject());
        }
        if (task.getCreator() != null && task.getCreator().getId() != null) {
            users.putIfAbsent(task.getCreator().getId(), task.getCreator());
        }
        if (task.getTags() != null) {
            task.getTags().forEach(tag -> {
                if (tag.getId() != null) tags.putIfAbsent(tag.getId(), tag);
            });
        }
        if (task.getContractors() != null) {
            task.getContractors().forEach(user -> {
                if (user.getId() != null) users.putIfAbsent(user.getId(), user);
            });
        }
        if (task.getSubtasks() != null) {
            task.getSubtasks().forEach(sub ->
                    collectFromTask(sub, projects, tags, users));
        }
    }
}
