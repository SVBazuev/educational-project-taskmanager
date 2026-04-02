package edu.taskmanager.backend.chain;


import edu.taskmanager.backend.model.Task;

import java.util.Map;


public class DescriptionFilter implements TaskFilter, FilterFactory {
    private final String description;
    private TaskFilter next;

    public DescriptionFilter(String description) {
        this.description = description;
    }

    public DescriptionFilter() {
        this.description = null;
    }

    @Override
    public boolean filter(Task task) {
        boolean descriptionMatches = description.equals(task.getDescription().toLowerCase());

        if (!descriptionMatches) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter create(Map.Entry<String, String> value) throws IllegalArgumentException {
        String description = value.getValue().toLowerCase();
        return new DescriptionFilter(description);
    }
}
