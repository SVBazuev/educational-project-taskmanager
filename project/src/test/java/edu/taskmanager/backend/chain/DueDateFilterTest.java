package edu.taskmanager.backend.chain;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DueDateFilterTest {

    private Task task;
    private User testUser;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Создаем тестового пользователя
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Создаем тестовую задачу
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setCreator(testUser);
        task.setPriority(Priority.MEDIUM);
        task.setStatus(TaskStatus.TODO);

        // Текущее время для тестов
        now = LocalDateTime.now();
    }

    @Test
    void filter_ShouldReturnTrue_WhenDueDateIsWithinRange() {
        // Задача со сроком выполнения в середине диапазона
        LocalDateTime dueDate = now.plusDays(5);
        task.setDueDate(dueDate);

        DueDateFilter filter = new DueDateFilter(now.plusDays(1), now.plusDays(10));

        assertTrue(filter.filter(task));
    }

    @Test
    void filter_ShouldReturnTrue_WhenDueDateEqualsStartDate() {
        // Срок выполнения равен начальной дате
        LocalDateTime dueDate = now.plusDays(5);
        task.setDueDate(dueDate);

        DueDateFilter filter = new DueDateFilter(dueDate, now.plusDays(10));

        assertTrue(filter.filter(task));
    }

    @Test
    void filter_ShouldReturnTrue_WhenDueDateEqualsEndDate() {
        // Срок выполнения равен конечной дате
        LocalDateTime dueDate = now.plusDays(5);
        task.setDueDate(dueDate);

        DueDateFilter filter = new DueDateFilter(now.plusDays(1), dueDate);

        assertTrue(filter.filter(task));
    }

    @Test
    void filter_ShouldReturnFalse_WhenDueDateIsBeforeStartDate() {
        // Срок выполнения раньше начала диапазона
        task.setDueDate(now.plusDays(3));

        DueDateFilter filter = new DueDateFilter(now.plusDays(5), now.plusDays(10));

        assertFalse(filter.filter(task));
    }

    @Test
    void filter_ShouldReturnFalse_WhenDueDateIsAfterEndDate() {
        // Срок выполнения позже конца диапазона
        task.setDueDate(now.plusDays(15));

        DueDateFilter filter = new DueDateFilter(now.plusDays(1), now.plusDays(10));

        assertFalse(filter.filter(task));
    }

    @Test
    void filter_ShouldReturnTrue_WhenNoStartDate() {
        // Только конечная дата (начало не задано)
        task.setDueDate(now.plusDays(5));

        DueDateFilter filter = new DueDateFilter(null, now.plusDays(10));

        assertTrue(filter.filter(task));
    }

    @Test
    void filter_ShouldReturnTrue_WhenNoEndDate() {
        // Только начальная дата (конец не задан)
        task.setDueDate(now.plusDays(5));

        DueDateFilter filter = new DueDateFilter(now.plusDays(1), null);

        assertTrue(filter.filter(task));
    }

    @Test
    void filter_ShouldReturnTrue_WhenBothDatesNull() {
        // Диапазон не задан (должен пропускать все задачи)
        task.setDueDate(now.plusDays(5));

        DueDateFilter filter = new DueDateFilter(null, null);

        assertTrue(filter.filter(task));
    }
}