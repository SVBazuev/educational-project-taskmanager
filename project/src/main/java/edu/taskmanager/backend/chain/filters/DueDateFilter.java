package edu.taskmanager.backend.chain.filters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;

/**
 * Фильтр, пропускающий задачи, срок выполнения которых попадает в указанный диапазон дат.
 * Диапазон включает начальную и конечную даты.
 */

public class DueDateFilter implements TaskFilter, FilterFactory {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private TaskFilter next;

    public DueDateFilter() {
        this.startDate = null;
        this.endDate = null;
    }

    public DueDateFilter(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean filter(Task task) {

        LocalDateTime dueDate = task.getDueDate();

        // Проверяем, попадает ли срок выполнения в диапазон
        boolean inRange = true;

        if (startDate != null) {
            inRange = inRange && !dueDate.isBefore(startDate);
        }

        if (endDate != null) {
            inRange = inRange && !dueDate.isAfter(endDate);
        }

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

    @Override
    public TaskFilter create(Map.Entry<String, String> entry)
    throws IllegalArgumentException {
        String key = entry.getKey();
        String value = entry.getValue();
        LocalDateTime date = LocalDateTime.parse(
            value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
        TaskFilter filter = null;

        switch (key) {
            case "duestartdate":
                filter = new DueDateFilter(date, null);
                break;
            case "dueenddate":
                filter = new DueDateFilter(null, date);
                break;
            default:
                System.out.printf(
                    "Ops, здесь ожидалось: %s или %s\n%s: \"%s\"",
                    "\"duestartdate\"",
                    "\"dueenddate\"",
                    "А было передано",
                    key
                );
                break;
        }

        return filter;
    };
}
