package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import java.time.LocalDateTime;

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
         if (from != null && updateDate.isBefore(from)) {
            return false;
        }
        if (to != null && updateDate.isAfter(to)) {
            return false;
        }
        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}

// if (from != null && updateDate.isBefore(from)) {
//            return false;
//        }
//        if (to != null && updateDate.isAfter(to)) {
//            return false;
//        }
//        return true;