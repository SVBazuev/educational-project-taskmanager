package edu.taskmanager.console.handlers;


import java.util.List;


import edu.taskmanager.console.Command;
import edu.taskmanager.repository.*;


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
        if (args.size() < 2) {
            System.out.println("Использование: delete <task|project|tag|user> <id>");
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
            case "task" -> deleteTask(id);
            case "project" -> deleteProject(id);
            case "tag" -> deleteTag(id);
            case "user" -> deleteUser(id);
            default -> System.out.println("Неизвестный тип: " + type);
        }
    }

    private void deleteTask(long id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.deleteById(id);
            System.out.println("Задача с ID " + id + " удалена.");
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    private void deleteProject(long id) {
        if (projectRepository.findById(id).isPresent()) {
            projectRepository.deleteById(id);
            System.out.println("Проект с ID " + id + " удалён.");
        } else {
            System.out.println("Проект с ID " + id + " не найден.");
        }
    }

    private void deleteTag(long id) {
        if (tagRepository.findById(id).isPresent()) {
            tagRepository.deleteById(id);
            System.out.println("Тег с ID " + id + " удалён.");
        } else {
            System.out.println("Тег с ID " + id + " не найден.");
        }
    }

    private void deleteUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            System.out.println("Пользователь с ID " + id + " удалён.");
        } else {
            System.out.println("Пользователь с ID " + id + " не найден.");
        }
    }

    @Override
    public String getDescription() {
        return "delete <тип> <id> - удалить объект";
    }
}
