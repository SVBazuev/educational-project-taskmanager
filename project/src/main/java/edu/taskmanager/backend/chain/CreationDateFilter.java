package edu.taskmanager.backend.chain;


import java.time.LocalDateTime;

import edu.taskmanager.backend.model.Task;


/**
 * Фильтр, пропускающий задачи по дате создания задачи.
 * Поля startDate и endDate представляют собой временные метки LocalDateTime,
 * которые определяют диапазон дат для фильтрации задач.
 * 1. startDate - начальная дата диапазона. Задачи, созданные раньше этой даты будут отфильтрованы.
 * 2. endDate - конечная дата диапазона. Задачи, созданные позже этой даты будут отфильтрованы.
 */
public class CreationDateFilter implements TaskFilter {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private TaskFilter next;

    public CreationDateFilter(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean filter(Task task) {
        LocalDateTime creationDate = task.getCreatedAt();

        if (startDate != null && creationDate.isBefore(startDate)) { return false; }

        if (endDate != null && creationDate.isAfter(endDate)) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}
