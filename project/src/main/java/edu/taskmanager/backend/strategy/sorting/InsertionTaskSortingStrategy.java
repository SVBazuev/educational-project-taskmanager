package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InsertionTaskSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        if (tasks == null) {
            return new ArrayList<>();
        }
        List<Task> sorted = new ArrayList<>(tasks);
        for (int i = 1; i < sorted.size(); i++) {
            Task key = sorted.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(sorted.get(j), key) > 0) {
                sorted.set(j + 1, sorted.get(j));
                j--;
            }
            sorted.set(j + 1, key);
        }
        return sorted;
    }
}
