package edu.taskmanager.chain;

import edu.taskmanager.model.Task;
import edu.taskmanager.util.TaskStatus;

import java.util.List;
import java.util.stream.Collectors;

public class StatusFilter implements TaskFilter {

    private final TaskStatus status;

    public StatusFilter(TaskStatus status) {
        this.status = status;
    }

    @Override
    public List<Task> apply(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

}
