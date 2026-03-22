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

@BeforeEach
void setUp() {
tasks = new ArrayList<>();

priorityComparator = Comparator.comparing(
(Task task) -> task.getPriority().ordinal(),
Comparator.reverseOrder()
);

dueDateComparator = Comparator.comparing(Task::getDueDate);
titleComparator = Comparator.comparing(Task::getTitle);
}

private Task createTask(
String title, Priority priority, LocalDateTime dueDate) {
Task task = new Task(title, dueDate, null, priority, TaskStatus.TODO);
task.setId((long) (Math.random() * 1000));
return task;
}

private Task createTask(String title, Priority priority) {
return createTask(title, priority, LocalDateTime.now().plusDays(1));
}

@Test
void cocktailSort_ShouldSortTasksByPriorityDescending() {
Task lowTask = createTask("Low Priority", Priority.LOW);
Task highTask = createTask("High Priority", Priority.HIGH);
Task mediumTask = createTask("Medium Priority", Priority.MEDIUM);
Task criticalTask = createTask("Critical Priority", Priority.CRITICAL);

tasks.addAll(Arrays.asList(lowTask, highTask, mediumTask, criticalTask));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

assertEquals(4, sortedTasks.size());
assertEquals(Priority.CRITICAL, sortedTasks.get(0).getPriority());
assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
assertEquals(Priority.LOW, sortedTasks.get(3).getPriority());
}

@Test
void cocktailSort_ShouldSortTasksByDueDate() {
LocalDateTime now = LocalDateTime.now();
Task task1 = createTask("Task 1", Priority.MEDIUM, now.plusDays(3));
Task task2 = createTask("Task 2", Priority.HIGH, now.plusDays(1));
Task task3 = createTask("Task 3", Priority.LOW, now.plusDays(2));

tasks.addAll(Arrays.asList(task1, task2, task3));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, dueDateComparator);

assertEquals("Task 2", sortedTasks.get(0).getTitle());
assertEquals("Task 3", sortedTasks.get(1).getTitle());
assertEquals("Task 1", sortedTasks.get(2).getTitle());
}

@Test
void cocktailSort_ShouldSortTasksByTitle() {
Task taskA = createTask("Alpha", Priority.HIGH);
Task taskB = createTask("Beta", Priority.MEDIUM);
Task taskC = createTask("Gamma", Priority.LOW);

tasks.addAll(Arrays.asList(taskC, taskA, taskB));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, titleComparator);

assertEquals("Alpha", sortedTasks.get(0).getTitle());
assertEquals("Beta", sortedTasks.get(1).getTitle());
assertEquals("Gamma", sortedTasks.get(2).getTitle());
}

@Test
void cocktailSort_ShouldHandleEmptyList() {
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

assertTrue(sortedTasks.isEmpty());
}

@Test
void cocktailSort_ShouldHandleSingleElement() {
tasks.add(createTask("Single Task", Priority.HIGH));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

assertEquals(1, sortedTasks.size());
assertEquals("Single Task", sortedTasks.get(0).getTitle());
}

@Test
void cocktailSort_ShouldHandleAlreadySortedList() {
Task criticalTask = createTask("Critical", Priority.CRITICAL);
Task highTask = createTask("High", Priority.HIGH);
Task mediumTask = createTask("Medium", Priority.MEDIUM);

tasks.addAll(Arrays.asList(criticalTask, highTask, mediumTask));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

assertEquals(Priority.CRITICAL, sortedTasks.get(0).getPriority());
assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
}

@Test
void cocktailSort_ShouldHandleReverseSortedList() {
Task lowTask = createTask("Low", Priority.LOW);
Task mediumTask = createTask("Medium", Priority.MEDIUM);
Task highTask = createTask("High", Priority.HIGH);

tasks.addAll(Arrays.asList(lowTask, mediumTask, highTask));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());
assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
assertEquals(Priority.LOW, sortedTasks.get(2).getPriority());
}

@Test
void cocktailSort_ShouldHandleDuplicatePriorities() {
Task highTask1 = createTask("High 1", Priority.HIGH);
Task highTask2 = createTask("High 2", Priority.HIGH);
Task mediumTask = createTask("Medium", Priority.MEDIUM);

tasks.addAll(Arrays.asList(mediumTask, highTask2, highTask1));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());
assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
}

@Test
void cocktailSort_ShouldNotModifyOriginalList() {
Task task1 = createTask("Task 1", Priority.LOW);
Task task2 = createTask("Task 2", Priority.HIGH);
Task task3 = createTask("Task 3", Priority.MEDIUM);

List<Task> originalList = new ArrayList<>(Arrays.asList(task1, task2, task3));
List<Task> originalCopy = new ArrayList<>(originalList);
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

List<Task> sortedList = strategy.sort(originalList, priorityComparator);

assertEquals(originalCopy, originalList);
assertNotSame(sortedList, originalList);
}

@Test
void cocktailSort_ShouldThrowException_WhenComparatorIsNull() {
tasks.add(createTask("Task 1", Priority.HIGH));
tasks.add(createTask("Task 2", Priority.LOW));
CocktailTaskSortingStrategy strategy = new CocktailTaskSortingStrategy();

assertThrows(
NullPointerException.class,
() -> strategy.sort(tasks, null)
);
}
}
