package edu.taskmanager.frontend.console;

import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.proxy.TaskServiceProxy;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.NotificationService;
import edu.taskmanager.backend.service.TaskService;
import edu.taskmanager.backend.service.TaskServiceImpl;
import edu.taskmanager.backend.strategy.notification.EmailNotificationStrategy;
import edu.taskmanager.frontend.console.decorator.LoggingCommandDecorator;
import edu.taskmanager.frontend.console.decorator.TimingCommandDecorator;
import edu.taskmanager.frontend.console.handlers.*;
import edu.taskmanager.frontend.console.parser.CommandParser;
import edu.taskmanager.frontend.console.parser.ParsedCommand;
import edu.taskmanager.frontend.console.util.Printer;

import java.util.Scanner;

public class ConsoleApplication {
    private final CommandRegistry registry = new CommandRegistry();
    private final CommandParser parser = new CommandParser();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    // Текущий пользователь (для демо возьмём админа)
    private final User currentUser;

    public ConsoleApplication() {
        // Создадим тестового пользователя (в реальности получали бы после аутентификации)
        UserRepository userRepo = new InMemoryUserRepository();
        User admin = new User("admin", "admin", User.Role.ADMIN);
        currentUser = userRepo.save(admin);
    }

    public void start() {
        initializeCommands();
        Printer.printWelcome();
        printHelp();

        while (running) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            ParsedCommand parsed = parser.parse(input);
            Command command = registry.getCommand(parsed.name());

            if (command == null) {
                Printer.printUnknownCommand(parsed.name());
                continue;
            }

            try {
                command.execute(parsed.args());
            } catch (Exception e) {
                Printer.printError("Ошибка выполнения команды: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void initializeCommands() {
        // Репозитории
        TaskRepository taskRepo = new InMemoryTaskRepository();
        ProjectRepository projectRepo = new InMemoryProjectRepository();
        TagRepository tagRepo = new InMemoryTagRepository();
        UserRepository userRepo = new InMemoryUserRepository();

        // Сервисы
        TaskService realTaskService = new TaskServiceImpl(taskRepo);
        TaskService taskService = new TaskServiceProxy(realTaskService); // оборачиваем прокси
        NotificationService notificationService = new NotificationService();
        notificationService.setNotificationStrategy(new EmailNotificationStrategy(currentUser.getUsername() + "@example.com"));

        // Загрузчик данных (может использовать репозитории напрямую)
        new DataLoader(taskRepo, projectRepo, tagRepo, userRepo).load("dev_tasks_.json");

        // Команды с внедрением зависимостей и текущим пользователем
        Command create = new CreateCommand(taskService, notificationService, projectRepo, tagRepo, userRepo, currentUser);
        Command list = new ListCommand(taskService, projectRepo, tagRepo, userRepo, currentUser);
        Command get = new GetCommand(taskService, projectRepo, tagRepo, userRepo, currentUser);
        Command update = new UpdateCommand(taskService, projectRepo, tagRepo, userRepo, currentUser);
        Command delete = new DeleteCommand(taskService, projectRepo, tagRepo, userRepo, currentUser);
        Command filter = new FilterCommand(taskService, tagRepo, projectRepo, userRepo, currentUser);
        Command sorting = new SortingCommand(taskService, currentUser);
        Command help = new HelpCommand(registry);
        Command exit = new ExitCommand(() -> running = false, new DataSaver(taskRepo, projectRepo, userRepo, tagRepo), "saved/dev_tasks_.json");

        // Регистрация с декораторами
        registry.register("create", new LoggingCommandDecorator(new TimingCommandDecorator(create)));
        registry.register("list", list);
        registry.register("get", get);
        registry.register("update", update);
        registry.register("delete", delete);
        registry.register("filter", filter);
        registry.register("sorting", sorting);
        registry.register("help", help);
        registry.register("exit", exit);
    }

    private void printHelp() {
        Command help = registry.getCommand("help");
        if (help != null) {
            help.execute(java.util.List.of());
        }
    }
}
