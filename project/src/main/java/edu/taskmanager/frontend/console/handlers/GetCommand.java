package edu.taskmanager.frontend.console.handlers;


import java.util.List;
import java.util.Optional;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.frontend.console.Command;


public class GetCommand implements Command {
    private final ServisRepository servisRepository;

    public GetCommand(ServisRepository servisRepository) {
        this.servisRepository = servisRepository;
    }

    @Override
    public void execute(List<String> args) {
        if (args.size() < 2) {
            System.out.println("Использование: get <task|project|tag|user> <id>");
            return;
        }
        String type = args.get(0).toLowerCase();
        long id;
        try {
            id = Long.parseLong(args.get(1));
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом.");
            return;
        }

        switch (type) {
            case "task" -> getTask(id);
            case "project" -> getProject(id);
            case "tag" -> getTag(id);
            case "user" -> getUser(id);
            default -> System.out.println("Неизвестный тип: " + type);
        }
    }

    private void getTask(long id) {
        Optional<Task> opt = servisRepository.taskRepo().findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    private void getProject(long id) {
        Optional<Project> opt = servisRepository.projectRepo().findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Проект с ID " + id + " не найден.");
        }
    }

    private void getTag(long id) {
        Optional<Tag> opt = servisRepository.tagRepo().findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Тег с ID " + id + " не найден.");
        }
    }

    private void getUser(long id) {
        Optional<User> opt = servisRepository.userRepo().findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
    }

    @Override
    public String getDescription() {
        return "get <тип> <id> - показать объект по ID";
    }
}
