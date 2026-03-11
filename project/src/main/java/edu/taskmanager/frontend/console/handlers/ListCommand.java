package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.ProjectRepository;
import edu.taskmanager.backend.repository.TagRepository;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.backend.service.TaskService;
import edu.taskmanager.frontend.console.Command;

import java.util.List;

public class ListCommand implements Command {
    private final TaskService taskService;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final User currentUser;

    public ListCommand(TaskService taskService,
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
            System.out.println("Не указан тип для списка. Используйте: list <tasks|projects|tags|users>");
            return;
        }
        String type = args.get(0).toLowerCase();
        switch (type) {
            case "tasks" -> listTasks();
            case "projects" -> listProjects();
            case "tags" -> listTags();
            case "users" -> listUsers();
            default -> System.out.println("Неизвестный тип: " + type);
        }
    }

    private void listTasks() {
        try {
            List<Task> tasks = taskService.getAllTasks(currentUser);
            if (tasks.isEmpty()) {
                System.out.println("Список задач пуст.");
            } else {
                System.out.println("Список задач:");
                tasks.forEach(System.out::println);
            }
        } catch (SecurityException e) {
            System.out.println("Ошибка доступа: " + e.getMessage());
        }
    }

    private void listProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            System.out.println("Список проектов пуст.");
        } else {
            System.out.println("Список проектов:");
            projects.forEach(System.out::println);
        }
    }

    private void listTags() {
        List<Tag> tags = tagRepository.findAll();
        if (tags.isEmpty()) {
            System.out.println("Список тегов пуст.");
        } else {
            System.out.println("Список тегов:");
            tags.forEach(System.out::println);
        }
    }

    private void listUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
        } else {
            System.out.println("Список пользователей:");
            users.forEach(System.out::println);
        }
    }

    @Override
    public String getDescription() {
        return "list <tasks|projects|tags|users> - показать список объектов";
    }
}
