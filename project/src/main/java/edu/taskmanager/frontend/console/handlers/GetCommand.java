package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.TagRepository;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.backend.service.TaskService;
import edu.taskmanager.frontend.console.Command;

import java.util.List;
import java.util.Optional;

public class GetCommand implements Command {
    private final TaskService taskService;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final User currentUser;

    public GetCommand(TaskService taskService,
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
        try {
            Optional<Task> opt = taskService.getTask(id, currentUser);
            if (opt.isPresent()) {
                System.out.println(opt.get());
            } else {
                System.out.println("Задача с ID " + id + " не найдена.");
            }
        } catch (SecurityException e) {
            System.out.println("Ошибка доступа: " + e.getMessage());
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
