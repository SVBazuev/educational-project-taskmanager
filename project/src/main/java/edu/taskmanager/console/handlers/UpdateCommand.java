package edu.taskmanager.console.handlers;


import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import edu.taskmanager.model.*;
import edu.taskmanager.repository.*;
import edu.taskmanager.util.Priority;
import edu.taskmanager.util.TaskStatus;
import edu.taskmanager.console.Command;


public class UpdateCommand implements Command {
    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public UpdateCommand(
            TaskRepository taskRepository, ProjectRepository projectRepository,
            TagRepository tagRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(List<String> args) {
        if (args.size() < 3) {
            System.out.println("Использование: update <task|project|tag|user> <id> <поле> <значение>");
            return;
        }
        String type = args.get(0).toLowerCase();
        long id;
        try {
            id = Long.parseLong(args.get(1));
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом.");
            return;
        }
        String field = args.get(2).toLowerCase();
        // Значение может содержать пробелы, поэтому объединяем остальные аргументы
        String value = args.size() > 3 ? String.join(" ", args.subList(3, args.size())) : "";

        switch (type) {
            case "task" -> updateTask(id, field, value);
            case "project" -> updateProject(id, field, value);
            case "tag" -> updateTag(id, field, value);
            case "user" -> updateUser(id, field, value);
            default -> System.out.println("Неизвестный тип: " + type);
        }
    }

    private void updateTask(long id, String field, String value) {
        Optional<Task> opt = taskRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Задача с ID " + id + " не найдена.");
            return;
        }
        Task task = opt.get();
        switch (field) {
            case "title" -> task.setTitle(value);
            case "description" -> task.setDescription(value);
            case "duedate" -> {
                try {
                    LocalDateTime newDate = LocalDateTime.parse(value, DATE_FORMATTER);
                    task.setDueDate(newDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Неверный формат даты. Используйте YYYY-MM-DD HH:MM.");
                    return;
                }
            }
            case "priority" -> {
                try {
                    Priority newPriority = Priority.valueOf(value.toUpperCase());
                    task.setPriority(newPriority);
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный приоритет. Допустимые: LOW, MEDIUM, HIGH, CRITICAL.");
                    return;
                }
            }
            case "status" -> {
                try {
                    TaskStatus newStatus = TaskStatus.valueOf(value.toUpperCase());
                    task.setStatus(newStatus);
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный статус. Допустимые: TODO, IN_PROGRESS, DONE.");
                    return;
                }
            }
            default -> {
                System.out.println("Неизвестное поле. Доступные: title, description, dueDate, priority, status.");
                return;
            }
        }
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        System.out.println("Задача обновлена: " + task);
    }

    private void updateProject(long id, String field, String value) {
        Optional<Project> opt = projectRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Проект с ID " + id + " не найден.");
            return;
        }
        Project project = opt.get();
        switch (field) {
            case "name" -> project.setName(value);
            case "description" -> project.setDescription(value);
            default -> {
                System.out.println("Неизвестное поле. Доступные: name, description.");
                return;
            }
        }
        projectRepository.save(project);
        System.out.println("Проект обновлён: " + project);
    }

    private void updateTag(long id, String field, String value) {
        if (!field.equals("name")) {
            System.out.println("У тега можно изменить только поле name.");
            return;
        }
        Optional<Tag> opt = tagRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Тег с ID " + id + " не найден.");
            return;
        }
        Tag tag = opt.get();
        tag.setName(value);
        tagRepository.save(tag);
        System.out.println("Тег обновлён: " + tag);
    }

    private void updateUser(long id, String field, String value) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Пользователь с ID " + id + " не найден.");
            return;
        }
        User user = opt.get();
        switch (field) {
            case "username" -> user.setUsername(value);
            case "password" -> user.setPasswordHash(value);
            case "role" -> {
                try {
                    User.Role newRole = User.Role.valueOf(value.toUpperCase());
                    user.setRole(newRole);
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверная роль. Допустимые: ADMIN, USER, GUEST.");
                    return;
                }
            }
            default -> {
                System.out.println("Неизвестное поле. Доступные: username, password, role.");
                return;
            }
        }
        userRepository.save(user);
        System.out.println("Пользователь обновлён: " + user);
    }

    @Override
    public String getDescription() {
        return "update <тип> <id> <поле> <значение> - обновить поле объекта";
    }
}
