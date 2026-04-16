package edu.taskmanager.backend.chain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.DateType;

public class DateRangeFilter implements TaskFilter, FilterFactory{

    private final DateType dateType;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private TaskFilter next;

    public DateRangeFilter() {
        this.dateType = null;
        this.startDate = null;
        this.endDate = null;
    }

    public DateRangeFilter(
    DateType dateType, LocalDateTime startDate, LocalDateTime endDate) {
        if (dateType == null) {
            throw new IllegalArgumentException("DateType cannot be null");
        }
        this.dateType = dateType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean filter(Task task) {
        LocalDateTime date = dateType.extract(task);

        if (date == null) {
            return false;
        }

        if (startDate != null && date.isBefore(startDate)) {
            return false;
        }

        if (endDate != null && date.isAfter(endDate)) {
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

    public DateType getDateType() {
        return dateType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return String.format("DateRangeFilter{%s: %s - %s}",
            dateType,
            startDate != null ? startDate : "∞",
            endDate != null ? endDate : "∞");
    }

    @Override
    public TaskFilter createFilter(Map.Entry<String, String> entry) {
        String key = entry.getKey();
        String value = entry.getValue();
        LocalDateTime date = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        TaskFilter filter;
        // Определяем тип даты по ключу
        DateType type = DateType.fromKey(key);

        if (type == null) {
            return null;
        }
        if (key.contains("start")){
            filter = new DateRangeFilter(type, date, null);
        }
        else{
            filter = new DateRangeFilter(type, null, date);
        }
        return filter;
    }
}
