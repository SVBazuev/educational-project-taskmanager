package edu.taskmanager.console;


import edu.taskmanager.console.decorator.LoggingCommandDecorator;
import edu.taskmanager.console.decorator.TimingCommandDecorator;
import edu.taskmanager.console.handlers.*;
import edu.taskmanager.console.parser.CommandParser;
import edu.taskmanager.console.parser.ParsedCommand;
import edu.taskmanager.console.util.Printer;
import edu.taskmanager.repository.*;

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

        // Создание команд
        Command create = new CreateCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command list = new ListCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command get = new GetCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command update = new UpdateCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command delete = new DeleteCommand(taskRepo, projectRepo, tagRepo, userRepo);
        Command filter = new FilterCommand(taskRepo, tagRepo, projectRepo, userRepo);
        Command help = new HelpCommand(registry);
        Command exit = new ExitCommand(() -> running = false);

        // Регистрация (можем обернуть нужные команды в декораторы)
        registry.register("create", new LoggingCommandDecorator(new TimingCommandDecorator(create)));
        registry.register("list", list);
        registry.register("get", get);
        registry.register("update", update);
        registry.register("delete", delete);
        registry.register("filter", filter);
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
