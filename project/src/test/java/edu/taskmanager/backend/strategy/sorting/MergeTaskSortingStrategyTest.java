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
    private TaskSortingStrategy sortingStrategy;

    @BeforeEach
    void setUp() {
        // Создаем тестового пользователя
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Создаем тестовые задачи
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
        sortingStrategy = new MergeTaskSortingStrategy();
    }

    @Test
    void mergeSort_ShouldSortByPriorityAscending() {
        // Создаем компаратор для сортировки по приоритету (от низкого к высокому)
        Comparator<Task> priorityComparator = Comparator.comparing(Task::getPriority);

        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);

        // Проверяем, что задачи отсортированы по приоритету
        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.LOW, sortedTasks.get(0).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(2).getPriority());
        assertEquals(Priority.CRITICAL, sortedTasks.get(3).getPriority());

        // Проверяем, что исходный список не изменился
        assertEquals(4, tasks.size());
        assertEquals(Priority.HIGH, tasks.get(0).getPriority());
    }

    @Test
    void mergeSort_ShouldSortByPriorityDescending() {
        // Создаем компаратор для сортировки по приоритету (от высокого к низкому)
        Comparator<Task> priorityComparator = (t1, t2) -> t2.getPriority().compareTo(t1.getPriority());

        List<Task> sortedTasks = sortingStrategy.sort(tasks, priorityComparator);

        // Проверяем, что задачи отсортированы по приоритету в обратном порядке
        assertEquals(4, sortedTasks.size());
        assertEquals(Priority.CRITICAL, sortedTasks.get(0).getPriority());
        assertEquals(Priority.HIGH, sortedTasks.get(1).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(3).getPriority());
    }

    @Test
    void mergeSort_ShouldSortByStatus() {
        // Создаем компаратор для сортировки по статусу
        Comparator<Task> statusComparator = Comparator.comparing(Task::getStatus);

        List<Task> sortedTasks = sortingStrategy.sort(tasks, statusComparator);

        // Проверяем, что задачи отсортированы по статусу
        assertEquals(4, sortedTasks.size());

        // Ожидаемый порядок: TODO, TODO, IN_PROGRESS, DONE
        assertEquals(TaskStatus.TODO, sortedTasks.get(0).getStatus());
        assertEquals(TaskStatus.TODO, sortedTasks.get(1).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, sortedTasks.get(2).getStatus());
        assertEquals(TaskStatus.DONE, sortedTasks.get(3).getStatus());
    }

    @Test
    void mergeSort_ShouldSortByCreationDate() {
        // Создаем компаратор для сортировки по дате создания
        Comparator<Task> dateComparator = Comparator.comparing(Task::getCreatedAt);

        List<Task> sortedTasks = sortingStrategy.sort(tasks, dateComparator);

        // Проверяем, что задачи отсортированы по дате создания
        assertTrue(sortedTasks.get(0).getCreatedAt().isBefore(sortedTasks.get(1).getCreatedAt()));
        assertTrue(sortedTasks.get(1).getCreatedAt().isBefore(sortedTasks.get(2).getCreatedAt()));
        assertTrue(sortedTasks.get(2).getCreatedAt().isBefore(sortedTasks.get(3).getCreatedAt()));
    }

    @Test
    void mergeSort_ShouldSortByMultipleFields() {
        // Создаем компаратор для сортировки по нескольким полям одновременно
        Comparator<Task> multiComparator = Comparator
                .comparing(Task::getStatus)
                .thenComparing(Task::getPriority)
                .thenComparing(Task::getCreatedAt);

        List<Task> sortedTasks = sortingStrategy.sort(tasks, multiComparator);

        // Проверяем первую группу (TODO)
        assertEquals(TaskStatus.TODO, sortedTasks.get(0).getStatus());
        assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());

        // Проверяем вторую группу (TODO)
        assertEquals(TaskStatus.TODO, sortedTasks.get(1).getStatus());
        assertEquals(Priority.CRITICAL, sortedTasks.get(1).getPriority());

        // Проверяем третью группу (IN_PROGRESS)
        assertEquals(TaskStatus.IN_PROGRESS, sortedTasks.get(2).getStatus());
        assertEquals(Priority.MEDIUM, sortedTasks.get(2).getPriority());

        // Проверяем четвертую группу (DONE)
        assertEquals(TaskStatus.DONE, sortedTasks.get(3).getStatus());
        assertEquals(Priority.LOW, sortedTasks.get(3).getPriority());
    }

    @Test
    void mergeSort_ShouldHandleEmptyList() {
        // Обработка пустого списка
        List<Task> emptyList = new ArrayList<>();

        List<Task> sortedTasks = sortingStrategy.sort(emptyList, Comparator.comparing(Task::getId));

        assertTrue(sortedTasks.isEmpty());
    }

    @Test
    void mergeSort_ShouldHandleSingleElement() {
        // Обработка списка с одним элементом
        List<Task> singleTaskList = new ArrayList<>();
        singleTaskList.add(tasks.get(0));

        List<Task> sortedTasks = sortingStrategy.sort(singleTaskList, Comparator.comparing(Task::getId));

        assertEquals(1, sortedTasks.size());
        assertEquals(tasks.get(0).getId(), sortedTasks.get(0).getId());
    }

    @Test
    void mergeSort_ShouldSortByTitle() {
        // Создаем компаратор для сортировки по названию задачи
        Comparator<Task> titleComparator = Comparator.comparing(Task::getTitle);

        List<Task> sortedTasks = sortingStrategy.sort(tasks, titleComparator);

        // Проверяем алфавитный порядок
        assertEquals("Task 1", sortedTasks.get(0).getTitle());
        assertEquals("Task 2", sortedTasks.get(1).getTitle());
        assertEquals("Task 3", sortedTasks.get(2).getTitle());
        assertEquals("Task 4", sortedTasks.get(3).getTitle());
    }

    @Test
    void mergeSort_ShouldSortByCreator() {
        // Создаем компаратор для сортировки по создателю задачи
        Comparator<Task> creatorComparator = Comparator
                .comparing(task -> task.getCreator().getUsername());

        List<Task> sortedTasks = sortingStrategy.sort(tasks, creatorComparator);

        // Проверяем, что все задачи созданы одним пользователем
        for (Task task : sortedTasks) {
            assertEquals("testuser", task.getCreator().getUsername());
        }
    }

    @Test
    void mergeSort_ShouldHandleNullComparator() {
        // Обработка nill-компаратора
        assertThrows(NullPointerException.class, () -> {
            sortingStrategy.sort(tasks, null);
        });
    }

    @Test
    void mergeSort_ShouldHandleLargeDataset() {
        // Проверка производительности при большом наборе данных
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
        List<Task> sortedTasks = sortingStrategy.sort(largeTasks, comparator);

        // Проверяем, что список отсортирован
        for (int i = 1; i < sortedTasks.size(); i++) {
            assertTrue(sortedTasks.get(i-1).getCreatedAt().isBefore(sortedTasks.get(i).getCreatedAt()));
        }
    }

    @Test
    void mergeSort_ShouldPreserveOriginalList() {
        // Проверка на неизменность исходного списка
        List<Task> originalTasks = new ArrayList<>(tasks);

        Comparator<Task> comparator = Comparator.comparing(Task::getPriority);
        sortingStrategy.sort(tasks, comparator);

        // Проверяем, что исходный список не изменился
        for (int i = 0; i < tasks.size(); i++) {
            assertEquals(originalTasks.get(i).getId(), tasks.get(i).getId());
            assertEquals(originalTasks.get(i).getPriority(), tasks.get(i).getPriority());
            assertEquals(originalTasks.get(i).getStatus(), tasks.get(i).getStatus());
        }
    }
}