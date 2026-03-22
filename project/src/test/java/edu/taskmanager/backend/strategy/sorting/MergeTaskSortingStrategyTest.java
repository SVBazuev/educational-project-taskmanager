package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
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

class MergeTaskSortingStrategyTest {

    private List<Task> tasks;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        tasks = new ArrayList<>();

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setPriority(Priority.HIGH);
        task1.setStatus(TaskStatus.TODO);
        task1.setCreator(testUser);
        task1.setCreatedAt(LocalDateTime.now().minusDays(1));

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setPriority(Priority.MEDIUM);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setCreator(testUser);
        task2.setCreatedAt(LocalDateTime.now().minusDays(2));

        Task task3 = new Task();
        task3.setId(3L);
        task3.setTitle("Task 3");
        task3.setPriority(Priority.LOW);
        task3.setStatus(TaskStatus.DONE);
        task3.setCreator(testUser);
        task3.setCreatedAt(LocalDateTime.now().minusDays(3));

        Task task4 = new Task();
        task4.setId(4L);
        task4.setTitle("Task 4");
        task4.setPriority(Priority.CRITICAL);
        task4.setStatus(TaskStatus.TODO);
        task4.setCreator(testUser);
        task4.setCreatedAt(LocalDateTime.now());

        tasks.addAll(Arrays.asList(task1, task2, task3, task4));
    }

    @Test
    void mergeSort_ShouldSortByPriorityAscending() {
        Comparator<Task> priorityComparator = Comparator
            .comparing(Task::getPriority);
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.LOW, sortedTasks.get(0).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(2).getPriority());
        assertEquals(Priority.CRITICAL, sortedTasks.get(3).getPriority());

        assertEquals(4, tasks.size());
        assertEquals(Priority.HIGH, tasks.get(0).getPriority());
    }

    @Test
    void mergeSort_ShouldSortByPriorityDescending() {
        Comparator<Task> priorityComparator = (t1, t2)
            -> t2.getPriority().compareTo(t1.getPriority());
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, priorityComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.CRITICAL, sortedTasks.get(0).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(3).getPriority());
    }

    @Test
    void mergeSort_ShouldSortByStatus() {
        Comparator<Task> statusComparator = Comparator
            .comparing(Task::getStatus);
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, statusComparator);

        assertEquals(4, sortedTasks.size());
        assertEquals(TaskStatus.TODO, sortedTasks.get(0).getStatus());
        assertEquals(TaskStatus.TODO, sortedTasks.get(1).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, sortedTasks.get(2).getStatus());
        assertEquals(TaskStatus.DONE, sortedTasks.get(3).getStatus());
    }

    @Test
    void mergeSort_ShouldSortByCreationDate() {
        Comparator<Task> dateComparator = Comparator
            .comparing(Task::getCreatedAt);
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, dateComparator);

        assertTrue(
            sortedTasks.get(0)
            .getCreatedAt()
            .isBefore(sortedTasks.get(1).getCreatedAt())
        );
        assertTrue(
            sortedTasks.get(1)
            .getCreatedAt()
            .isBefore(sortedTasks.get(2).getCreatedAt())
        );
        assertTrue(
            sortedTasks.get(2)
            .getCreatedAt()
            .isBefore(sortedTasks.get(3).getCreatedAt())
        );
    }

    @Test
    void mergeSort_ShouldSortByMultipleFields() {
        Comparator<Task> multiComparator = Comparator
            .comparing(Task::getStatus)
            .thenComparing(Task::getPriority)
            .thenComparing(Task::getCreatedAt);
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, multiComparator);

        assertEquals(TaskStatus.TODO, sortedTasks.get(0).getStatus());
        assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());

        assertEquals(TaskStatus.TODO, sortedTasks.get(1).getStatus());
        assertEquals(Priority.CRITICAL, sortedTasks.get(1).getPriority());

        assertEquals(TaskStatus.IN_PROGRESS, sortedTasks.get(2).getStatus());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());

        assertEquals(TaskStatus.DONE, sortedTasks.get(3).getStatus());
        assertEquals(Priority.LOW, sortedTasks.get(3).getPriority());
    }

    @Test
    void mergeSort_ShouldHandleEmptyList() {
        List<Task> emptyList = new ArrayList<>();
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(
            emptyList, Comparator.comparing(Task::getId)
        );

        assertTrue(sortedTasks.isEmpty());
    }

    @Test
    void mergeSort_ShouldHandleSingleElement() {
        List<Task> singleTaskList = new ArrayList<>();
        singleTaskList.add(tasks.get(0));
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(
            singleTaskList, Comparator.comparing(Task::getId)
        );

        assertEquals(1, sortedTasks.size());
        assertEquals(tasks.get(0).getId(), sortedTasks.get(0).getId());
    }

    @Test
    void mergeSort_ShouldSortByTitle() {
        Comparator<Task> titleComparator = Comparator.comparing(Task::getTitle);
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, titleComparator);

        assertEquals("Task 1", sortedTasks.get(0).getTitle());
        assertEquals("Task 2", sortedTasks.get(1).getTitle());
        assertEquals("Task 3", sortedTasks.get(2).getTitle());
        assertEquals("Task 4", sortedTasks.get(3).getTitle());
    }

    @Test
    void mergeSort_ShouldSortByCreator() {
        Comparator<Task> creatorComparator = Comparator
            .comparing(task -> task.getCreator().getUsername());
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(tasks, creatorComparator);

        for (Task task : sortedTasks) {
            assertEquals("testuser", task.getCreator().getUsername());
        }
    }

    @Test
    void mergeSort_ShouldHandleNullComparator() {
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();
        assertThrows(NullPointerException.class, () -> strategy.sort(tasks, null));
    }

    @Test
    void mergeSort_ShouldHandleLargeDataset() {
        List<Task> largeTasks = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        for (int i = 0; i < 1000; i++) {
            Task task = new Task();
            task.setId((long) i);
            task.setTitle("Task " + i);
            task.setPriority(Priority.values()[i % 4]);
            task.setStatus(TaskStatus.values()[i % 3]);
            task.setCreator(user);
            task.setCreatedAt(LocalDateTime.now().plusDays(i));
            largeTasks.add(task);
        }

        Comparator<Task> comparator = Comparator.comparing(Task::getCreatedAt);
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        List<Task> sortedTasks = strategy.sort(largeTasks, comparator);

        for (int i = 1; i < sortedTasks.size(); i++) {
            assertTrue(
                sortedTasks.get(i-1)
                .getCreatedAt()
                .isBefore(sortedTasks.get(i).getCreatedAt())
            );
        }
    }

    @Test
    void mergeSort_ShouldPreserveOriginalList() {
        List<Task> originalTasks = new ArrayList<>(tasks);
        Comparator<Task> comparator = Comparator.comparing(Task::getPriority);
        MergeTaskSortingStrategy strategy = new MergeTaskSortingStrategy();

        strategy.sort(tasks, comparator);

        for (int i = 0; i < tasks.size(); i++) {
            assertEquals(originalTasks.get(i).getId(), tasks.get(i).getId());
            assertEquals(originalTasks.get(i).getPriority(), tasks.get(i).getPriority());
            assertEquals(originalTasks.get(i).getStatus(), tasks.get(i).getStatus());
        }
    }
}
