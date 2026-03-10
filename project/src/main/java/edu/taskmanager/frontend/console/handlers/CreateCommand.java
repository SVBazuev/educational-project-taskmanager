package edu.taskmanager.frontend.console.handlers;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.taskmanager.backend.builder.TaskBuilder;
import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.TagRepository;
import edu.taskmanager.backend.repository.TaskRepository;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.frontend.console.Command;
import edu.taskmanager.frontend.console.parser.ArgumentParser;

public class CreateCommand implements Command {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public CreateCommand(
            TaskRepository taskRepository, ProjectRepository projectRepository,
            TagRepository tagRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Не указан тип создаваемого объекта. Используйте: create <task|project|tag|user> ...");
            return;
        }

        String type = args.get(0).toLowerCase();
        List<String> params = args.subList(1, args.size());

        // Парсим аргументы в карту критериев
        Map<String, String> criteria = new HashMap<>();
        for (String arg : params) {
            String[] kv = arg.split("=", 2);
            if (kv.length == 2) {
                criteria.put(kv[0].toLowerCase(), kv[1]);
            } else {
                System.out.println("Пропущен некорректный аргумент: " + arg);
            }
        }

        switch (type) {
            case "task" -> createTask(criteria);
            case "project" -> createProject(criteria);
            case "tag" -> createTag(criteria);
            case "user" -> createUser(criteria);
            default -> System.out.println("Неизвестный тип: " + type);
        }
    }

    private void createTask(Map<String, String> criteria) {
        if (criteria.isEmpty() || !criteria.containsKey("title")) {
            System.out.println("Использование: create task title=\"Название задачи\" [dueDate=\"2024-12-31 23:59\"] [priority=\"MEDIUM\"]");
            return;
        }

        String title = criteria.get("title");
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        Priority priority = Priority.MEDIUM;

        if (criteria.containsKey("duedate")) {
            try {
                dueDate = LocalDateTime.parse(
                    criteria.get("duedate"),
                    ArgumentParser.DATE_FORMATTER
                );
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Используйте YYYY-MM-DD HH:MM. Будет установлена дата по умолчанию.");
            }
        }

        if (criteria.containsKey("priority")) {
            try {
                priority = Priority.valueOf(criteria.get("priority").toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный приоритет. Допустимые: LOW, MEDIUM, HIGH, CRITICAL. Установлен MEDIUM.");
            }
        }

        Task task = new TaskBuilder()
                .setTitle(title)
                .setDueDate(dueDate)
                .setPriority(priority)
                .setStatus(TaskStatus.TODO)
                .build();

        Task saved = taskRepository.save(task);
        System.out.println("Создана задача: " + saved);
    }

    private void createProject(Map<String, String> criteria) {
        if (criteria.isEmpty() || !criteria.containsKey("name")) {
            System.out.println("Использование: create project name=\"Название проекта\" [description=\"Описание\"]");
            return;
        }

        String name = criteria.get("name");
        String description = criteria.getOrDefault("description", "");

        Project project = new Project.ProjectBuilder()
                .setName(name)
                .setDescription(description)
                .build();

        Project saved = projectRepository.save(project);
        System.out.println("Создан проект: " + saved);
    }

    private void createTag(Map<String, String> criteria) {
        if (criteria.isEmpty() || !criteria.containsKey("name")) {
            System.out.println("Использование: create tag name=\"Название тега\"");
            return;
        }

        String name = criteria.get("name");
        Tag tag = new Tag(name);
        Tag saved = tagRepository.save(tag);
        System.out.println("Создан тег: " + saved);
    }

    private void createUser(Map<String, String> criteria) {
        if (criteria.size() < 3 || !criteria.containsKey("username")
                || !criteria.containsKey("password") || !criteria.containsKey("role")) {
            System.out.println("Использование: create user username=\"Имя\" password=\"Пароль\" role=\"ADMIN|USER|GUEST\"");
            return;
        }

        String username = criteria.get("username");
        String password = criteria.get("password");
        String roleStr = criteria.get("role").toUpperCase();

        User.Role role;
        try {
            role = User.Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверная роль. Допустимые: ADMIN, USER, GUEST.");
            return;
        }

        User user = new User(username, password, role);
        User saved = userRepository.save(user);
        System.out.println("Создан пользователь: " + saved);
    }

    @Override
    public String getDescription() {
        return "create <тип> ... - создание объекта (task, project, tag, user)";
    }
}
