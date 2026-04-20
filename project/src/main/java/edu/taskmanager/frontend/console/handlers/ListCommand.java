package edu.taskmanager.frontend.console.handlers;


import java.util.List;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.repository.*;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.frontend.console.Command;


public class ListCommand implements Command {
    private final ServisRepository servisRepository;

    public ListCommand(ServisRepository servisRepository) {
        this.servisRepository = servisRepository;
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
        List<Task> tasks = servisRepository.taskRepo().findAll();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("Список задач:");
            tasks.forEach(System.out::println);
        }
    }

    private void listProjects() {
        List<Project> projects = servisRepository.projectRepo().findAll();
        if (projects.isEmpty()) {
            System.out.println("Список проектов пуст.");
        } else {
            System.out.println("Список проектов:");
            projects.forEach(System.out::println);
        }
    }

    private void listTags() {
        List<Tag> tags = servisRepository.tagRepo().findAll();
        if (tags.isEmpty()) {
            System.out.println("Список тегов пуст.");
        } else {
            System.out.println("Список тегов:");
            tags.forEach(System.out::println);
        }
    }

    private void listUsers() {
        List<User> users = servisRepository.userRepo().findAll();
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
