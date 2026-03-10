package edu.taskmanager.frontend.console.handlers;


import java.util.List;
import java.util.Optional;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.frontend.console.Command;


public class GetCommand implements Command {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public GetCommand(
            TaskRepository taskRepository, ProjectRepository projectRepository,
            TagRepository tagRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
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
        Optional<Task> opt = taskRepository.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    private void getProject(long id) {
        Optional<Project> opt = projectRepository.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Проект с ID " + id + " не найден.");
        }
    }

    private void getTag(long id) {
        Optional<Tag> opt = tagRepository.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Тег с ID " + id + " не найден.");
        }
    }

    private void getUser(long id) {
        Optional<User> opt = userRepository.findById(id);
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
