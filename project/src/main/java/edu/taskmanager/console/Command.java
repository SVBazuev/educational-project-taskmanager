package edu.taskmanager.console;

import java.util.List;

public interface Command {
    /**
     * Выполняет команду с заданными аргументами.
     * @param args список аргументов (без имени команды)
     */
    void execute(List<String> args);

    /**
     * @return описание команды для справки
     */
    String getDescription();
}
