package edu.taskmanager.backend.chain;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UpdateDateFilterTest {

    private UpdateDateFilter filter;
    private Task taskBefore;
    private Task taskWithin;
    private Task taskAfter;
    private Task taskNullDate;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2026, 3, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 31, 23, 59);

        filter = new UpdateDateFilter(start, end);

        // Создаем тестовые задачи
        taskBefore = new Task();
        taskBefore.setUpdatedAt(LocalDateTime.of(2026, 2, 28, 0, 0));

        taskWithin = new Task();
        taskWithin.setUpdatedAt(LocalDateTime.of(2026, 3, 15, 12, 0));

        taskAfter = new Task();
        taskAfter.setUpdatedAt(LocalDateTime.of(2026, 4, 1, 0, 0));

        taskNullDate = new Task();
        taskNullDate.setUpdatedAt(null);
    }

    @Test
    void testFilter_TaskBeforeRange() {
        // Проверяем, что задача, обновленная до начала диапазона, не проходит фильтр
        assertFalse(filter.filter(taskBefore), "Задача должна быть отфильтрована");
    }

    @Test
    void testFilter_TaskWithinRange() {
        // Проверяем, что задача, обновленная в пределах диапазона, проходит фильтр
        assertTrue(filter.filter(taskWithin), "Задача должна пройти фильтр");
    }

    @Test
    void testFilter_TaskAfterRange() {
        // Проверяем, что задача, обновленная после конца диапазона, не проходит фильтр
        assertFalse(filter.filter(taskAfter), "Задача должна быть отфильтрована");
    }

    @Test
    void testFilter_NullUpdateDate() {
        // Проверяем поведение фильтра при null значении даты обновления задачи
        assertFalse(filter.filter(taskNullDate), "Задача с null датой должна быть отфильтрована");
    }

    @Test
    void testFilter_StartDateNull() {
        /* Проверяем работу фильтра при null значении startDate
         * В этом случае должны проходить все задачи, обновленные до endDate
         */
        UpdateDateFilter filterWithNullStart = new UpdateDateFilter(null, LocalDateTime.of(2026, 3, 31, 23, 59));
        assertTrue(filterWithNullStart.filter(taskBefore), "Задача должна пройти фильтр");
    }

    @Test
    void testFilter_EndDateNull() {
        /* Проверяем работу фильтра при null значении endDate
         * В этом случае должны проходить все задачи, обновленные после startDate
         */
        UpdateDateFilter filterWithNullEnd = new UpdateDateFilter(LocalDateTime.of(2026, 3, 1, 0, 0), null);
        assertTrue(filterWithNullEnd.filter(taskAfter), "Задача должна пройти фильтр");
    }

    @Test
    void testFilter_BothDatesNull() {
        /* Проверяем работу фильтра при null значениях обеих дат
         * В этом случае все задачи должны проходить фильтр
         */
        UpdateDateFilter filterWithNullDates = new UpdateDateFilter(null, null);
        assertTrue(filterWithNullDates.filter(taskBefore), "Задача должна пройти фильтр");
        assertTrue(filterWithNullDates.filter(taskWithin), "Задача должна пройти фильтр");
        assertTrue(filterWithNullDates.filter(taskAfter), "Задача должна пройти фильтр");
    }

    @Test
    void testChainFilter() {
        /* Проверяем работу фильтра в цепочке с другим фильтром
         * создаем следующий фильтр, который проверяет приоритет задачи
         */
        TaskFilter nextFilter = new TaskFilter() {
            @Override
            public boolean filter(Task task) {
                return task.getPriority() != null && task.getPriority().equals(Priority.HIGH);
            }

            public void setNext(TaskFilter next) {
            }
        };

        filter.setNext(nextFilter);

        // Создаем тестовые задачи для проверки цепочки фильтров
        Task taskInRangeHighPriority = new Task();
        taskInRangeHighPriority.setUpdatedAt(LocalDateTime.of(2026, 3, 15, 12, 0));
        taskInRangeHighPriority.setPriority(Priority.HIGH);

        Task taskInRangeLowPriority = new Task();
        taskInRangeLowPriority.setUpdatedAt(LocalDateTime.of(2026, 3, 15, 12, 0));
        taskInRangeLowPriority.setPriority(Priority.LOW);

        Task taskOutOfRangeHighPriority = new Task();
        taskOutOfRangeHighPriority.setUpdatedAt(LocalDateTime.of(2026, 2, 28, 0, 0));
        taskOutOfRangeHighPriority.setPriority(Priority.HIGH);

        // Проверяем, что задача проходит оба фильтра:
        // 1. Находится в диапазоне дат обновления
        // 2. Имеет высокий приоритет
        assertTrue(filter.filter(taskInRangeHighPriority),
                "Задача должна пройти оба фильтра: по дате и по приоритету");

        // Проверяем, что задача не проходит второй фильтр (низкий приоритет)
        assertFalse(filter.filter(taskInRangeLowPriority),
                "Задача должна быть отфильтрована по приоритету");

        // Проверяем, что задача не проходит первый фильтр (дата вне диапазона)
        assertFalse(filter.filter(taskOutOfRangeHighPriority),
                "Задача должна быть отфильтрована по дате");
    }
}