package edu.taskmanager.backend.collections;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.DateType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MyCustomCollection<T> implements Iterable<T> {
    private T[] array; //массив
    private int size; //индекс элемента для добавления

    public MyCustomCollection(int length) {
        array = (T[]) new Object[length];
        size = 0;
    }

    // Существующие методы
    public void add(T value) { //добавить элемент в конец
        ensureCapacity();
        array[size++] = value;
    }
    public void add(int index, T value) {
    checkIndex(index); // Проверка индекса
    ensureCapacity(); // Увеличение ёмкости массива при необходимости
    System.arraycopy(array, index, array, index + 1, size - index); // Смещение элементов
    array[index] = value; // Вставка нового элемента
    size++; // Увеличение размера
    }
    public T get(int index) { //получить элемент
        checkIndex(index);
        return array[index];
    }

    public void remove(int index) { //удалить элемент
        checkIndex(index);
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        size--;
    }

    public void addAll(MyCustomCollection<T> list) { //добавить другой лист
        for (int i = 0; i < list.size; i++) {
            this.add(list.get(i));
        }
    }
    public void addAll(Task[] tasks) {
        for (Task task : tasks) {
            this.add(task);
        }
    }
    
    public void set(int index, T value) {
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
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    private void ensureCapacity() { //проверить емкость, при необходимости увеличить
        if (size == array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Индекс вне диапазона");
    }

    

    // ============ СУЩЕСТВУЮЩИЕ МЕТОДЫ  ============
    
    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(array, size));
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < size;
            }
            @Override
            public T next() {
                return array[index++];
            }
        };
    }

    public Stream<T> stream() {
        return StreamSupport.stream(
                java.util.Spliterators.spliterator(array, 0, size, Spliterator.ORDERED),
                false
        );
    }

    public Stream<T> parallelStream() {
        return StreamSupport.stream(
                java.util.Spliterators.spliterator(array, 0, size, Spliterator.ORDERED),
                true
        );
    }

    public long getOccurrenceCounter(T target) {
        return this.parallelStream()
                .filter(i -> i != null && i.equals(target))
                .count();
    }

    public long getOccurrenceCounterMultiThreaded(T target) {
        return this.parallelStream()
                .filter(i -> i != null && i.equals(target))
                .count();
    }

    public long getOccurrenceCounterManualThreads(T target, int numberOfThreads) {
        if (size == 0) return 0;
        if (numberOfThreads <= 0) {
            numberOfThreads = Runtime.getRuntime().availableProcessors();
        }
        numberOfThreads = Math.min(numberOfThreads, size);

        long[] results = new long[numberOfThreads];
        Thread[] threads = new Thread[numberOfThreads];
        int segmentSize = (size + numberOfThreads - 1) / numberOfThreads;

        for (int t = 0; t < numberOfThreads; t++) {
            final int threadIndex = t;
            final int start = t * segmentSize;
            final int end = Math.min(start + segmentSize, size);

            threads[t] = new Thread(() -> {
                long count = 0;
                for (int i = start; i < end; i++) {
                    T element = array[i];
                    if (element != null && element.equals(target)) {
                        count++;
                    }
                }
                results[threadIndex] = count;
            });
            threads[t].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Поток был прерван: " + e.getMessage());
            }
        }

        long total = 0;
        for (long result : results) {
            total += result;
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyCustomCollection<?> other)) return false;
        if (this.size() != other.size()) return false;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).equals(other.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }
}
