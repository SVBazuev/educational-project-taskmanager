package edu.taskmanager.console.util;


public class Printer {
    private Printer() {}

    public static void printWelcome() {
        System.out.println("Добро пожаловать в Task Manager!");
        System.out.println("Введите 'help' для списка команд.");
    }

    public static void printUnknownCommand(String command) {
        System.out.println("Неизвестная команда: " + command + ". Введите 'help' для справки.");
    }

    public static void printError(String message) {
        System.err.println("Ошибка: " + message);
    }
}
