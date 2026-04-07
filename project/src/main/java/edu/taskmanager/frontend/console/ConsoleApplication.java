package edu.taskmanager.frontend.console;


import edu.taskmanager.backend.repository.*;
import edu.taskmanager.frontend.console.decorator.MultithreadingCommandDecorator;
import edu.taskmanager.frontend.console.handlers.GenerateCommand;
import edu.taskmanager.frontend.console.decorator.LoggingCommandDecorator;
import edu.taskmanager.frontend.console.decorator.ResultSavingDecorator;
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
                e.printStackTrace(); // для отладки, можно убрать
            }
        }
    }

    private void initializeCommands() {
        // Инициализация репозиториев (в реальном проекте можно использовать dependency injection)
        TaskRepository taskRepo = new InMemoryTaskRepository();
        ProjectRepository projectRepo = new InMemoryProjectRepository();
        TagRepository tagRepo = new InMemoryTagRepository();
        UserRepository userRepo = new InMemoryUserRepository();
        DataSaver dataSaver = new DataSaver(taskRepo, projectRepo, userRepo, tagRepo);

        // Загрузчик данных в репозитории
        new DataLoader(taskRepo, projectRepo, tagRepo, userRepo).load("dev_tasks_.json");

        // Создание команд
        Command create = new CreateCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command list = new ListCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command get = new GetCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command delete = new DeleteCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command filter = new FilterCommand(taskRepo);
        Command sorting = new SortingCommand(taskRepo);
        Command help = new HelpCommand(registry);
        Command generate = new GenerateCommand(taskRepo);
        Command exit = new ExitCommand(() -> running = false, dataSaver, "saved/dev_tasks_.json");


        // Регистрация (можем обернуть нужные команды в декораторы)
        registry.register("create", new LoggingCommandDecorator(new TimingCommandDecorator(create)));
        registry.register("list", list);
        registry.register("get", get);
        registry.register("delete", delete);
        registry.register("filter",
                new MultithreadingCommandDecorator(
                        new ResultSavingDecorator(filter, taskRepo)
                )
        );
        registry.register("sorting",
                new MultithreadingCommandDecorator(
                        new ResultSavingDecorator(sorting, taskRepo)
                )
        );
        registry.register("help", help);
        registry.register("generate",
                new MultithreadingCommandDecorator(
                        new ResultSavingDecorator(generate, taskRepo)
                )
        );
        registry.register("exit", exit);
    }

    private void printHelp() {
        Command help = registry.getCommand("help");
        if (help != null) {
            help.execute(java.util.List.of());
        }
    }
}
