package edu.taskmanager.console.handlers;

import edu.taskmanager.console.Command;
import edu.taskmanager.model.Project;
import edu.taskmanager.model.Task;
import edu.taskmanager.repository.ProjectRepository;
import edu.taskmanager.repository.TagRepository;
import edu.taskmanager.repository.TaskRepository;
import edu.taskmanager.repository.UserRepository;
import edu.taskmanager.strategy.sorting.ВubbleTaskSortingStrategy;

import java.util.*;


public class SortingCommand implements Command {
    private final TaskRepository taskRepository;

    public SortingCommand(
            TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
        if (allTasks.isEmpty()) {
            System.out.println("Нет задач для сортировки");
            return;
        }
        List<Task> sortedTasks = new ArrayList<>(allTasks);

        switch (type) {
            case "bubblesort" -> bubbleSort(criteria, sortedTasks);
            default -> System.out.println("Неизвестная сортировка: " + type);
        }
    }

    private void bubbleSort(Set<String> criteria, List<Task> sortedTasks) {

        if (criteria.isEmpty()) {
            System.out.println("Не указаны поля для сортировки. Используйте: title, description, dueDate, creator," +
                    " priority, status, project, tag, subtasks, contractors, createdAt, updatedAt, parentId");
            return;
        }
        Comparator<Task> combinedComparator = sortTasks(criteria, sortedTasks);

        sortedTasks = ВubbleTaskSortingStrategy.bubbleSort(sortedTasks, combinedComparator);

        System.out.println("Найдено задач: " + sortedTasks.size());
        sortedTasks.forEach(System.out::println);
        }


    public Comparator<Task> sortTasks(Set<String> criteria, List<Task> sortedTasks) {

        List<Comparator<Task>> comparators = new ArrayList<>();
        for (String field : criteria) {
            String trimmedField = field.trim().toLowerCase();

            switch (trimmedField) {

                case "title":
                    comparators.add(Comparator.comparing(Task::getTitle));
                    break;
                case "description":
                    comparators.add(Comparator.comparing(Task::getDescription));
                    break;
                case "dueDate":
                    comparators.add(Comparator.comparing(Task::getDueDate));
                    break;
                case "creator":
                    comparators.add(Comparator.comparing(task -> task.getCreator().getUsername()));
                    break;
                case "priority":
                    comparators.add(Comparator.comparing(Task::getPriority).reversed());
                    break;
                case "status":
                    comparators.add(Comparator.comparing(Task::getStatus));
                    break;
                case "project":
                    comparators.add(Comparator.comparing(task -> task.getProject().getName()));
                    break;
                case "tag":
                    comparators.add(Comparator.comparing(task -> task.getTags().size()));
                    break;
                case "subtasks":
                    comparators.add(Comparator.comparing(task -> task.getSubtasks().size()));
                    break;
                case "contractors":
                    comparators.add(Comparator.comparing(task -> task.getContractors().size()));
                    break;
                case "createdat":
                    comparators.add(Comparator.comparing(Task::getCreatedAt));
                    break;
                case "updatedat":
                    comparators.add(Comparator.comparing(Task::getUpdatedAt));
                    break;
                case "parentid":
                    comparators.add(Comparator.comparing(Task::getParentId));
                    break;
                default:
                    System.out.println("Неизвестное поле для сортировки: " + trimmedField);
            }
        }

        Comparator<Task> combinedComparator = null;

        if (!comparators.isEmpty()) {
            combinedComparator = comparators.get(0);
            for (int i = 1; i < comparators.size(); i++) {
                combinedComparator = combinedComparator.thenComparing(comparators.get(i));
            }

        }
        return combinedComparator;
    }

    @Override
    public String getDescription() {
        return "sorting&тип сортировки&ключ=значение... - сортировки по полям (title, description, dueDate, creator,\" +\n" +
                "                    \" priority, status, project, tag, subtasks, contractors, createdAt, updatedAt, parentId)";
    }
}
