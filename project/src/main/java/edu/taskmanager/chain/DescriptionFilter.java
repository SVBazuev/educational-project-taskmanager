package edu.taskmanager.chain;

import edu.taskmanager.builder.TaskBuilder;
import edu.taskmanager.model.Task;
import edu.taskmanager.util.Priority;
import edu.taskmanager.util.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DescriptionFilter implements TaskFilter {
    private final String description;
    private TaskFilter next;

    public DescriptionFilter(String description) {
        this.description = description;
    }

    @Override
    public boolean filter(Task task) {
        boolean descriptionMatches = description.equals(task.getDescription());

        if (!descriptionMatches) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }
}

