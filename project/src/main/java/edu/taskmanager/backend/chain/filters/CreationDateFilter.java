package edu.taskmanager.backend.chain.filters;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;


/**
 * Фильтр, пропускающий задачи по дате создания задачи.
 * Поля startDate и endDate — временные метки LocalDateTime,
 * которые определяют диапазон дат для фильтрации задач.
 * 1. startDate - начальная дата диапазона.
 *    Задачи, созданные раньше этой даты будут отфильтрованы.
 * 2. endDate - конечная дата диапазона.
 *    Задачи, созданные позже этой даты будут отфильтрованы.
 */
public class CreationDateFilter implements TaskFilter, FilterFactory {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private TaskFilter next;

    public CreationDateFilter() {
        this.startDate = null;
        this.endDate = null;
    }

    public CreationDateFilter(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean filter(Task task) {
        LocalDateTime creationDate = task.getCreatedAt();

        if (creationDate == null) { return false; }

        if (startDate != null && creationDate.isBefore(startDate)) { return false; }

        if (endDate != null && creationDate.isAfter(endDate)) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter create(Map.Entry<String, String> criteria)
    throws IllegalArgumentException {
        String key = criteria.getKey();
        String value = criteria.getValue();
        LocalDateTime date = LocalDateTime.parse(
            value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
        TaskFilter filter = null;

        switch (key) {
            case "startdate":
                filter = new CreationDateFilter(date, null);
                break;
            case "enddate":
                filter = new CreationDateFilter(null, date);
                break;
            default:
                System.out.printf(
                    "Ops, здесь ожидалось: %s или %s\n%s: \"%s\"",
                    "\"startdate\"",
                    "\"enddate\"",
                    "А было передано",
                    key
                );
                break;
        }

        return filter;
    };
}
