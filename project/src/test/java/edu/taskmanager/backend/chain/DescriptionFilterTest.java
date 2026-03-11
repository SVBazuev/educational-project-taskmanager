package edu.taskmanager.backend.chain;

import edu.taskmanager.backend.model.*;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionFilterTest {

    private Task task1;
    private Task task2;
    private Task task3;
    private DescriptionFilter filter;

    @BeforeEach
    void setUp() {
        // Создаем тестовые задачи
        task1 = createTask(1L, "Task 1", "Создание базовых классов модели и in-memory репозитория",
                Priority.HIGH, TaskStatus.TODO);
        task2 = createTask(2L, "Task 2", "создание базовых классов модели и in-memory репозитория",
                Priority.MEDIUM, TaskStatus.IN_PROGRESS);
        task3 = createTask(3L, "Task 3", "Написание тестов для сервисного слоя",
                Priority.LOW, TaskStatus.DONE);
    }

    @Test
    void filter_shouldReturnTrueWhenDescriptionMatchesAndNoNextFilter() {
        // Given
        filter = new DescriptionFilter("создание базовых классов модели и in-memory репозитория");

        // When & Then
        assertTrue(filter.filter(task1));
    }

    @Test
    void filter_shouldReturnFalseWhenDescriptionDoesNotMatch() {
        // Given
        filter = new DescriptionFilter("несуществующее описание");

        // When & Then
        assertFalse(filter.filter(task1));
    }

    @Test
    void filter_shouldChainFiltersAndReturnTrueWhenAllFiltersPass() {
        // Given
        DescriptionFilter firstFilter = new DescriptionFilter("создание базовых классов модели и in-memory репозитория");
        DescriptionFilter secondFilter = new DescriptionFilter("создание базовых классов модели и in-memory репозитория");
        firstFilter.setNext(secondFilter);

        // When & Then
        assertTrue(firstFilter.filter(task1));
    }

    @Test
    void filter_shouldChainFiltersAndReturnFalseWhenSecondFilterFails() {
        // Given
        DescriptionFilter firstFilter = new DescriptionFilter("создание базовых классов модели и in-memory репозитория");
        DescriptionFilter secondFilter = new DescriptionFilter("неподходящее описание");
        firstFilter.setNext(secondFilter);

        // When & Then
        assertFalse(firstFilter.filter(task1));
    }

    @Test
    void filter_shouldHandleNullDescription() {
        // Given
        Task taskWithNullDescription = createTask(4L, "Task 4", null,
                Priority.LOW, TaskStatus.TODO);
        filter = new DescriptionFilter("тестовое описание");

        // When & Then
        assertThrows(NullPointerException.class, () -> filter.filter(taskWithNullDescription));
    }

    @Test
    void filter_shouldHandleEmptyDescription() {
        // Given
        Task taskWithEmptyDescription = createTask(5L, "Task 5", "",
                Priority.LOW, TaskStatus.TODO);

        // When фильтр с пустой строкой
        filter = new DescriptionFilter("");
        assertTrue(filter.filter(taskWithEmptyDescription));

        // When фильтр с непустой строкой
        filter = new DescriptionFilter("описание");
        assertFalse(filter.filter(taskWithEmptyDescription));
    }

    @Test
    void setNext_shouldSetNextFilterCorrectly() {
        // Given
        DescriptionFilter filter1 = new DescriptionFilter("описание 1");
        DescriptionFilter filter2 = new DescriptionFilter("описание 2");

        // When
        filter1.setNext(filter2);

        // Then
        // Проверяем через цепочку фильтрации, что второй фильтр действительно установлен
        Task taskWithDescription2 = createTask(6L, "Task 6", "описание 2",
                Priority.HIGH, TaskStatus.TODO);

        // Первый фильтр не должен пропустить, т.к. описание не совпадает с "описание 1"
        assertFalse(filter1.filter(taskWithDescription2));

        // Проверяем, что фильтр работает с установленным next
        DescriptionFilter filter3 = new DescriptionFilter("описание 1");
        filter3.setNext(filter2);

        // Должно быть false, т.к. описание совпадает с первым фильтром,
        // но второй фильтр вернет false (описание не совпадает с "описание 2")
        assertFalse(filter3.filter(taskWithDescription2));
    }

    private Task createTask(Long id, String title, String description, Priority priority, TaskStatus status) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(LocalDateTime.now().plusDays(7));
        task.setPriority(priority);
        task.setStatus(status);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        // Создаем пользователя
        User creator = new User();
        creator.setId(7L);
        creator.setUsername("admin");
        creator.setRole(User.Role.ADMIN);
        task.setCreator(creator);

        // Создаем проект
        Project project = new Project();
        project.setId(1L);
        project.setName("Task Manager MVP");
        project.setDescription("Проект для управления задачами");
        project.setTasks(new ArrayList<>());
        task.setProject(project);

        // Инициализируем коллекции
        task.setTags(new ArrayList<>());
        task.setSubtasks(new ArrayList<>());
        task.setContractors(new ArrayList<>());

        return task;
    }
}