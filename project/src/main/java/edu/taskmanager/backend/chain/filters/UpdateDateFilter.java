package edu.taskmanager.backend.chain.filters;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;


/**
 * Фильтр, пропускающий задачи по дате последнего обновления задачи.
 * Поля startDate и endDate представляют собой временные метки LocalDateTime,
 * которые определяют диапазон дат для фильтрации задач.
 * 1. startDate - начальная дата диапазона. Задачи, обновленные раньше этой даты будут отфильтрованы.
 * 2. endDate - конечная дата диапазона. Задачи, обновленные позже этой даты будут отфильтрованы.
 */
public class UpdateDateFilter implements TaskFilter, FilterFactory {
    private final LocalDateTime upStartDate;
    private final LocalDateTime upEndDate;
    private TaskFilter next;

    public UpdateDateFilter() {
        this.upStartDate = null;
        this.upEndDate = null;
    }

    public UpdateDateFilter(LocalDateTime upStartDate, LocalDateTime upEndDate) {
        this.upStartDate = upStartDate;
        this.upEndDate = upEndDate;
    }

    @Override
    public boolean filter(Task task) {
        LocalDateTime updateDate = task.getUpdatedAt();

        if (updateDate == null) { return false; }

        if (upStartDate != null && updateDate.isBefore(upStartDate)) { return false; }

        if (upEndDate != null && updateDate.isAfter(upEndDate)) { return false; }

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
            case "upstartdate":
                filter = new UpdateDateFilter(date, null);
                break;
            case "upenddate":
                filter = new UpdateDateFilter(null, date);
                break;
            default:
                System.out.printf(
                    "Ops, здесь ожидалось: %s или %s\n%s: \"%s\"",
                    "\"upstartdate\"",
                    "\"upenddate\"",
                    "А было передано",
                    key
                );
                break;
        }

        return filter;
    };
}
