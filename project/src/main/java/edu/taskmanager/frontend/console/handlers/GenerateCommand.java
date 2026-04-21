package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.frontend.console.Command;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GenerateCommand implements Command {
    private final ServisRepository servisRepository;
    private List<Task> lastResult;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public GenerateCommand(ServisRepository servisRepository){
        this.servisRepository=servisRepository;
        this.lastResult = new ArrayList<>();
    }

    // Шаблоны для генерации контента
    private static final List<String> TITLES = Arrays.asList(
            "Реализовать авторизацию", "Исправить ошибку в экспорте", "Добавить поиск",
            "Оптимизировать запросы", "Написать тесты", "Обновить документацию",
            "Настроить CI/CD", "Рефакторинг старого кода", "Добавить уведомления",
            "Интеграция с API", "Резервное копирование", "Аудит безопасности",
            "Миграция данных", "Кэширование результатов", "Логирование ошибок",
            "Валидация форм", "Генерация отчетов", "Импорт из Excel",
            "Восстановление пароля", "Двухфакторная аутентификация"
    );

    private static final List<String> DESCRIPTIONS = Arrays.asList(
            "Необходимо реализовать полный цикл обработки",
            "Исправить критический баг в продакшене",
            "Добавить поддержку новых форматов",
            "Оптимизировать время отклика системы",
            "Покрыть код юнит-тестами",
            "Обновить API документацию",
            "Настроить автоматическую сборку",
            "Улучшить читаемость кода",
            "Добавить кнопку выгрузки данных",
            "Исправить проблемы с производительностью"
    );

    private static final List<String> USERNAMES = Arrays.asList(
            "alex", "maria", "john", "anna", "dmitry", "elena", "mike", "sarah"
    );

    private static final List<String> PROJECTS = Arrays.asList(
            "TaskFlow", "EcoSystem", "DataHub", "CloudSync", "SmartCRM"
    );

    private static final List<String> TAGS = Arrays.asList(
            "frontend", "backend", "database", "devops", "testing", "bug", "feature"
    );

    /**
     * Генерирует указанное количество задач
     */
    public static List<Task> generateTasks(int count) {
        List<Task> tasks = new ArrayList<>();

        // Создаем общие объекты
        List<User> users = createUsers();
        List<Project> projects = createProjects();
        List<Tag> tags = createTags();

        // Генерируем задачи
        for (int i = 0; i < count; i++) {
            Task task = new Task();
            task.setId((long) (i + 1));
            task.setTitle(getRandomItem(TITLES));
            task.setDescription(getRandomItem(DESCRIPTIONS));
            task.setDueDate(LocalDateTime.now().plusDays(random.nextInt(1, 31)));
            task.setCreator(getRandomItem(users));
            task.setPriority(getRandomPriority());
            task.setStatus(getRandomStatus());
            task.setProject(getRandomItem(projects));
            task.setTags(getRandomTags(tags));
            task.setSubtasks(new ArrayList<>());
            task.setContractors(getRandomContractors(users));
            task.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(1, 15)));
            task.setUpdatedAt(LocalDateTime.now());
            task.setParentId(null);

            tasks.add(task);
        }

        // Добавляем подзадачи (примерно 20% задач)
        addSubtasks(tasks);

        return tasks;
    }

    private static List<User> createUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < USERNAMES.size(); i++) {
            User user = new User();
            user.setId((long) (i + 1));
            user.setUsername(USERNAMES.get(i));
            user.setPasswordHash("hash" + i);
            user.setRole(i == 0 ? User.Role.ADMIN : User.Role.USER);
            users.add(user);
        }
        return users;
    }

    private static List<Project> createProjects() {
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < PROJECTS.size(); i++) {
            Project project = new Project();
            project.setId((long) (i + 1));
            project.setName(PROJECTS.get(i));
            project.setDescription("Проект " + PROJECTS.get(i));
            project.setTasks(new ArrayList<>());
            projects.add(project);
        }
        return projects;
    }

    private static List<Tag> createTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < TAGS.size(); i++) {
            Tag tag = new Tag();
            tag.setId((long) (i + 1));
            tag.setName(TAGS.get(i));
            tags.add(tag);
        }
        return tags;
    }

    private static Priority getRandomPriority() {
        Priority[] priorities = Priority.values();
        return priorities[random.nextInt(priorities.length)];
    }

    private static TaskStatus getRandomStatus() {
        TaskStatus[] statuses = TaskStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }

    private static List<Tag> getRandomTags(List<Tag> allTags) {
        int count = random.nextInt(0, 3);
        if (count == 0) return new ArrayList<>();

        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < count) {
            indexes.add(random.nextInt(allTags.size()));
        }

        return indexes.stream()
                .map(allTags::get)
                .collect(Collectors.toList());
    }

    private static List<User> getRandomContractors(List<User> allUsers) {
        int count = random.nextInt(0, 3);
        if (count == 0) return new ArrayList<>();

        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < count) {
            indexes.add(random.nextInt(allUsers.size()));
        }

        return indexes.stream()
                .map(allUsers::get)
                .collect(Collectors.toList());
    }

    private static void addSubtasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            // Каждая 5-я задача имеет подзадачи
            if (i % 5 == 0 && i + 1 < tasks.size()) {
                int subtaskCount = Math.min(random.nextInt(1, 3), tasks.size() - i - 1);
                List<Task> subtasks = new ArrayList<>();

                for (int j = 1; j <= subtaskCount; j++) {
                    Task subtask = tasks.get(i + j);
                    subtask.setParentId(tasks.get(i).getId());
                    subtasks.add(subtask);
                }

                tasks.get(i).setSubtasks(subtasks);
            }
        }
    }

    private static <T> T getRandomItem(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public List<Task> getLastResult() {
        return lastResult != null ? new ArrayList<>(lastResult) : new ArrayList<>();
    }

    @Override
    public void execute(List<String> args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Сколько задач сгенерировать? ");
        int count = scanner.nextInt();

        List<Task> tasks = GenerateCommand.generateTasks(count);

        lastResult = tasks;

        System.out.println("Создано задач: " + tasks.size());
    }

    @Override
    public String getDescription() {
        return "генерация задач (после слова generate нажать Enter)";
    }
}