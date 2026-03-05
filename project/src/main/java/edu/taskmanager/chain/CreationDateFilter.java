package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import java.time.LocalDateTime;

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
        if (from != null && creationDate.isBefore(from)) {
            return false;
        }
        if (to != null && creationDate.isAfter(to)) {
            return false;
        }
        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}
