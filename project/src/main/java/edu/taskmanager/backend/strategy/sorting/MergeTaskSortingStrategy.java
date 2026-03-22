package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeTaskSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        if (tasks == null) {
            return new ArrayList<>();
        }
        List<Task> sorted = new ArrayList<>(tasks);
        mergeSort(sorted, comparator, 0, sorted.size() - 1);
        return sorted;
    }

    private void mergeSort(List<Task> tasks, Comparator<Task> comparator, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(tasks, comparator, left, middle);
            mergeSort(tasks, comparator, middle + 1, right);
            merge(tasks, comparator, left, middle, right);
        }
    }

    private void merge(List<Task> tasks, Comparator<Task> comparator, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        List<Task> leftPart = new ArrayList<>();
        List<Task> rightPart = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            leftPart.add(tasks.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightPart.add(tasks.get(middle + 1 + j));
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comparator.compare(leftPart.get(i), rightPart.get(j)) <= 0) {
                tasks.set(k, leftPart.get(i));
                i++;
            } else {
                tasks.set(k, rightPart.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            tasks.set(k, leftPart.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            tasks.set(k, rightPart.get(j));
            j++;
            k++;
        }
    }
}
