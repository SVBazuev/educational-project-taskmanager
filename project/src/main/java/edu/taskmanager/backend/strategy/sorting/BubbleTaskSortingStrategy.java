package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BubbleTaskSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        if (tasks == null) {
            return new ArrayList<>();
        }
        List<Task> sorted = new ArrayList<>(tasks);
        int n = sorted.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(sorted.get(j), sorted.get(j + 1)) > 0) {
                    Task temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return sorted;
    }
}
