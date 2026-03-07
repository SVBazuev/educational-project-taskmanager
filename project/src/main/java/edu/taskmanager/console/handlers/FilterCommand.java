package edu.taskmanager.console.handlers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.taskmanager.chain.CreatorFilter;
import edu.taskmanager.chain.FilterChain;
import edu.taskmanager.chain.PriorityFilter;
import edu.taskmanager.chain.ProjectFilter;
import edu.taskmanager.chain.StatusFilter;
import edu.taskmanager.chain.TagFilter;
import edu.taskmanager.console.Command;
import edu.taskmanager.model.Project;
import edu.taskmanager.model.Tag;
import edu.taskmanager.model.Task;
import edu.taskmanager.model.User;
import edu.taskmanager.repository.ProjectRepository;
import edu.taskmanager.repository.TagRepository;
import edu.taskmanager.repository.TaskRepository;
import edu.taskmanager.repository.UserRepository;
import edu.taskmanager.util.Priority;
import edu.taskmanager.util.TaskStatus;

public class FilterCommand implements Command {
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public FilterCommand(
            TaskRepository taskRepository, TagRepository tagRepository,
            ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: filter&ключ1=значение1&ключ2=значение2...");
            System.out.println("Доступные ключи: status, tag, project");
            return;
        }

        // Парсим аргументы в карту критериев
        Map<String, String> criteria = new HashMap<>();
        for (String arg : args) {
            String[] kv = arg.split("=", 2);
            if (kv.length == 2) {
                criteria.put(kv[0].toLowerCase(), kv[1]);
            } else {
                System.out.println("Пропущен некорректный аргумент: " + arg);
            }
        }

        // Строим цепочку фильтров
        FilterChain filterChain = new FilterChain();

        // Фильтр по статусу
        if (criteria.containsKey("status")) {
            try {
                TaskStatus status = TaskStatus.valueOf(criteria.get("status").toUpperCase());
                filterChain.addFilter(new StatusFilter(status));
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный статус. Допустимые: TODO, IN_PROGRESS, DONE.");
                return;
            }
        }

        // Фильтр по тегу
        if (criteria.containsKey("tag")) {
            String tagName = criteria.get("tag");
            // Нужен метод поиска тега по имени, добавим его в TagRepository
            Optional<Tag> tagOpt = tagRepository.findByName(tagName);
            if (tagOpt.isPresent()) {
                filterChain.addFilter(new TagFilter(tagOpt.get()));
            } else {
                System.out.println("Тег с именем '" + tagName + "' не найден.");
                return;
            }
        }

        // Фильтр по проекту
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

        // Фильтр по создателю задачи
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
        // Фильтр по приоритету
        if (criteria.containsKey("priority")) {
            try {
                Priority priority = Priority.valueOf(criteria.get("priorty").toUpperCase());
                filterChain.addFilter(new PriorityFilter(priority));
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный приоритет. Допустимые: LOW, MEDIUM, HIGH, CRITICAL.");
                return;
            }
        }
        if (filterChain.isEmpty()) {
            System.out.println("Не задано ни одного корректного критерия фильтрации.");
            return;
        }
        
        // Получаем все задачи и фильтруем
        List<Task> allTasks = taskRepository.findAll();
        List<Task> filtered = filterChain.apply(allTasks);

        // Вывод
        if (filtered.isEmpty()) {
            System.out.println("Задачи, удовлетворяющие условиям, не найдены.");
        } else {
            System.out.println("Найдено задач: " + filtered.size());
            filtered.forEach(System.out::println);
        }
    }

    @Override
    public String getDescription() {
        return "filter&ключ=значение... - фильтрация задач (status, tag, project,priority)";
    }
}
