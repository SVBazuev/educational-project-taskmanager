package edu.taskmanager.frontend.console.handlers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.frontend.console.Command;

import static edu.taskmanager.frontend.console.parser.ArgumentParser.parseArguments;


public class DeleteCommand implements Command {
    private final ServisRepository servisRepository;

    public DeleteCommand(ServisRepository servisRepository) {
        this.servisRepository = servisRepository;
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
        Map<String, String> criteria = parseArguments(args);

        //servisRepository.deletById(type, criteria);

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
            if (servisRepository.taskRepo().findById(id).isPresent()) {
                servisRepository.taskRepo().deleteById(id);
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
            if (servisRepository.projectRepo().findById(id).isPresent()) {
                servisRepository.projectRepo().deleteById(id);
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
            if (servisRepository.tagRepo().findById(id).isPresent()) {
                servisRepository.tagRepo().deleteById(id);
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
            if (servisRepository.userRepo().findById(id).isPresent()) {
                servisRepository.userRepo().deleteById(id);
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
