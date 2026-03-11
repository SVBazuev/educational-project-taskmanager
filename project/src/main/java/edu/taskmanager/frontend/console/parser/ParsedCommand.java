package edu.taskmanager.frontend.console.parser;


import java.util.List;


public record ParsedCommand(String name, List<String> args) {}
