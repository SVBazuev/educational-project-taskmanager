package edu.taskmanager.frontend.console.parser;


import java.util.List;
import java.util.ArrayList;


public class CommandParser {
    public ParsedCommand parse(String input) {
        String[] parts = input.split("&");
        if (parts.length == 0) {
            return new ParsedCommand("", List.of());
        }
        String commandName = parts[0].toLowerCase();
        List<String> args = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            args.add(parts[i].trim());
        }
        ParsedCommand command = new ParsedCommand(commandName, args);

        System.out.println(command.args());
        return new ParsedCommand(commandName, args);
    }
}
