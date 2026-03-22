package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CocktailTaskSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        if (tasks == null) {
            return new ArrayList<>();
        }
        List<Task> sorted = new ArrayList<>(tasks);
        boolean swapped;
        int start = 0;
        int end = sorted.size() - 1;

        do {
            swapped = false;

            for (int i = start; i < end; i++) {
                if (comparator.compare(sorted.get(i), sorted.get(i + 1)) > 0) {
                    Task temp = sorted.get(i);
                    sorted.set(i, sorted.get(i + 1));
                    sorted.set(i + 1, temp);
                    swapped = true;
                }
            }

            if (!swapped) break;

            end--;
            swapped = false;

            for (int i = end - 1; i >= start; i--) {
                if (comparator.compare(sorted.get(i), sorted.get(i + 1)) > 0) {
                    Task temp = sorted.get(i);
                    sorted.set(i, sorted.get(i + 1));
                    sorted.set(i + 1, temp);
                    swapped = true;
                }
            }

            start++;

        } while (swapped);

        return sorted;
    }
}
