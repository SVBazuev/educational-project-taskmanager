package edu.taskmanager.backend.collections;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.DateType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Кастомная коллекция для хранения задач (Task).
 * Полностью заменяет стандартные коллекции - НЕТ зависимости от List/Collection.
 */
public class TaskCollection implements Iterable<Task> {
    private Task[] array;
    private int size;

    // ============ КОНСТРУКТОРЫ ============
    
    public TaskCollection(int initialCapacity) {
        array = new Task[initialCapacity];
        size = 0;
    }
    
    public TaskCollection() {
        this(10);
    }
    
    // ВАЖНО: НЕТ конструктора из Collection!
    // Все данные добавляются через методы add
    
    // ============ БАЗОВЫЕ МЕТОДЫ ============
    
    public void add(Task value) {
        ensureCapacity();
        array[size++] = value;
    }
    
    public void add(int index, Task value) {
        checkIndexForAdd(index);
        ensureCapacity();
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = value;
        size++;
    }
    
    /**
     * Добавление всех задач из другой TaskCollection
     */
    public void addAll(TaskCollection other) {
        for (int i = 0; i < other.size; i++) {
            this.add(other.array[i]);
        }
    }
    
    /**
     * Добавление всех задач из массива
     */
    public void addAll(Task[] tasks) {
        for (Task task : tasks) {
            this.add(task);
        }
    }

    public Task get(int index) {
        checkIndex(index);
        return array[index];
    }

    public Task remove(int index) {
        checkIndex(index);
        Task removed = array[index];
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        array[--size] = null;
        return removed;
    }
    
    public boolean remove(Task task) {
        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i].equals(task)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    public void set(int index, Task value) {
        checkIndex(index);
        array[index] = value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(array, 0, size, null);
        size = 0;
    }
    
    public boolean contains(Task task) {
        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i].equals(task)) {
                return true;
            }
        }
        return false;
    }
    
    public Task[] toArray() {
        return Arrays.copyOf(array, size);
    }

    private void ensureCapacity() {
        if (size == array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }
    
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        }
    }

    // ============ МЕТОДЫ ФИЛЬТРАЦИИ (Enum из файлов 1-3) ============
    
    public TaskCollection filterByStatus(TaskStatus status) {
        TaskCollection result = new TaskCollection();
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null && task.getStatus() == status) {
                result.add(task);
            }
        }
        return result;
    }
    
    public TaskCollection filterByPriority(Priority priority) {
        TaskCollection result = new TaskCollection();
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null && task.getPriority() == priority) {
                result.add(task);
            }
        }
        return result;
    }
    
    public TaskCollection filterByDateRange(DateType dateType, LocalDateTime start, LocalDateTime end) {
        TaskCollection result = new TaskCollection();
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null) {
                LocalDateTime date = dateType.extract(task);
                if (date != null) {
                    boolean afterStart = start == null || !date.isBefore(start);
                    boolean beforeEnd = end == null || !date.isAfter(end);
                    if (afterStart && beforeEnd) {
                        result.add(task);
                    }
                }
            }
        }
        return result;
    }
    
    public TaskCollection filter(Predicate<Task> predicate) {
        TaskCollection result = new TaskCollection();
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null && predicate.test(task)) {
                result.add(task);
            }
        }
        return result;
    }
    
    // ============ СПЕЦИАЛИЗИРОВАННЫЕ МЕТОДЫ ============
    
    public TaskCollection findTasksDueSoon(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(days);
        
        TaskCollection result = new TaskCollection();
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null && task.getDueDate() != null) {
                LocalDateTime dueDate = task.getDueDate();
                if (!dueDate.isBefore(now) && !dueDate.isAfter(deadline)) {
                    result.add(task);
                }
            }
        }
        return result;
    }
    
    public TaskCollection findOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        TaskCollection result = new TaskCollection();
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null && task.getDueDate() != null && task.getDueDate().isBefore(now)) {
                result.add(task);
            }
        }
        return result;
    }
    
    // ============ СТАТИСТИКА ============
    
    public Map<TaskStatus, Long> getStatusStatistics() {
        Map<TaskStatus, Long> stats = new HashMap<>();
        for (TaskStatus status : TaskStatus.values()) {
            stats.put(status, 0L);
        }
        
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null) {
                TaskStatus status = task.getStatus();
                stats.put(status, stats.get(status) + 1);
            }
        }
        return stats;
    }
    
    public Map<Priority, Long> getPriorityStatistics() {
        Map<Priority, Long> stats = new HashMap<>();
        for (Priority priority : Priority.values()) {
            stats.put(priority, 0L);
        }
        
        for (int i = 0; i < size; i++) {
            Task task = array[i];
            if (task != null) {
                Priority priority = task.getPriority();
                stats.put(priority, stats.get(priority) + 1);
            }
        }
        return stats;
    }
    
    // ============ СОРТИРОВКА ============
    
    public void sort(java.util.Comparator<Task> comparator) {
        Arrays.sort(array, 0, size, comparator);
    }

    // ============ ИТЕРАТОР И STREAM ============
    
    @Override
    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < size;
            }
            @Override
            public Task next() {
                return array[index++];
            }
        };
    }
    
    public Stream<Task> stream() {
        return StreamSupport.stream(
                java.util.Spliterators.spliterator(array, 0, size, Spliterator.ORDERED),
                false
        );
    }

    public Stream<Task> parallelStream() {
        return StreamSupport.stream(
                java.util.Spliterators.spliterator(array, 0, size, Spliterator.ORDERED),
                true
        );
    }
    
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
