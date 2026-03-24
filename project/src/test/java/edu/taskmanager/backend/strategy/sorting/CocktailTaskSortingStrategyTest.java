package edu.taskmanager.backend.strategy.sorting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;

class CocktailTaskSortingStrategyTest {
    private List<Task> tasks;
    private Comparator<Task> priorityComparator;
    private Comparator<Task> dueDateComparator;
    private Comparator<Task> titleComparator;
    private TaskSortingStrategy sortingStrategy;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        
        // Компаратор для сортировки по приоритету (от высокого к низкому)
        priorityComparator = Comparator.comparing(
            (Task task) -> task.getPriority().ordinal(), 
            Comparator.reverseOrder()
        );
        
        // Компаратор для сортировки по сроку выполнения
        dueDateComparator = Comparator.comparing(Task::getDueDate);
        
        // Компаратор для сортировки по названию
        titleComparator = Comparator.comparing(Task::getTitle);
        sortingStrategy = new CocktailTaskSortingStrategy();
    }

    private Task createTask(String title, Priority priority, LocalDateTime dueDate) {
        Task task = new Task(title, dueDate, null, priority, TaskStatus.TODO);
        task.setId((long) (Math.random() * 1000));
        return task;
    }

    private Task createTask(String title, Priority priority) {
        return createTask(title, priority, LocalDateTime.now().plusDays(1));
    }

    @Test
    void cocktailSort_ShouldSortTasksByPriorityDescending() {
        // Arrange
        Task lowTask = createTask("Low Priority", Priority.LOW);
        Task highTask = createTask("High Priority", Priority.HIGH);
        Task mediumTask = createTask("Medium Priority", Priority.MEDIUM);
        Task criticalTask = createTask("Critical Priority", Priority.CRITICAL);
        
        tasks.addAll(Arrays.asList(lowTask, highTask, mediumTask, criticalTask));
        
        // Act - ВАЖНО: используем cocktailSort, а не sort!
        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);
        
        // Assert
        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.CRITICAL, sortedTasks.get(0).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(3).getPriority());
    }

    @Test
    void cocktailSort_ShouldSortTasksByDueDate() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Task task1 = createTask("Task 1", Priority.MEDIUM, now.plusDays(3));
        Task task2 = createTask("Task 2", Priority.HIGH, now.plusDays(1));
        Task task3 = createTask("Task 3", Priority.LOW, now.plusDays(2));
        
        tasks.addAll(Arrays.asList(task1, task2, task3));
        
        // Act
        List<Task> sortedTasks = sortingStrategy.sort(tasks, dueDateComparator);
        
        // Assert
        assertEquals("Task 2", sortedTasks.get(0).getTitle());
        assertEquals("Task 3", sortedTasks.get(1).getTitle());
        assertEquals("Task 1", sortedTasks.get(2).getTitle());
    }

    @Test
    void cocktailSort_ShouldSortTasksByTitle() {
        // Arrange
        Task taskA = createTask("Alpha", Priority.HIGH);
        Task taskB = createTask("Beta", Priority.MEDIUM);
        Task taskC = createTask("Gamma", Priority.LOW);
        
        tasks.addAll(Arrays.asList(taskC, taskA, taskB));
        
        // Act
        List<Task> sortedTasks = sortingStrategy.sort(tasks, titleComparator);
        
        // Assert
        assertEquals("Alpha", sortedTasks.get(0).getTitle());
        assertEquals("Beta", sortedTasks.get(1).getTitle());
        assertEquals("Gamma", sortedTasks.get(2).getTitle());
    }

    @Test
    void cocktailSort_ShouldHandleEmptyList() {
        // Act
        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);
        
        // Assert
        assertTrue(sortedTasks.isEmpty());
    }

    @Test
    void cocktailSort_ShouldHandleSingleElement() {
        // Arrange
        tasks.add(createTask("Single Task", Priority.HIGH));
        
        // Act
        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);
        
        // Assert
        assertEquals(1, sortedTasks.size());
        assertEquals("Single Task", sortedTasks.get(0).getTitle());
    }

    @Test
    void cocktailSort_ShouldHandleAlreadySortedList() {
        // Arrange
        Task criticalTask = createTask("Critical", Priority.CRITICAL);
        Task highTask = createTask("High", Priority.HIGH);
        Task mediumTask = createTask("Medium", Priority.MEDIUM);
        
        tasks.addAll(Arrays.asList(criticalTask, highTask, mediumTask));
        
        // Act
        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);
        
        // Assert
        assertEquals(Priority.CRITICAL, sortedTasks.get(0).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
    }

    @Test
    void cocktailSort_ShouldHandleReverseSortedList() {
        // Arrange
        Task lowTask = createTask("Low", Priority.LOW);
        Task mediumTask = createTask("Medium", Priority.MEDIUM);
        Task highTask = createTask("High", Priority.HIGH);
        
        tasks.addAll(Arrays.asList(lowTask, mediumTask, highTask));
        
        // Act
        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);
        
        // Assert
        assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(2).getPriority());
    }

    @Test
    void cocktailSort_ShouldHandleDuplicatePriorities() {
        // Arrange
        Task highTask1 = createTask("High 1", Priority.HIGH);
        Task highTask2 = createTask("High 2", Priority.HIGH);
        Task mediumTask = createTask("Medium", Priority.MEDIUM);
        
        tasks.addAll(Arrays.asList(mediumTask, highTask2, highTask1));
        
        // Act
        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);
        
        // Assert
        assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
    }

    @Test
    void cocktailSort_ShouldNotModifyOriginalList() {
        // Arrange
        Task task1 = createTask("Task 1", Priority.LOW);
        Task task2 = createTask("Task 2", Priority.HIGH);
        Task task3 = createTask("Task 3", Priority.MEDIUM);
        
        List<Task> originalList = new ArrayList<>(Arrays.asList(task1, task2, task3));
        List<Task> originalCopy = new ArrayList<>(originalList);
        
        // Act
        List<Task> sortedList = sortingStrategy.sort(originalList, priorityComparator);
        
        // Assert
        assertEquals(originalCopy, originalList); // Исходный список не изменился
        assertNotSame(sortedList, originalList);  // Возвращен новый список
    }

    @Test
    void cocktailSort_ShouldThrowException_WhenComparatorIsNull() {
        // Arrange
        tasks.add(createTask("Task", Priority.HIGH));
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            sortingStrategy.sort(tasks, null);
        });
    }
}