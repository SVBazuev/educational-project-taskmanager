package edu.taskmanager.backend.strategy.sorting;

import java.util.Comparator;
import java.util.List;

import edu.taskmanager.backend.model.Task;

public interface TaskSortingStrategy {
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator);
}
