package edu.taskmanager.frontend.console;


import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.ServisRepository;
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
        ServisRepository servisRepository = new ServisRepository();
        DataSaver dataSaver = new DataSaver(servisRepository);

        // Загрузчик данных в репозитории
        new DataLoader(servisRepository).load("dev_tasks_.json");

        // Создание команд
        Command create = new CreateCommand(servisRepository);
        Command list = new ListCommand(servisRepository);
        Command get = new GetCommand(servisRepository);
        Command delete = new DeleteCommand(servisRepository);
        Command filter = new FilterCommand(servisRepository);
        Command sorting = new SortingCommand(servisRepository);
        Command help = new HelpCommand(registry);
        Command generate = new GenerateCommand(servisRepository);
        Command exit = new ExitCommand(() -> running = false, dataSaver, "saved/dev_tasks_.json");


        // Регистрация (можем обернуть нужные команды в декораторы)
        registry.register("create", new LoggingCommandDecorator(new TimingCommandDecorator(create)));
        registry.register("list", list);
        registry.register("get", get);
        registry.register("delete", delete);
        registry.register("filter", new ResultSavingDecorator(filter, servisRepository));
        registry.register("sorting", new ResultSavingDecorator(sorting, servisRepository));
        registry.register("help", help);
        registry.register("generate", new ResultSavingDecorator(generate, servisRepository));
        registry.register("exit", exit);
    }

    private void printHelp() {
        Command help = registry.getCommand("help");
        if (help != null) {
            help.execute(java.util.List.of());
        }
    }
}
