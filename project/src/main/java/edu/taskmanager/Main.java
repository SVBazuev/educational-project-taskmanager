package edu.taskmanager;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


import edu.taskmanager.builder.TaskBuilder;
import edu.taskmanager.model.*;
import edu.taskmanager.repository.*;
import edu.taskmanager.util.Priority;
import edu.taskmanager.util.TaskStatus;


/**
 * Консольное приложение для управления задачами, проектами, тегами и пользователями.
 * Поддерживает базовые CRUD-операции.
 * Команды вводятся пользователем в интерактивном режиме, разделитель аргументов - символ '&'.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in, "CP866");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Репозитории
    private static final ProjectRepository projectRepository = new InMemoryProjectRepository();
    private static final UserRepository userRepository = new InMemoryUserRepository();
    private static final TagRepository tagRepository = new InMemoryTagRepository();
    private static final TaskRepository taskRepository = new InMemoryTaskRepository();

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Task Manager!");
        printHelp();

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("&");
            if (parts.length == 0) continue;

            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "exit":
                        System.out.println("Выход из программы.");
                        return;
                    case "help":
                        printHelp();
                        break;
                    case "create":
                        if (parts.length < 2) {
                            System.out.println("Не указан тип создаваемого объекта. Используйте: create&<task|project|tag|user>&...");
                            break;
                        }
                        handleCreate(parts);
                        break;
                    case "list":
                        if (parts.length < 2) {
                            System.out.println("Не указан тип для списка. Используйте: list&<tasks|projects|tags|users>");
                            break;
                        }
                        handleList(parts);
                        break;
                    case "get":
                        if (parts.length < 3) {
                            System.out.println("Использование: get&<task|project|tag|user>&<id>");
                            break;
                        }
                        handleGet(parts);
                        break;
                    case "update":
                        if (parts.length < 4) {
                            System.out.println("Использование: update&<task|project|tag|user>&<id>&<поле>&<значение>");
                            break;
                        }
                        handleUpdate(parts);
                        break;
                    case "delete":
                        if (parts.length < 3) {
                            System.out.println("Использование: delete&<task|project|tag|user>&<id>");
                            break;
                        }
                        handleDelete(parts);
                        break;
                    default:
                        System.out.println("Неизвестная команда. Введите 'help' для справки.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("\nДоступные команды (аргументы разделяются символом '&'):");
        System.out.println("  create&task&<title>&[dueDate(YYYY-MM-DD HH:MM)]&[priority(LOW|MEDIUM|HIGH|CRITICAL)]");
        System.out.println("  create&project&<name>&[description]");
        System.out.println("  create&tag&<name>");
        System.out.println("  create&user&<username>&<password>&<role(ADMIN|USER|GUEST)>");
        System.out.println("  list&tasks");
        System.out.println("  list&projects");
        System.out.println("  list&tags");
        System.out.println("  list&users");
        System.out.println("  get&task&<id>");
        System.out.println("  get&project&<id>");
        System.out.println("  get&tag&<id>");
        System.out.println("  get&user&<id>");
        System.out.println("  update&task&<id>&<field>&<newValue> (fields: title, description, dueDate, priority, status)");
        System.out.println("  update&project&<id>&<field>&<newValue> (fields: name, description)");
        System.out.println("  update&tag&<id>&name&<newName>");
        System.out.println("  update&user&<id>&<field>&<newValue> (fields: username, password, role)");
        System.out.println("  delete&task&<id>");
        System.out.println("  delete&project&<id>");
        System.out.println("  delete&tag&<id>");
        System.out.println("  delete&user&<id>");
        System.out.println("  help - показать эту справку");
        System.out.println("  exit - выход");
    }

    // --- Обработчики команд ---

    private static void handleCreate(String[] parts) {
        String type = parts[1].toLowerCase();
        switch (type) {
            case "task":
                createTask(parts);
                break;
            case "project":
                createProject(parts);
                break;
            case "tag":
                createTag(parts);
                break;
            case "user":
                createUser(parts);
                break;
            default:
                System.out.println("Неизвестный тип: " + type);
        }
    }

    private static void handleList(String[] parts) {
        String type = parts[1].toLowerCase();
        switch (type) {
            case "tasks":
                listTasks();
                break;
            case "projects":
                listProjects();
                break;
            case "tags":
                listTags();
                break;
            case "users":
                listUsers();
                break;
            default:
                System.out.println("Неизвестный тип: " + type);
        }
    }

    private static void handleGet(String[] parts) {
        String type = parts[1].toLowerCase();
        long id;
        try {
            id = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом.");
            return;
        }
        switch (type) {
            case "task":
                getTask(id);
                break;
            case "project":
                getProject(id);
                break;
            case "tag":
                getTag(id);
                break;
            case "user":
                getUser(id);
                break;
            default:
                System.out.println("Неизвестный тип: " + type);
        }
    }

    private static void handleUpdate(String[] parts) {
        String type = parts[1].toLowerCase();
        long id;
        try {
            id = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом.");
            return;
        }
        String field = parts[3].toLowerCase();
        String value;
        if (parts.length > 4) {
            value = parts[4];
            if (parts.length > 5) {
                System.out.println("Внимание: значение содержит символ '&', возможно, оно было разбито. Используется только первая часть: " + value);
            }
        } else {
            System.out.println("Не указано новое значение.");
            return;
        }

        switch (type) {
            case "task":
                updateTask(id, field, value);
                break;
            case "project":
                updateProject(id, field, value);
                break;
            case "tag":
                updateTag(id, field, value);
                break;
            case "user":
                updateUser(id, field, value);
                break;
            default:
                System.out.println("Неизвестный тип: " + type);
        }
    }

    private static void handleDelete(String[] parts) {
        String type = parts[1].toLowerCase();
        long id;
        try {
            id = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом.");
            return;
        }
        switch (type) {
            case "task":
                deleteTask(id);
                break;
            case "project":
                deleteProject(id);
                break;
            case "tag":
                deleteTag(id);
                break;
            case "user":
                deleteUser(id);
                break;
            default:
                System.out.println("Неизвестный тип: " + type);
        }
    }

    // --- CRUD для Task ---

    private static void createTask(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Использование: create&task&<title>&[dueDate]&[priority]");
            return;
        }
        String title = parts[2];
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        Priority priority = Priority.MEDIUM;

        if (parts.length >= 4) {
            try {
                dueDate = LocalDateTime.parse(parts[3], DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Используйте YYYY-MM-DD HH:MM. Будет установлена дата по умолчанию.");
            }
        }
        if (parts.length >= 5) {
            try {
                priority = Priority.valueOf(parts[4].toUpperCase());
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

        Task savedTask = taskRepository.save(task);
        System.out.println("Создана задача: " + savedTask);
    }

    private static void listTasks() {
        List<Task> allTasks = taskRepository.findAll();
        if (allTasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        System.out.println("Список задач:");
        allTasks.forEach(System.out::println);
    }

    private static void getTask(long id) {
        Optional<Task> opt = taskRepository.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    private static void updateTask(long id, String field, String value) {
        Optional<Task> opt = taskRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Задача с ID " + id + " не найдена.");
            return;
        }
        Task task = opt.get();
        switch (field) {
            case "title":
                task.setTitle(value);
                break;
            case "description":
                task.setDescription(value);
                break;
            case "duedate":
                try {
                    LocalDateTime newDate = LocalDateTime.parse(value, DATE_FORMATTER);
                    task.setDueDate(newDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Неверный формат даты. Используйте YYYY-MM-DD HH:MM.");
                    return;
                }
                break;
            case "priority":
                try {
                    Priority newPriority = Priority.valueOf(value.toUpperCase());
                    task.setPriority(newPriority);
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный приоритет. Допустимые: LOW, MEDIUM, HIGH, CRITICAL.");
                    return;
                }
                break;
            case "status":
                try {
                    TaskStatus newStatus = TaskStatus.valueOf(value.toUpperCase());
                    task.setStatus(newStatus);
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный статус. Допустимые: TODO, IN_PROGRESS, DONE.");
                    return;
                }
                break;
            default:
                System.out.println("Неизвестное поле. Доступные: title, description, dueDate, priority, status.");
                return;
        }
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        System.out.println("Задача обновлена: " + task);
    }

    private static void deleteTask(long id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            System.out.println("Задача с ID " + id + " удалена.");
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    // --- CRUD для Project ---

    private static void createProject(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Использование: create&project&<name>&[description]");
            return;
        }
        String name = parts[2];
        String description = (parts.length >= 4) ? parts[3] : "";

        // Используем встроенный билдер
        Project project = new Project.ProjectBuilder()
                .setName(name)
                .setDescription(description)
                .build();

        Project savedProject = projectRepository.save(project);
        System.out.println("Создан проект: " + savedProject);
    }

    private static void listProjects() {
        List<Project> allProjects = projectRepository.findAll();
        if (allProjects.isEmpty()) {
            System.out.println("Список проектов пуст.");
            return;
        }
        System.out.println("Список проектов:");
        allProjects.forEach(System.out::println);
    }

    private static void getProject(long id) {
        Optional<Project> opt = projectRepository.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Проект с ID " + id + " не найден.");
        }
    }

    private static void updateProject(long id, String field, String value) {
        Optional<Project> opt = projectRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Проект с ID " + id + " не найден.");
            return;
        }
        Project project = opt.get();
        switch (field) {
            case "name":
                project.setName(value);
                break;
            case "description":
                project.setDescription(value);
                break;
            default:
                System.out.println("Неизвестное поле. Доступные: name, description.");
                return;
        }
        projectRepository.save(project);
        System.out.println("Проект обновлён: " + project);
    }

    private static void deleteProject(long id) {
        if (projectRepository.findById(id).isPresent()) {
            projectRepository.deleteById(id);
            System.out.println("Проект с ID " + id + " удалён.");
        } else {
            System.out.println("Проект с ID " + id + " не найден.");
        }
    }

    // --- CRUD для Tag ---

    private static void createTag(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Использование: create&tag&<name>");
            return;
        }
        String name = parts[2];
        Tag tag = new Tag(name);
        Tag savedTag = tagRepository.save(tag);
        System.out.println("Создан тег: " + savedTag);
    }

    private static void listTags() {
        List<Tag> allTags = tagRepository.findAll();
        if (allTags.isEmpty()) {
            System.out.println("Список тегов пуст.");
            return;
        }
        System.out.println("Список тегов:");
        allTags.forEach(System.out::println);
    }

    private static void getTag(long id) {
        Optional<Tag> opt = tagRepository.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Тег с ID " + id + " не найден.");
        }
    }

    private static void updateTag(long id, String field, String value) {
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

    private static void deleteTag(long id) {
        if (tagRepository.findById(id).isPresent()) {
            tagRepository.deleteById(id);
            System.out.println("Тег с ID " + id + " удалён.");
        } else {
            System.out.println("Тег с ID " + id + " не найден.");
        }
    }

    // --- CRUD для User ---

    private static void createUser(String[] parts) {
        if (parts.length < 5) {
            System.out.println("Использование: create&user&<username>&<password>&<role(ADMIN|USER|GUEST)>");
            return;
        }
        String username = parts[2];
        String password = parts[3];
        String roleStr = parts[4].toUpperCase();
        User.Role role;
        try {
            role = User.Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверная роль. Допустимые: ADMIN, USER, GUEST.");
            return;
        }
        User user = new User(username, password, role);
        User savedUser = userRepository.save(user);
        System.out.println("Создан пользователь: " + savedUser);
    }

    private static void listUsers() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            System.out.println("Список пользователей пуст.");
            return;
        }
        System.out.println("Список пользователей:");
        allUsers.forEach(System.out::println);
    }

    private static void getUser(long id) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
    }

    private static void updateUser(long id, String field, String value) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Пользователь с ID " + id + " не найден.");
            return;
        }
        User user = opt.get();
        switch (field) {
            case "username":
                user.setUsername(value);
                break;
            case "password":
                user.setPasswordHash(value);
                break;
            case "role":
                try {
                    User.Role newRole = User.Role.valueOf(value.toUpperCase());
                    user.setRole(newRole);
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверная роль. Допустимые: ADMIN, USER, GUEST.");
                    return;
                }
                break;
            default:
                System.out.println("Неизвестное поле. Доступные: username, password, role.");
                return;
        }
        userRepository.save(user);
        System.out.println("Пользователь обновлён: " + user);
    }

    private static void deleteUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            System.out.println("Пользователь с ID " + id + " удалён.");
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
    }
}
