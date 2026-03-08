package edu.taskmanager.console.handlers;

import edu.taskmanager.console.Command;
import edu.taskmanager.model.Task;
import edu.taskmanager.repository.ProjectRepository;
import edu.taskmanager.repository.TagRepository;
import edu.taskmanager.repository.TaskRepository;
import edu.taskmanager.repository.UserRepository;
import edu.taskmanager.strategy.sorting.PriorityStatusTitleSort;

import java.util.*;


public class SortingCommand implements Command {
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public SortingCommand(
            TaskRepository taskRepository, TagRepository tagRepository,
            ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: sorting&ключ1=значение1&ключ2=значение2...");
            //System.out.println("Доступна комбинация : status, tag, project");
            return;
        }

        // Парсим аргументы в карту критериев
        Map<String, String> criteria = new HashMap<>();
        for (String arg : args) {
            String[] kv = arg.split("=", 2);
            if (kv.length == 2) {
                criteria.put(kv[0].toLowerCase(), kv[1]);
            } else {
                System.out.println("Пропущен некорректный аргумент: " + arg);
            }
        }

        List<Task> allTasks = taskRepository.findAll();
        List<Task> sortedTasks = new ArrayList<>(allTasks);

        if (criteria.containsKey("priority")) {
            sortedTasks = PriorityStatusTitleSort.bubbleSort(sortedTasks,
                    Comparator.comparing(Task::getPriority).reversed());
        }

        if (criteria.containsKey("status")) {
            sortedTasks = PriorityStatusTitleSort.bubbleSort(sortedTasks,
                    Comparator.comparing(Task::getStatus));
        }

        if (criteria.containsKey("title")) {
            sortedTasks = PriorityStatusTitleSort.bubbleSort(sortedTasks,
                    Comparator.comparing(Task::getTitle));
        }






    }

    @Override
    public String getDescription() {
        return "";
    }
}
