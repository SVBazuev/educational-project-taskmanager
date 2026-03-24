package edu.taskmanager.backend.strategy.sorting;


import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;


import edu.taskmanager.backend.model.Task;


//СОРТИРОВКА ПО Priority Status И Title
public class BubbleTaskSortingStrategy implements TaskSortingStrategy {

    @Override
    public List<Task> sort(List<Task> tasks, Comparator<Task> comparator) {
        // Создаем копию списка, чтобы не изменять исходный
        List<Task> sortedTasks = new ArrayList<>(tasks);
        int n = sortedTasks.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(sortedTasks.get(j), sortedTasks.get(j + 1)) > 0) {
                    Task temp = sortedTasks.get(j);
                    sortedTasks.set(j, sortedTasks.get(j + 1));
                    sortedTasks.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        return sortedTasks;
    }
}
