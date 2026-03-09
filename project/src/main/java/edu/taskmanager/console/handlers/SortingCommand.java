package edu.taskmanager.console.handlers;

import edu.taskmanager.console.Command;
import edu.taskmanager.model.Project;
import edu.taskmanager.model.Task;
import edu.taskmanager.repository.ProjectRepository;
import edu.taskmanager.repository.TagRepository;
import edu.taskmanager.repository.TaskRepository;
import edu.taskmanager.repository.UserRepository;
import edu.taskmanager.strategy.sorting.BubbleSort;

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
            System.out.println("Использование: sorting&значение1&значение2...");
            //System.out.println("Доступна комбинация : status, tag, project");
            return;
        }

        String type = args.get(0).toLowerCase();
        List<String> params = args.subList(1, args.size());

        // Парсим аргументы в карту критериев
        Set<String> criteria = new HashSet<>();
        for (String arg : params) {
            String[] keys = arg.split("&");
            for (String key : keys) {
                if (!key.trim().isEmpty()) {
                    criteria.add(key.trim().toLowerCase());
                }
                else {
                    System.out.println("Пропущен пустой аргумент: " + arg);
                }
            }
        }

        List<Task> allTasks = taskRepository.findAll();
        List<Task> sortedTasks = new ArrayList<>(allTasks);

        switch (type) {
            case "bubblesort" -> bubbleSort(criteria, sortedTasks);
            default -> System.out.println("Неизвестная сортировка: " + type);
        }
    }

    private void bubbleSort(Set<String> criteria, List<Task> sortedTasks) {

        //if (criteria.isEmpty() || !criteria.contains("bubblesort")) {
        //    System.out.println("Использование: sorting bubbleSort <priority, status, title>=" + criteria);
        //    return;
        //}

        List<Comparator<Task>> comparators = new ArrayList<>();

        for (String field : criteria) {
            String trimmedField = field.trim().toLowerCase();

            switch (trimmedField) {

                case "priority":
                    comparators.add(Comparator.comparing(Task::getPriority).reversed());
                    break;
                case "status":
                    comparators.add(Comparator.comparing(Task::getStatus));
                    break;
                case "title":
                    comparators.add(Comparator.comparing(Task::getTitle));
                    break;
                default:
                    System.out.println("Неизвестное поле для сортировки: " + trimmedField);
            }
        }

        if (!comparators.isEmpty()) {
            Comparator<Task> combinedComparator = comparators.get(0);
            for (int i = 1; i < comparators.size(); i++) {
                combinedComparator = combinedComparator.thenComparing(comparators.get(i));
            }

            sortedTasks = BubbleSort.bubbleSort(sortedTasks, combinedComparator);

            System.out.println("Найдено задач: " + sortedTasks.size());
            sortedTasks.forEach(System.out::println);
        }
    }

    @Override
    public String getDescription() {
        return "sorting&тип сортировки&ключ=значение... - сортировки по полям (priority, status, title)";
    }
}
