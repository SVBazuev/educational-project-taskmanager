package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

/**
 * Фильтр, пропускающий задачи, срок выполнения которых истекает на текущей неделе.
 * Неделя считается с понедельника по воскресенье.
 */
public class DueDateThisWeekFilter implements TaskFilter {
    private TaskFilter next;

    public DueDateThisWeekFilter() {
    }

    @Override
    public boolean filter(Task task) {
        // Проверяем, что есть срок выполнения
        if (task.getDueDate() == null) {
            return false;
        }

        LocalDateTime dueDate = task.getDueDate();
        LocalDateTime now = LocalDateTime.now();

        // Начало текущей недели (понедельник, 00:00)
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .toLocalDate()
                .atStartOfDay();

        // Конец текущей недели (воскресенье, 23:59:59)
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atTime(23, 59, 59, 999999999);

        // Проверяем, попадает ли срок выполнения в интервал текущей недели
        boolean dueThisWeek = !dueDate.isBefore(startOfWeek) &&
                !dueDate.isAfter(endOfWeek);

        if (!dueThisWeek) {
            return false;
        }

        if (next != null) {
            return next.filter(task);
        }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}