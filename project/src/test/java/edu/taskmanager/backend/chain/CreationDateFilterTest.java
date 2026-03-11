package edu.taskmanager.backend.chain;

import edu.taskmanager.backend.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreationDateFilterTest {

    private CreationDateFilter filter;
    private Task taskBefore;
    private Task taskWithin;
    private Task taskAfter;
    private Task taskNullDate;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 31, 23, 59);

        filter = new CreationDateFilter(start, end);

        // Создаем тестовые задачи
        taskBefore = new Task();
        taskBefore.setCreatedAt(LocalDateTime.of(2026, 2, 28, 0, 0));
        assertNotNull(taskBefore.getCreatedAt());

        taskWithin = new Task();
        taskWithin.setCreatedAt(LocalDateTime.of(2026, 3, 15, 12, 0));
        assertNotNull(taskWithin.getCreatedAt());

        taskAfter = new Task();
        taskAfter.setCreatedAt(LocalDateTime.of(2026, 4, 1, 0, 0));
        assertNotNull(taskAfter.getCreatedAt());

        taskNullDate = new Task();
        taskNullDate.setCreatedAt(null);
        assertNull(taskNullDate.getCreatedAt());
    }

    @Test
    void testFilter_TaskBeforeRange() {
        // Проверяем, что задача, созданная до начала диапазона, не проходит фильтр
        assertFalse(filter.filter(taskBefore), "Задача должна быть отфильтрована");
    }

    @Test
    void testFilter_TaskWithinRange() {
        // Проверяем, что задача, созданная в пределах диапазона, проходит фильтр
        assertTrue(filter.filter(taskWithin), "Задача должна пройти фильтр");
    }

    @Test
    void testFilter_TaskAfterRange() {
        // Проверяем, что задача, созданная после конца диапазона, не проходит фильтр
        assertFalse(filter.filter(taskAfter),"Задача должна быть отфильтрована");
    }

    @Test
    void testFilter_NullCreationDate() {
        // Создаем задачу с валидной датой, но потом устанавливаем null
        Task task = new Task();
        task.setCreatedAt(LocalDateTime.now());
        task.setCreatedAt(null); // Устанавливаем null

        assertFalse(filter.filter(task), "Задача с null датой должна быть отфильтрована");
    }

    @Test
    void testFilter_StartDateNull() {
        /* Проверяем работу фильтра при null значении startDate
         * В этом случае должны проходить все задачи, созданные до endDate
         */
        CreationDateFilter filterWithNullStart = new CreationDateFilter(null, LocalDateTime.of(2026, 3, 31, 23, 59));
        assertTrue(filterWithNullStart.filter(taskBefore), "Задача должна пройти фильтр");
    }

    @Test
    void testFilter_EndDateNull() {
        /*Проверяем работу фильтра при null значении endDate
         * В этом случае должны проходить все задачи, созданные после startDate
         */
        CreationDateFilter filterWithNullEnd = new CreationDateFilter(LocalDateTime.of(2026, 3, 1, 0, 0), null);
        assertTrue(filterWithNullEnd.filter(taskAfter), "Задача должна пройти фильтр");
    }

    @Test
    void testFilter_BothDatesNull() {
        /* Проверяем работу фильтра при null значениях обеих дат
         * В этом случае все задачи должны проходить фильтр
         */
        CreationDateFilter filterWithNullDates = new CreationDateFilter(null, null);
        assertTrue(filterWithNullDates.filter(taskBefore), "Задача должна пройти фильтр");
        assertTrue(filterWithNullDates.filter(taskWithin), "Задача должна пройти фильтр");
        assertTrue(filterWithNullDates.filter(taskAfter), "Задача должна пройти фильтр");
    }

    @Test
    void testChainFilter() {
        /* Проверяем работу фильтра в цепочке с другим фильтром,
         * создаем следующий фильтр, который проверяет наличие слова "test" в названии
         */
        TaskFilter nextFilter = new TaskFilter() {
            @Override
            public boolean filter(Task task) {
                return task.getTitle().contains("test");
            }

            @Override
            public void setNext(TaskFilter next) {

            }
        };

        filter.setNext(nextFilter);

        // Создаем задачи для проверки
        Task taskInRangeWithTitle = new Task();
        taskInRangeWithTitle.setCreatedAt(LocalDateTime.of(2026, 3, 15, 12, 0));
        taskInRangeWithTitle.setTitle("test task");

        Task taskInRangeWithoutTitle = new Task();
        taskInRangeWithoutTitle.setCreatedAt(LocalDateTime.of(2026, 3, 15, 12, 0));
        taskInRangeWithoutTitle.setTitle("another task");

        assertTrue(filter.filter(taskInRangeWithTitle));
        assertFalse(filter.filter(taskInRangeWithoutTitle));
    }
}