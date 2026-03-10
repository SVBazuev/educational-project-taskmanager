package edu.taskmanager.frontend.console.handlers;


import java.util.List;

import edu.taskmanager.frontend.console.Command;
import edu.taskmanager.frontend.console.CommandRegistry;


public class HelpCommand implements Command {
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void execute(List<String> args) {
        System.out.println("\nДоступные команды:");
        for (String name : registry.getCommandNames().stream().sorted().toList()) {
            Command cmd = registry.getCommand(name);
            System.out.printf("  %-10s - %s%n", name, cmd.getDescription());
        }
        System.out.println("Для получения справки по конкретной команде используйте: help <команда> (не реализовано)");
    }

    @Override
    public String getDescription() {
        return "help - показать эту справку";
    }
}
