package edu.taskmanager.backend.chain;


import edu.taskmanager.backend.model.Task;


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
