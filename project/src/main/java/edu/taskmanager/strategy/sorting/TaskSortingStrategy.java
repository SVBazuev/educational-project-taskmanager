package edu.taskmanager.strategy.sorting;

import edu.taskmanager.model.Task;

import java.util.List;

public interface TaskSortingStrategy {
    public List<Task> sort(List<Task> tasks);
}
