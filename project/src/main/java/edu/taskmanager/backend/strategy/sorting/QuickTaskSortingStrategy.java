package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuickTaskSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        if (tasks == null) {
            return new ArrayList<>();
        }
        List<Task> sorted = new ArrayList<>(tasks);
        quickSort(sorted, 0, sorted.size() - 1, comparator);
        return sorted;
    }

    private void quickSort(List<Task> tasks, int low, int high, Comparator<Task> comparator) {
        if (low < high) {
            int pivotIndex = partition(tasks, low, high, comparator);
            quickSort(tasks, low, pivotIndex - 1, comparator);
            quickSort(tasks, pivotIndex + 1, high, comparator);
        }
    }

    private int partition(List<Task> tasks, int low, int high, Comparator<Task> comparator) {
        Task pivot = tasks.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparator.compare(tasks.get(j), pivot) <= 0) {
                i++;
                Task temp = tasks.get(i);
                tasks.set(i, tasks.get(j));
                tasks.set(j, temp);
            }
        }
        Task temp = tasks.get(i + 1);
        tasks.set(i + 1, tasks.get(high));
        tasks.set(high, temp);
        return i + 1;
    }
}
