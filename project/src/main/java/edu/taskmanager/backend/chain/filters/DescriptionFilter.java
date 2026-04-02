package edu.taskmanager.backend.chain.filters;


import java.util.Map;
import java.util.regex.Pattern;

import edu.taskmanager.backend.chain.FilterFactory;
import edu.taskmanager.backend.chain.TaskFilter;
import edu.taskmanager.backend.model.Task;


public class DescriptionFilter implements TaskFilter, FilterFactory {
    private final String regex;
    private TaskFilter next;

    public DescriptionFilter() {
        this.regex = null;
    }

    public DescriptionFilter(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean filter(Task task) {
        boolean descriptionMatches = Pattern.matches(regex, task.getDescription());

        if (!descriptionMatches) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter create(Map.Entry<String, String> criteria)
    throws IllegalArgumentException  {
        return new DescriptionFilter(criteria.getValue());
    }
}
