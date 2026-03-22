package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Project;
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

class BubblesSortEvenIdsOnlyTest {

    private List<Task> tasks;
    private User testUser;
    private Project testProject;

    @BeforeEach
    void setUp() {
        // Создаем тестового пользователя
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Создаем тестовый проект
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");

        // Создаем тестовые задачи
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
    void bubbleSortEvenIds_ShouldSortEvenIdsOnly_WhenMixedIds() {
        // Задачи с четными ID: [4, 2, 6] - должны быть отсортированы как [2, 4, 6]
        List<Task> mixedTasks = new ArrayList<>();

        Task t1 = new Task(); t1.setId(4L); t1.setTitle("Task 4");
        Task t2 = new Task(); t2.setId(2L); t2.setTitle("Task 2");
        Task t3 = new Task(); t3.setId(6L); t3.setTitle("Task 6");
        Task t4 = new Task(); t4.setId(1L); t4.setTitle("Task 1");
        Task t5 = new Task(); t5.setId(3L); t5.setTitle("Task 3");
        Task t6 = new Task(); t6.setId(5L); t6.setTitle("Task 5");

        mixedTasks.addAll(Arrays.asList(t1, t2, t3, t4, t5, t6));

        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        List<Task> result = strategy.sort(mixedTasks, Comparator.comparing(Task::getId));

        // Проверяем размер списка
        assertEquals(6, result.size());

        // Проверяем, что четные задачи отсортированы по возрастанию
        assertEquals(2, result.get(0).getId()); // Наименьший четный ID на позиции 0
        assertEquals(4, result.get(1).getId()); // Средний четный ID на позиции 1
        assertEquals(6, result.get(2).getId()); // Наибольший четный ID на позиции 2

        // Проверяем, что нечетные задачи остались на своих местах
        assertEquals(1, result.get(3).getId()); // ID 1 на позиции 3
        assertEquals(3, result.get(4).getId()); // ID 3 на позиции 4
        assertEquals(5, result.get(5).getId()); // ID 5 на позиции 5

        // Дополнительно проверяем, что оригинальный список не изменился
        assertEquals(4, mixedTasks.get(0).getId()); // Оригинал должен остаться неизменным
    }

    @Test
    void bubbleSortEvenIds_ShouldSortEvenIds_WhenOnlyEvenIds() {
        // Создаем неотсортированный список четных ID
        List<Task> evenTasks = new ArrayList<>();

        Task task4 = new Task(); task4.setId(4L); task4.setTitle("Task 4");
        Task task2 = new Task(); task2.setId(2L); task2.setTitle("Task 2");
        Task task8 = new Task(); task8.setId(8L); task8.setTitle("Task 8");
        Task task6 = new Task(); task6.setId(6L); task6.setTitle("Task 6");

        evenTasks.addAll(Arrays.asList(task4, task2, task8, task6));

        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        List<Task> result = strategy.sort(evenTasks, Comparator.comparing(Task::getId));

        // Проверяем сортировку по возрастанию ID
        assertEquals(2, result.get(0).getId());
        assertEquals(4, result.get(1).getId());
        assertEquals(6, result.get(2).getId());
        assertEquals(8, result.get(3).getId());
    }

    @Test
    void bubbleSortEvenIds_ShouldReturnEmptyList_WhenEmptyList() {
        List<Task> emptyList = new ArrayList<>();
        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        List<Task> result = strategy.sort(emptyList, Comparator.comparing(Task::getId));

        assertTrue(result.isEmpty());
    }

    @Test
    void bubbleSortEvenIds_ShouldReturnEmptyList_WhenNullInput() {
        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        List<Task> result = strategy.sort(null, Comparator.comparing(Task::getId));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void bubbleSortEvenIds_ShouldNotModifyOriginalList() {
        List<Task> originalList = new ArrayList<>(tasks);
        List<Task> originalCopy = new ArrayList<>(tasks);

        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        strategy.sort(originalList, Comparator.comparing(Task::getId));

        // Оригинальный список не должен измениться
        assertEquals(originalCopy.size(), originalList.size());
        for (int i = 0; i < originalCopy.size(); i++) {
            assertSame(originalCopy.get(i), originalList.get(i));
        }
    }

    @Test
    void bubbleSortEvenIds_ShouldPreserveTaskProperties() {
        List<Task> testTasks = new ArrayList<>();

        Task highPriorityTask = new Task();
        highPriorityTask.setId(6L);
        highPriorityTask.setTitle("High Priority Task");
        highPriorityTask.setPriority(Priority.HIGH);
        highPriorityTask.setStatus(TaskStatus.IN_PROGRESS);
        highPriorityTask.setCreator(testUser);
        highPriorityTask.setProject(testProject);

        Task lowPriorityTask = new Task();
        lowPriorityTask.setId(4L);
        lowPriorityTask.setTitle("Low Priority Task");
        lowPriorityTask.setPriority(Priority.LOW);
        lowPriorityTask.setStatus(TaskStatus.TODO);
        lowPriorityTask.setCreator(testUser);
        lowPriorityTask.setProject(testProject);

        testTasks.addAll(Arrays.asList(highPriorityTask, lowPriorityTask));

        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        List<Task> result = strategy.sort(testTasks, Comparator.comparing(Task::getId));

        // Проверяем, что свойства задач сохранились после сортировки
        Task firstTask = result.get(0);
        Task secondTask = result.get(1);

        assertEquals(4, firstTask.getId());
        assertEquals("Low Priority Task", firstTask.getTitle());
        assertEquals(Priority.LOW, firstTask.getPriority());
        assertEquals(TaskStatus.TODO, firstTask.getStatus());
        assertSame(testUser, firstTask.getCreator());
        assertSame(testProject, firstTask.getProject());

        assertEquals(6, secondTask.getId());
        assertEquals("High Priority Task", secondTask.getTitle());
        assertEquals(Priority.HIGH, secondTask.getPriority());
        assertEquals(TaskStatus.IN_PROGRESS, secondTask.getStatus());
    }

    @Test
    void bubbleSortEvenIds_ShouldSortEvenIds_WhenMultipleSameIds() {
        List<Task> tasksWithDuplicates = new ArrayList<>();

        Task task1 = new Task(); task1.setId(4L); task1.setTitle("First 4");
        Task task2 = new Task(); task2.setId(2L); task2.setTitle("First 2");
        Task task3 = new Task(); task3.setId(4L); task3.setTitle("Second 4");
        Task task4 = new Task(); task4.setId(2L); task4.setTitle("Second 2");

        tasksWithDuplicates.addAll(Arrays.asList(task1, task2, task3, task4));

        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        List<Task> result = strategy.sort(tasksWithDuplicates, Comparator.comparing(Task::getId));

        // Проверяем сортировку по ID, задачи с одинаковыми ID сохраняют относительный порядок
        assertEquals(2, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(4, result.get(2).getId());
        assertEquals(4, result.get(3).getId());

        // Проверяем сохранение порядка для одинаковых ID
        assertEquals("First 2", result.get(0).getTitle());
        assertEquals("Second 2", result.get(1).getTitle());
        assertEquals("First 4", result.get(2).getTitle());
        assertEquals("Second 4", result.get(3).getTitle());
    }

    @Test
    void bubbleSortEvenIds_ShouldSortEvenIds_WhenSingleElement() {
        List<Task> singleTaskList = new ArrayList<>();
        Task task = new Task(); task.setId(2L); task.setTitle("Single Task");
        singleTaskList.add(task);

        BubblesSortEvenIdsOnly strategy = new BubblesSortEvenIdsOnly();
        List<Task> result = strategy.sort(singleTaskList, Comparator.comparing(Task::getId));

        assertEquals(1, result.size());
        assertSame(task, result.get(0));
    }
}
