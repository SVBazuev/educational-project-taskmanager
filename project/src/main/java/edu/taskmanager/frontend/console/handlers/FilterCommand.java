package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.chain.*;
import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.TagRepository;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.backend.service.TaskService;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.frontend.console.Command;
import edu.taskmanager.frontend.console.parser.ArgumentParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static edu.taskmanager.frontend.console.parser.ArgumentParser.DATE_FORMATTER;

public class FilterCommand implements Command {
    private final TaskService taskService;
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final User currentUser;

    public FilterCommand(TaskService taskService,
                         TagRepository tagRepository,
                         ProjectRepository projectRepository,
                         UserRepository userRepository,
                         User currentUser) {
        this.taskService = taskService;
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: filter&ключ1=значение1&ключ2=значение2...");
            System.out.println("Доступные ключи: description, status, tag, project, user, priority, " +
                    "duestartdate, dueenddate, startdate, enddate, upstartdate, upenddate");
            return;
        }

        Map<String, String> criteria = new HashMap<>();
        for (String arg : args) {
            String[] kv = arg.split("=", 2);
            if (kv.length == 2) {
                criteria.put(kv[0].toLowerCase(), kv[1]);
            } else {
                System.out.println("Пропущен некорректный аргумент: " + arg);
            }
        }

        FilterChain filterChain = new FilterChain();

        // Статус
        if (criteria.containsKey("status")) {
            try {
                TaskStatus status = TaskStatus.valueOf(criteria.get("status").toUpperCase());
                filterChain.addFilter(new StatusFilter(status));
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный статус. Допустимые: TODO, IN_PROGRESS, DONE.");
                return;
            }
        }

        // Тег
        if (criteria.containsKey("tag")) {
            String tagName = criteria.get("tag");
            Optional<Tag> tagOpt = tagRepository.findByName(tagName);
            if (tagOpt.isPresent()) {
                filterChain.addFilter(new TagFilter(tagOpt.get()));
            } else {
                System.out.println("Тег с именем '" + tagName + "' не найден.");
                return;
            }
        }

        // Проект
        if (criteria.containsKey("project")) {
            try {
                long projectId = Long.parseLong(criteria.get("project"));
                Optional<Project> projectOpt = projectRepository.findById(projectId);
                if (projectOpt.isPresent()) {
                    filterChain.addFilter(new ProjectFilter(projectOpt.get()));
                } else {
                    System.out.println("Проект с ID " + projectId + " не найден.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("ID проекта должно быть числом.");
                return;
            }
        }

        // Создатель
        if (criteria.containsKey("user")) {
            try {
                long userId = Long.parseLong(criteria.get("user"));
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    filterChain.addFilter(new CreatorFilter(userOpt.get()));
                } else {
                    System.out.println("Пользователь с ID " + userId + " не найден.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("ID пользователя должно быть числом.");
                return;
            }
        }

        // Приоритет
        if (criteria.containsKey("priority")) {
            try {
                Priority priority = Priority.valueOf(criteria.get("priority").toUpperCase());
                filterChain.addFilter(new PriorityFilter(priority));
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный приоритет. Допустимые: LOW, MEDIUM, HIGH, CRITICAL.");
                return;
            }
        }

        // Описание
        if (criteria.containsKey("description")) {
            String description = criteria.get("description").toLowerCase();
            filterChain.addFilter(new DescriptionFilter(description));
        }

        // Дата выполнения
        if (criteria.containsKey("duestartdate")) {
            try {
                LocalDateTime dueStartDate = LocalDateTime.parse(criteria.get("duestartdate"), DATE_FORMATTER);
                filterChain.addFilter(new DueDateFilter(dueStartDate, null));
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Используйте формат: yyyy-MM-dd HH:mm");
                return;
            }
        }
        if (criteria.containsKey("dueenddate")) {
            try {
                LocalDateTime dueEndDate = LocalDateTime.parse(criteria.get("dueenddate"), DATE_FORMATTER);
                filterChain.addFilter(new DueDateFilter(null, dueEndDate));
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Используйте формат: yyyy-MM-dd HH:mm");
                return;
            }
        }

        // Дата создания
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (criteria.containsKey("startdate") || criteria.containsKey("enddate")) {
            try {
                if (criteria.containsKey("startdate")) {
                    startDate = LocalDateTime.parse(criteria.get("startdate"), ArgumentParser.DATE_FORMATTER);
                }
                if (criteria.containsKey("enddate")) {
                    endDate = LocalDateTime.parse(criteria.get("enddate"), ArgumentParser.DATE_FORMATTER);
                }
                if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                    System.out.println("Дата начала не может быть позже даты окончания");
                    return;
                }
                filterChain.addFilter(new CreationDateFilter(startDate, endDate));
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Используйте формат: yyyy-MM-dd HH:mm");
                return;
            }
        }

        // Дата обновления
        LocalDateTime upStartDate = null;
        LocalDateTime upEndDate = null;
        if (criteria.containsKey("upstartdate") || criteria.containsKey("upenddate")) {
            try {
                if (criteria.containsKey("upstartdate")) {
                    upStartDate = LocalDateTime.parse(criteria.get("upstartdate"), ArgumentParser.DATE_FORMATTER);
                }
                if (criteria.containsKey("upenddate")) {
                    upEndDate = LocalDateTime.parse(criteria.get("upenddate"), ArgumentParser.DATE_FORMATTER);
                }
                if (upStartDate != null && upEndDate != null && upStartDate.isAfter(upEndDate)) {
                    System.out.println("Дата начала не может быть позже даты окончания");
                    return;
                }
                filterChain.addFilter(new UpdateDateFilter(upStartDate, upEndDate));
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Используйте формат: yyyy-MM-dd HH:mm");
                return;
            }
        }

        if (filterChain.isEmpty()) {
            System.out.println("Не задано ни одного корректного критерия фильтрации.");
            return;
        }

        try {
            List<Task> allTasks = taskService.getAllTasks(currentUser);
            List<Task> filtered = filterChain.apply(allTasks);
            if (filtered.isEmpty()) {
                System.out.println("Задачи, удовлетворяющие условиям, не найдены.");
            } else {
                System.out.println("Найдено задач: " + filtered.size());
                filtered.forEach(System.out::println);
            }
        } catch (SecurityException e) {
            System.out.println("Ошибка доступа: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "filter&ключ=значение... - фильтрация задач (description, status, tag, project, user, priority, " +
                "duestartdate, dueenddate, startdate, enddate, upstartdate, upenddate)";
    }
}
