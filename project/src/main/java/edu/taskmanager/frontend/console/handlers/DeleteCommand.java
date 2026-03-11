package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.TagRepository;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.backend.service.TaskService;
import edu.taskmanager.frontend.console.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DeleteCommand implements Command {
    private final TaskService taskService;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final User currentUser;

    public DeleteCommand(TaskService taskService,
                         ProjectRepository projectRepository,
                         TagRepository tagRepository,
                         UserRepository userRepository,
                         User currentUser) {
        this.taskService = taskService;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: delete <task|project|tag|user> <id>");
            return;
        }
        String type = args.get(0).toLowerCase();
        List<String> params = args.subList(1, args.size());

        Map<String, String> criteria = new HashMap<>();
        for (String arg : params) {
            String[] kv = arg.split("=", 2);
            if (kv.length == 2) {
                criteria.put(kv[0].toLowerCase(), kv[1]);
            } else {
                System.out.println("Пропущен некорректный аргумент: " + arg);
            }
        }

        switch (type) {
            case "task" -> deleteTask(criteria);
            case "project" -> deleteProject(criteria);
            case "tag" -> deleteTag(criteria);
            case "user" -> deleteUser(criteria);
            default -> System.out.println("Неизвестный тип: " + type);
        }
    }

    private void deleteTask(Map<String, String> criteria) {
        if (!criteria.containsKey("id")) {
            System.out.println("Использование: delete task id=<число>");
            return;
        }
        try {
            long id = Long.parseLong(criteria.get("id"));
            taskService.deleteTask(id, currentUser);
            System.out.println("Задача с ID " + id + " удалена.");
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом");
        } catch (SecurityException e) {
            System.out.println("Ошибка прав доступа: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteProject(Map<String, String> criteria) {
        if (!criteria.containsKey("id")) {
            System.out.println("Использование: delete project id=<число>");
            return;
        }
        try {
            long id = Long.parseLong(criteria.get("id"));
            if (projectRepository.findById(id).isPresent()) {
                projectRepository.deleteById(id);
                System.out.println("Проект с ID " + id + " удален.");
            } else {
                System.out.println("Проект с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом");
        }
    }

    private void deleteTag(Map<String, String> criteria) {
        if (!criteria.containsKey("id")) {
            System.out.println("Использование: delete tag id=<число>");
            return;
        }
        try {
            long id = Long.parseLong(criteria.get("id"));
            if (tagRepository.findById(id).isPresent()) {
                tagRepository.deleteById(id);
                System.out.println("Тег с ID " + id + " удален.");
            } else {
                System.out.println("Тег с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом");
        }
    }

    private void deleteUser(Map<String, String> criteria) {
        if (!criteria.containsKey("id")) {
            System.out.println("Использование: delete user id=<число>");
            return;
        }
        try {
            long id = Long.parseLong(criteria.get("id"));
            if (userRepository.findById(id).isPresent()) {
                userRepository.deleteById(id);
                System.out.println("Пользователь с ID " + id + " удален.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом");
        }
    }

    @Override
    public String getDescription() {
        return "delete <тип> <id> - удалить объект";
    }
}
