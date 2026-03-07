package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import java.time.LocalDateTime;

/**
 * Фильтр, пропускающий задачи, срок выполнения которых попадает в указанный диапазон дат.
 * Диапазон включает начальную и конечную даты.
 */

public class DueDateRangeFilter implements TaskFilter {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private TaskFilter next;

    public DueDateRangeFilter(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean filter(Task task) {

        LocalDateTime dueDate = task.getDueDate();

        // Проверяем, попадает ли срок выполнения в диапазон
        boolean inRange = !dueDate.isBefore(startDate) && !dueDate.isAfter(endDate);

        if (!inRange) {
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