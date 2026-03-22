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

class BubbleTaskSortingStrategyTest {

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

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task B");
        task2.setPriority(Priority.HIGH);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setCreator(testUser);
        task2.setProject(testProject);
        task2.setCreatedAt(LocalDateTime.now().minusDays(2));

        Task task3 = new Task();
        task3.setId(3L);
        task3.setTitle("Task C");
        task3.setPriority(Priority.MEDIUM);
        task3.setStatus(TaskStatus.DONE);
        task3.setCreator(testUser);
        task3.setProject(testProject);
        task3.setCreatedAt(LocalDateTime.now().minusDays(1));

        Task task4 = new Task();
        task4.setId(4L);
        task4.setTitle("Task D");
        task4.setPriority(Priority.CRITICAL);
        task4.setStatus(TaskStatus.TODO);
        task4.setCreator(testUser);
        task4.setProject(testProject);
        task4.setCreatedAt(LocalDateTime.now());

        tasks.addAll(Arrays.asList(task1, task2, task3, task4));
    }

    @Test
    void bubbleSort_ShouldSortByPriorityAscending() {
        Comparator<Task> priorityComparator = Comparator.comparing(Task::getPriority);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.LOW, sortedTasks.get(0).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(2).getPriority());
        assertEquals(Priority.CRITICAL, sortedTasks.get(3).getPriority());

        assertEquals(4, tasks.size());
        assertEquals(Priority.LOW, tasks.get(0).getPriority());
    }

    @Test
    void bubbleSort_ShouldSortByPriorityDescending() {
        Comparator<Task> priorityComparator = (
            (t1, t2) -> t2.getPriority().compareTo(t1.getPriority())
        );
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.CRITICAL, sortedTasks.get(0).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(3).getPriority());
    }

    @Test
    void bubbleSort_ShouldSortByStatus() {
        Comparator<Task> statusComparator = Comparator.comparing(Task::getStatus);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, statusComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals(TaskStatus.TODO, sortedTasks.get(0).getStatus());
        assertEquals(TaskStatus.TODO, sortedTasks.get(1).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, sortedTasks.get(2).getStatus());
        assertEquals(TaskStatus.DONE, sortedTasks.get(3).getStatus());
    }

    @Test
    void bubbleSort_ShouldSortByTitle() {
        Comparator<Task> titleComparator = Comparator.comparing(Task::getTitle);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, titleComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals("Task A", sortedTasks.get(0).getTitle());
        assertEquals("Task B", sortedTasks.get(1).getTitle());
        assertEquals("Task C", sortedTasks.get(2).getTitle());
        assertEquals("Task D", sortedTasks.get(3).getTitle());
    }

    @Test
    void bubbleSort_ShouldSortByPriorityAndThenByTitle() {
        Comparator<Task> priorityThenTitleComparator = Comparator
        .comparing(Task::getPriority)
        .thenComparing(Task::getTitle);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

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

        List<Task> sortedTasks = strategy.sort(
            tasks, priorityThenTitleComparator
        );

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
    void bubbleSort_ShouldHandleEmptyList() {
        List<Task> emptyList = new ArrayList<>();
        Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(emptyList, comparator);

        assertTrue(sortedTasks.isEmpty());
    }

    @Test
    void bubbleSort_ShouldHandleSingleElementList() {
        List<Task> singleTaskList = Arrays.asList(tasks.get(0));
        Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(singleTaskList, comparator);

        assertEquals(1, sortedTasks.size());
        assertEquals(tasks.get(0).getId(), sortedTasks.get(0).getId());
    }

    @Test
    void bubbleSort_ShouldNotModifyOriginalList() {
        List<Task> originalList = new ArrayList<>(tasks);
        Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        strategy.sort(tasks, comparator);

        assertEquals(originalList.size(), tasks.size());
        for (int i = 0; i < originalList.size(); i++) {
            assertEquals(originalList.get(i).getId(), tasks.get(i).getId());
        }
    }

    @Test
    void bubbleSort_ShouldHandleNullComparator() {
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();
        assertThrows(
            NullPointerException.class,
            () -> strategy.sort(tasks, null)
        );
    }

    @Test
    void bubbleSort_ShouldHandleNullTasksInList() {
        List<Task> listWithNull = new ArrayList<>(tasks);
        listWithNull.add(null);
        Comparator<Task> comparator = Comparator.comparing(Task::getTitle);
        BubbleTaskSortingStrategy strategy = new BubbleTaskSortingStrategy();

        assertThrows(
            NullPointerException.class,
            () -> strategy.sort(listWithNull, comparator)
        );
    }
}
