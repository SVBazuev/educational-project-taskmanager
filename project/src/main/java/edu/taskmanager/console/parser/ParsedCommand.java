package edu.taskmanager.console.parser;


import java.util.List;


public record ParsedCommand(String name, List<String> args) {}
