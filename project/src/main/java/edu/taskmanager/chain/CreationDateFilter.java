package edu.taskmanager.chain;


import edu.taskmanager.model.Task;
import java.time.LocalDateTime;


/**
 * Фильтр, пропускающий задачи по дате создания задачи.
 * Поля from и to представляют собой временные метки LocalDateTime,
 * которые определяют диапазон дат для фильтрации задач.
 * 1. from - начальная дата диапазона, задачи, созданные раньше этой даты будут отфильтрованы.
 * 2. to - конечная дата диапазона, задачи, созданные раньше этой даты будут отфильтрованы.
 */
public class CreationDateFilter implements TaskFilter {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private TaskFilter next;

    public CreationDateFilter(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean filter(Task task) {
        LocalDateTime creationDate = task.getCreatedAt();

        if (from != null && creationDate.isBefore(from)) { return false; }

        if (to != null && creationDate.isAfter(to)) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}