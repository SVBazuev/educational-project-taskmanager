package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.service.TaskService;
import edu.taskmanager.backend.strategy.sorting.*;
import edu.taskmanager.frontend.console.Command;

import java.time.LocalDateTime;
import java.util.*;

public class SortingCommand implements Command {
    private final TaskService taskService;
    private final User currentUser;

    public SortingCommand(TaskService taskService, User currentUser) {
        this.taskService = taskService;
        this.currentUser = currentUser;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: sorting&типСортировки&поле1&поле2...");
            System.out.println("Типы: bubblesort, cocktailsort, mergesort, insertionsort, quicksort");
            System.out.println("Поля: title, description, dueDate, creator, priority, status, project, tag, subtasks, contractors, createdAt, updatedAt, parentId");
            return;
        }

        String type = args.get(0).toLowerCase();
        List<String> params = args.subList(1, args.size());

        Set<String> criteria = new HashSet<>();
        for (String arg : params) {
            String[] keys = arg.split("&");
            for (String key : keys) {
                if (!key.trim().isEmpty()) {
                    criteria.add(key.trim().toLowerCase());
                } else {
                    System.out.println("Пропущен пустой аргумент: " + arg);
                }
            }
        }

        if (criteria.isEmpty()) {
            System.out.println("Не указаны поля для сортировки.");
            return;
        }

        try {
            List<Task> allTasks = taskService.getAllTasks(currentUser);
            if (allTasks.isEmpty()) {
                System.out.println("Нет задач для сортировки");
                return;
            }

            Comparator<Task> comparator = buildComparator(criteria);
            List<Task> sortedTasks = new ArrayList<>();

            switch (type) {
                case "bubblesort" -> sortedTasks = BubbleTaskSortingStrategy.bubbleSort(allTasks, comparator);
                case "cocktailsort" -> sortedTasks = CocktailTaskSortingStrategy.cocktailSort(allTasks, comparator);
                case "mergesort" -> sortedTasks = MergeTaskSortingStrategy.mergeSort(allTasks, comparator);
                case "insertionsort" -> sortedTasks = InsertionTaskSortingStrategy.insertionSort(allTasks, comparator);
                case "quicksort" -> sortedTasks = QuickTaskSortingStrategy.quickSort(allTasks, comparator);
                default -> {
                    System.out.println("Неизвестная сортировка: " + type);
                    return;
                }
            }

            System.out.println("Отсортировано задач: " + sortedTasks.size());
            sortedTasks.forEach(System.out::println);
        } catch (SecurityException e) {
            System.out.println("Ошибка доступа: " + e.getMessage());
        }
    }

    private Comparator<Task> buildComparator(Set<String> fields) {
        List<Comparator<Task>> comparators = new ArrayList<>();
        for (String field : fields) {
            switch (field) {
                case "title" -> comparators.add(Comparator.comparing(Task::getTitle, Comparator.nullsLast(String::compareTo)));
                case "description" -> comparators.add(Comparator.comparing(Task::getDescription, Comparator.nullsLast(String::compareTo)));
                case "duedate" -> comparators.add(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(LocalDateTime::compareTo)));
                case "creator" -> comparators.add(Comparator.comparing(t -> t.getCreator() != null ? t.getCreator().getUsername() : "", Comparator.nullsLast(String::compareTo)));
                case "priority" -> comparators.add(Comparator.comparing(Task::getPriority, Comparator.nullsLast(Enum::compareTo)).reversed());
                case "status" -> comparators.add(Comparator.comparing(Task::getStatus, Comparator.nullsLast(Enum::compareTo)));
                case "project" -> comparators.add(Comparator.comparing(t -> t.getProject() != null ? t.getProject().getName() : "", Comparator.nullsLast(String::compareTo)));
                case "tag" -> comparators.add(Comparator.comparing(t -> t.getTags() != null ? t.getTags().size() : 0));
                case "subtasks" -> comparators.add(Comparator.comparing(t -> t.getSubtasks() != null ? t.getSubtasks().size() : 0));
                case "contractors" -> comparators.add(Comparator.comparing(t -> t.getContractors() != null ? t.getContractors().size() : 0));
                case "createdat" -> comparators.add(Comparator.comparing(Task::getCreatedAt, Comparator.nullsLast(LocalDateTime::compareTo)));
                case "updatedat" -> comparators.add(Comparator.comparing(Task::getUpdatedAt, Comparator.nullsLast(LocalDateTime::compareTo)));
                case "parentid" -> comparators.add(Comparator.comparing(Task::getParentId, Comparator.nullsLast(Long::compareTo)));
                default -> System.out.println("Неизвестное поле для сортировки: " + field);
            }
        }

        Comparator<Task> combined = null;
        if (!comparators.isEmpty()) {
            combined = comparators.get(0);
            for (int i = 1; i < comparators.size(); i++) {
                combined = combined.thenComparing(comparators.get(i));
            }
        }
        return combined;
    }

    @Override
    public String getDescription() {
        return "sorting&тип&поле... - сортировка задач (тип: bubblesort, cocktailsort, mergesort, insertionsort, quicksort)";
    }
}
