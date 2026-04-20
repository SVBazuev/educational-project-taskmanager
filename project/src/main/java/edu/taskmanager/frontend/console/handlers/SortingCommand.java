
package edu.taskmanager.frontend.console.handlers;

import java.util.*;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.backend.strategy.sorting.*;
import edu.taskmanager.frontend.console.Command;

import static edu.taskmanager.frontend.console.parser.ArgumentParser.parseArgumentsList;


public class SortingCommand implements Command {
    private final ServisRepository servisRepository;
    private TaskSortingStrategy sortingStrategy;
    private List<Task> lastResult;

    public SortingCommand(ServisRepository servisRepository) {
        this.servisRepository = servisRepository;
        this.lastResult = new ArrayList<>();
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: sorting&значение1&значение2...");
            return;
        }

        String type = args.get(0).toLowerCase();
        List<String> params = args.subList(1, args.size());

        // Парсим аргументы в карту критериев
        Set<String> criteria = parseArgumentsList(params);

        List<Task> allTasks = servisRepository.taskRepo().findAll();
        if (allTasks.isEmpty()) {
            System.out.println("Нет задач для сортировки");
            lastResult = new ArrayList<>();
            return;
        }
        List<Task> sortedTasks = new ArrayList<>(allTasks);

        Comparator<Task> combinedComparator = sortTasks(criteria, sortedTasks);

        switch (type) {
            case "bubblesort" -> sortingStrategy = new BubbleTaskSortingStrategy();
            case "cocktailsort" -> sortingStrategy = new CocktailTaskSortingStrategy();
            case "mergesort" -> sortingStrategy = new MergeTaskSortingStrategy();
            case "insertionsort" -> sortingStrategy = new InsertionTaskSortingStrategy();
            case "quicksort" -> sortingStrategy = new QuickTaskSortingStrategy();
            case "bubblesortswap" -> sortingStrategy = new BubblesSortEvenIdsOnly();
            default -> System.out.println("Неизвестная сортировка: " + type);
        }
        sortedTasks = sortingStrategy.sort(sortedTasks, combinedComparator);

        lastResult = new ArrayList<>(sortedTasks);

        System.out.println("Найдено задач: " + sortedTasks.size());
        sortedTasks.forEach(System.out::println);
    }

    public List<Task> getLastResult() {
        return lastResult != null ? new ArrayList<>(lastResult) : new ArrayList<>();
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
        return new StringBuilder()
            .append("sorting&тип сортировки&поле&...")
            .append(" - сортировка задач\n")
            .append("                   ")
            .append("Доступные поля:\n")
            .append("                       ")
            .append("title, description, dueDate, creator,\n")
            .append("                       ")
            .append("priority, status, project, tag, subtasks,\n")
            .append("                       ")
            .append("contractors, createdAt, updatedAt, parentId")
            .toString();
    }
}
