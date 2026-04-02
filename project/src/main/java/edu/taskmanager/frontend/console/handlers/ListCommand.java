package edu.taskmanager.frontend.console.handlers;


import java.util.List;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.frontend.console.Command;


public class ListCommand implements Command {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public ListCommand(
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
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("Список задач:");
            tasks.forEach(System.out::println);
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
