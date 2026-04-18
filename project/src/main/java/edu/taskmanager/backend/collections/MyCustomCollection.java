package edu.taskmanager.backend.collections;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.util.TaskStatus;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.DateType;
import edu.taskmanager.backend.chain.FilterChain;

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
    public void addAll(T[] items) {
    for (T item : items) {
        this.add(item);
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

/**
     * Метод для подсчета элементов, удовлетворяющих предикату.
     * @param condition предикат для фильтрации
     * @return количество элементов, удовлетворяющих условию
     */
    public long getOccurrenceCounter(Predicate<T> condition) {
        return this.parallelStream()
                   .filter(condition)
                   .count();
    }

    /**
     * Метод для подсчета элементов, удовлетворяющих фильтрам из цепочки.
     * @param filterChain цепочка фильтров
     * @return количество элементов, прошедших все фильтры
     */
    public long getFilteredCount(FilterChain filterChain) {
        if (filterChain.isEmpty()) {
            return size; // Если цепочка фильтров пуста, возвращаем размер коллекции
        }
        return filterChain.apply(this.stream().filter(e -> e instanceof Task).map(e -> (Task) e).toList()).size();
    }

    /**
     * Метод для получения коллекции элементов, удовлетворяющих фильтрам из цепочки.
     * @param filterChain цепочка фильтров
     * @return коллекция элементов, прошедших все фильтры
     */
    public MyCustomCollection<T> getFilteredCollection(FilterChain filterChain) {
        MyCustomCollection<T> filteredCollection = new MyCustomCollection<>(size);
        if (filterChain.isEmpty()) {
            for (T item : this) {
                filteredCollection.add(item);
            }
            return filteredCollection;
        }
        filterChain.apply(this.stream().filter(e -> e instanceof Task).map(e -> (Task) e).toList())
                .forEach(task -> filteredCollection.add((T) task));
        return filteredCollection;
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
