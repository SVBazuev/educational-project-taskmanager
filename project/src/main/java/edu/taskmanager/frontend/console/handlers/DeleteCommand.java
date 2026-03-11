package edu.taskmanager.frontend.console.handlers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.taskmanager.backend.repository.*;
import edu.taskmanager.frontend.console.Command;


public class DeleteCommand implements Command {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public DeleteCommand(
            TaskRepository taskRepository, ProjectRepository projectRepository,
            TagRepository tagRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: delete <task|project|tag|user> <id>");
            return;
        }
        String type = args.get(0).toLowerCase();
        List<String> params = args.subList(1, args.size());

        // Парсим аргументы в карту критериев
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
            if (taskRepository.findById(id).isPresent()) {
                taskRepository.deleteById(id);
                System.out.println("Задача с ID " + id + " удалена.");
            } else {
                System.out.println("Задача с ID " + id + " не найдена.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом");
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
                System.out.println("Проект с ID" + id + " удален.");
            } else {
                System.out.println("Проект с ID" + id + " не найден");
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
                System.out.println("Пользователь с ID" + id + " не найден");
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
