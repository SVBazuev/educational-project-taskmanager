package edu.taskmanager.chain;


import edu.taskmanager.model.Task;
import java.time.LocalDateTime;


/**
 * Фильтр, пропускающий задачи по дате последнего обновления задачи.
 * Поля startDate и endDate представляют собой временные метки LocalDateTime,
 * которые определяют диапазон дат для фильтрации задач.
 * 1. startDate - начальная дата диапазона. Задачи, обновленные раньше этой даты будут отфильтрованы.
 * 2. endDate - конечная дата диапазона. Задачи, обновленные позже этой даты будут отфильтрованы.
 */
public class UpdateDateFilter implements TaskFilter {
    private final LocalDateTime upStartDate;
    private final LocalDateTime upEndDate;
    private TaskFilter next;

    public UpdateDateFilter(LocalDateTime upStartDate, LocalDateTime upEndDate) {
        this.upStartDate = upStartDate;
        this.upEndDate = upEndDate;
    }

    @Override
    public boolean filter(Task task) {
      LocalDateTime updateDate = task.getUpdatedAt();

      if (upStartDate != null && updateDate.isBefore(upStartDate)) { return false; }

      if (upEndDate != null && updateDate.isAfter(upEndDate)) { return false; }

      if (next != null) { return next.filter(task); }

      return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}