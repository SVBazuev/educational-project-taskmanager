package edu.taskmanager.chain;


import edu.taskmanager.model.Task;
import java.time.LocalDateTime;


/**
 * Фильтр, пропускающий задачи по дате обновления задачи.
 * Поля from и to представляют собой временные метки LocalDateTime,
 * которые определяют диапазон дат для фильтрации задач.
 * 1. from - начальная дата диапазона, задачи, созданные раньше этой даты будут отфильтрованы.
 * 2. to - конечная дата диапазона, задачи, созданные раньше этой даты будут отфильтрованы.
 */
public class UpdateDateFilter implements TaskFilter {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private TaskFilter next;

    public UpdateDateFilter(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean filter(Task task) {
      LocalDateTime updateDate = task.getUpdatedAt();

      if (from != null && updateDate.isBefore(from)) { return false; }

      if (to != null && updateDate.isAfter(to)) { return false; }

      if (next != null) { return next.filter(task); }

      return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}