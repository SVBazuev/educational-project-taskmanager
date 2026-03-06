package edu.taskmanager.console;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Реестр команд. Хранит сопоставление имени команды и её реализации.
 */
public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Регистрирует команду под указанным именем.
     * @param name имя команды (например, "create")
     * @param command реализация команды
     */
    public void register(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    /**
     * Возвращает команду по имени, или null, если не найдена.
     */
    public Command getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    /**
     * Возвращает множество всех зарегистрированных имён команд.
     */
    public Set<String> getCommandNames() {
        return commands.keySet();
    }

    /**
     * Проверяет, существует ли команда с таким именем.
     */
    public boolean hasCommand(String name) {
        return commands.containsKey(name.toLowerCase());
    }
}
