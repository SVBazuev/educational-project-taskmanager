package edu.taskmanager.frontend.console.handlers;


import static edu.taskmanager.frontend.console.parser.ArgumentParser.DATE_FORMATTER;
import static edu.taskmanager.frontend.console.parser.ArgumentParser.parseArguments;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import edu.taskmanager.backend.chain.*;
import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.TagRepository;
import edu.taskmanager.backend.repository.TaskRepository;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.frontend.console.Command;
import edu.taskmanager.frontend.console.parser.ArgumentParser;

public class FilterCommand implements Command {
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private List<Task> lastResult;

    public FilterCommand(
            TaskRepository taskRepository, TagRepository tagRepository,
            ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.lastResult = new ArrayList<>();
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: filter&ключ1=значение1&ключ2=значение2...");
            System.out.println("Доступные ключи: description, status, tag, project, user, priority, " +
                    "duestartdate, dueenddate, startdate, enddate, upstartdate, upenddate");
            return;
        }

        // Парсим аргументы в карту критериев
        Map<String, String> criteria = parseArguments(args);

        // Строим цепочку фильтров
        FilterChain filterChain = new FilterChain();

        criteria.entrySet().stream()
                .forEach(filterChain::create);

        if (filterChain.isEmpty()) {
            System.out.println("Не задано ни одного корректного критерия фильтрации.");
            return;
        }

        // Получаем все задачи и фильтруем
        List<Task> allTasks = taskRepository.findAll();
        lastResult = filterChain.apply(allTasks);

        // Вывод
        if (lastResult.isEmpty()) {
            System.out.println("Задачи, удовлетворяющие условиям, не найдены.");
        } else {
            System.out.println("Найдено задач: " + lastResult.size());
            lastResult.forEach(System.out::println);
        }
    }

    public List<Task> getLastResult() {
        return lastResult != null ? new ArrayList<>(lastResult) : new ArrayList<>();
    }

    @Override
    public String getDescription() {

        return new StringBuilder()
            .append("filter&ключ=значение&... - фильтрация задач\n")
            .append("                   ")
            .append("Доступные поля:\n")
            .append("                       ")
            .append("description, dueDate, creator,\n")
            .append("                       ")
            .append("priority, status, project, tag, subtasks,\n")
            .append("                       ")
            .append("contractors, createdAt, updatedAt, parentId")
            .toString();
    }
}
