package edu.taskmanager.console.handlers;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


import edu.taskmanager.model.*;
import edu.taskmanager.repository.*;
import edu.taskmanager.util.Priority;
import edu.taskmanager.console.Command;
import edu.taskmanager.util.TaskStatus;
import edu.taskmanager.builder.TaskBuilder;


public class CreateCommand implements Command {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        switch (type) {
            case "task" -> createTask(params);
            case "project" -> createProject(params);
            case "tag" -> createTag(params);
            case "user" -> createUser(params);
            default -> System.out.println("Неизвестный тип: " + type);
        }
    }

    private void createTask(List<String> params) {
        // Формат: create task <title> [dueDate] [priority]
        if (params.isEmpty()) {
            System.out.println("Использование: create task <title> [dueDate(YYYY-MM-DD HH:MM)] [priority]");
            return;
        }
        String title = params.get(0);
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        Priority priority = Priority.MEDIUM;

        if (params.size() >= 2) {
            try {
                dueDate = LocalDateTime.parse(params.get(1), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Используйте YYYY-MM-DD HH:MM. Будет установлена дата по умолчанию.");
            }
        }
        if (params.size() >= 3) {
            try {
                priority = Priority.valueOf(params.get(2).toUpperCase());
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

    private void createProject(List<String> params) {
        // Формат: create project <name> [description]
        if (params.isEmpty()) {
            System.out.println("Использование: create project <name> [description]");
            return;
        }
        String name = params.get(0);
        String description = params.size() >= 2 ? params.get(1) : "";

        Project project = new Project.ProjectBuilder()
                .setName(name)
                .setDescription(description)
                .build();

        Project saved = projectRepository.save(project);
        System.out.println("Создан проект: " + saved);
    }

    private void createTag(List<String> params) {
        // Формат: create tag <name>
        if (params.isEmpty()) {
            System.out.println("Использование: create tag <name>");
            return;
        }
        String name = params.get(0);
        Tag tag = new Tag(name);
        Tag saved = tagRepository.save(tag);
        System.out.println("Создан тег: " + saved);
    }

    private void createUser(List<String> params) {
        // Формат: create user <username> <password> <role>
        if (params.size() < 3) {
            System.out.println("Использование: create user <username> <password> <role(ADMIN|USER|GUEST)>");
            return;
        }
        String username = params.get(0);
        String password = params.get(1);
        String roleStr = params.get(2).toUpperCase();
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
