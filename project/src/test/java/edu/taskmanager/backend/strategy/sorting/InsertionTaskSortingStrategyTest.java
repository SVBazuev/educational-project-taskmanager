package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InsertionTaskSortingStrategyTest {

    private List<Task> tasks;
    private User testUser;
    private Project testProject;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");

        tasks = new ArrayList<>();

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task A");
        task1.setPriority(Priority.LOW);
        task1.setStatus(TaskStatus.TODO);
        task1.setCreator(testUser);
        task1.setProject(testProject);
        task1.setCreatedAt(LocalDateTime.now().minusDays(3));
        task1.setDueDate(LocalDateTime.now().plusDays(5));

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task B");
        task2.setPriority(Priority.HIGH);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setCreator(testUser);
        task2.setProject(testProject);
        task2.setCreatedAt(LocalDateTime.now().minusDays(2));
        task2.setDueDate(LocalDateTime.now().plusDays(2));

        Task task3 = new Task();
        task3.setId(3L);
        task3.setTitle("Task C");
        task3.setPriority(Priority.MEDIUM);
        task3.setStatus(TaskStatus.DONE);
        task3.setCreator(testUser);
        task3.setProject(testProject);
        task3.setCreatedAt(LocalDateTime.now().minusDays(1));
        task3.setDueDate(LocalDateTime.now().plusDays(3));

        Task task4 = new Task();
        task4.setId(4L);
        task4.setTitle("Task D");
        task4.setPriority(Priority.CRITICAL);
        task4.setStatus(TaskStatus.TODO);
        task4.setCreator(testUser);
        task4.setProject(testProject);
        task4.setCreatedAt(LocalDateTime.now());
        task4.setDueDate(LocalDateTime.now().plusDays(1));

        tasks.addAll(Arrays.asList(task1, task2, task3, task4));
    }

    @Test
    void insertionSort_ShouldSortByPriorityAscending() {
        Comparator<Task> priorityComparator = Comparator.comparing(Task::getPriority);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.LOW, sortedTasks.get(0).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(2).getPriority());
        assertEquals(Priority.CRITICAL, sortedTasks.get(3).getPriority());
    }

    @Test
    void insertionSort_ShouldSortByDueDate() {
        Comparator<Task> dueDateComparator = Comparator.comparing(Task::getDueDate);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, dueDateComparator);

        assertEquals("Task D", sortedTasks.get(0).getTitle());
        assertEquals("Task B", sortedTasks.get(1).getTitle());
        assertEquals("Task C", sortedTasks.get(2).getTitle());
        assertEquals("Task A", sortedTasks.get(3).getTitle());
    }

    @Test
    void insertionSort_ShouldSortByTitleDescending() {
        Comparator<Task> titleComparator = (t1, t2) -> t2.getTitle().compareTo(t1.getTitle());
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, titleComparator);

        assertEquals("Task D", sortedTasks.get(0).getTitle());
        assertEquals("Task C", sortedTasks.get(1).getTitle());
        assertEquals("Task B", sortedTasks.get(2).getTitle());
        assertEquals("Task A", sortedTasks.get(3).getTitle());
    }

    @Test
    void insertionSort_ShouldSortByPriorityAndThenByTitle() {
        Comparator<Task> priorityThenTitleComparator = Comparator
            .comparing(Task::getPriority)
            .thenComparing(Task::getTitle);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        Task task5 = new Task();
        task5.setId(5L);
        task5.setTitle("Task E");
        task5.setPriority(Priority.LOW);
        task5.setStatus(TaskStatus.TODO);
        task5.setCreator(testUser);
        task5.setProject(testProject);

        Task task6 = new Task();
        task6.setId(6L);
        task6.setTitle("Task F");
        task6.setPriority(Priority.LOW);
        task6.setStatus(TaskStatus.TODO);
        task6.setCreator(testUser);
        task6.setProject(testProject);

        tasks.add(task6);
        tasks.add(task5);

        List<Task> sortedTasks = strategy.sort(tasks, priorityThenTitleComparator);

        assertEquals(6, sortedTasks.size());

        assertEquals(Priority.LOW, sortedTasks.get(0).getPriority());
        assertEquals("Task A", sortedTasks.get(0).getTitle());

        assertEquals(Priority.LOW, sortedTasks.get(1).getPriority());
        assertEquals("Task E", sortedTasks.get(1).getTitle());

        assertEquals(Priority.LOW, sortedTasks.get(2).getPriority());
        assertEquals("Task F", sortedTasks.get(2).getTitle());

        assertEquals(Priority.MEDIUM, sortedTasks.get(3).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(4).getPriority());
        assertEquals(Priority.CRITICAL, sortedTasks.get(5).getPriority());
    }

    @Test
    void insertionSort_ShouldHandleListWithDuplicatePriorities() {
        List<Task> duplicateTasks = new ArrayList<>();

        Task taskLow1 = new Task();
        taskLow1.setId(10L);
        taskLow1.setTitle("Low First");
        taskLow1.setPriority(Priority.LOW);
        taskLow1.setCreator(testUser);

        Task taskLow2 = new Task();
        taskLow2.setId(11L);
        taskLow2.setTitle("Low Second");
        taskLow2.setPriority(Priority.LOW);
        taskLow2.setCreator(testUser);

        Task taskHigh = new Task();
        taskHigh.setId(12L);
        taskHigh.setTitle("High Task");
        taskHigh.setPriority(Priority.HIGH);
        taskHigh.setCreator(testUser);

        duplicateTasks.add(taskHigh);
        duplicateTasks.add(taskLow2);
        duplicateTasks.add(taskLow1);

        Comparator<Task> priorityComparator = Comparator.comparing(Task::getPriority);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(duplicateTasks, priorityComparator);

        assertEquals(Priority.LOW, sortedTasks.get(0).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(1).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(2).getPriority());
    }

    @Test
    void insertionSort_ShouldHandleEmptyList() {
        List<Task> emptyList = new ArrayList<>();
        Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(emptyList, comparator);

        assertTrue(sortedTasks.isEmpty());
    }

    @Test
    void insertionSort_ShouldHandleSingleElement() {
        List<Task> singleTaskList = Arrays.asList(tasks.get(0));
        Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(singleTaskList, comparator);

        assertEquals(1, sortedTasks.size());
        assertEquals("Task A", sortedTasks.get(0).getTitle());
    }

    @Test
    void insertionSort_ShouldNotModifyOriginalList() {
        List<Task> originalCopy = new ArrayList<>(tasks);
        Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        strategy.sort(tasks, comparator);

        assertEquals(originalCopy.size(), tasks.size());
        for (int i = 0; i < originalCopy.size(); i++) {
            assertEquals(originalCopy.get(i).getId(), tasks.get(i).getId());
        }
    }

    @Test
    void insertionSort_ShouldThrowExceptionWhenComparatorIsNull() {
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();
        assertThrows(NullPointerException.class, () -> strategy.sort(tasks, null));
    }

    @Test
    void insertionSort_ShouldSortByCreatedAt() {
        Comparator<Task> createdAtComparator = Comparator.comparing(Task::getCreatedAt);
        InsertionTaskSortingStrategy strategy = new InsertionTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, createdAtComparator);

        assertEquals("Task A", sortedTasks.get(0).getTitle());
        assertEquals("Task B", sortedTasks.get(1).getTitle());
        assertEquals("Task C", sortedTasks.get(2).getTitle());
        assertEquals("Task D", sortedTasks.get(3).getTitle());
    }
}
