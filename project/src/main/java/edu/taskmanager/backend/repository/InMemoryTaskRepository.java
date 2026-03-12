package edu.taskmanager.backend.repository;


import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.taskmanager.backend.model.Task;


/**
 * Реализация репозитория в оперативной памяти
 * с использованием потокобезопасных коллекций.
 * Хранит задачи в ConcurrentHashMap, где ключ — идентификатор задачи.
 * Для генерации идентификаторов используется AtomicLong.
 */
public class InMemoryTaskRepository implements TaskRepository {

    /**
     * Хранилище задач: id -> Task
     */
    private final Map<Long, Task> storage = new ConcurrentHashMap<>();

    /**
     * Генератор уникальных идентификаторов для новых задач.
     * Начинается с 1.
     */
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Task save(Task task) {
        // Если задача новая (id == null), генерируем id и сохраняем
        if (task.getId() == null) {
            long newId = idGenerator.getAndIncrement();
            // Создаём копию задачи с установленным id, чтобы не изменять исходный объект
            Task taskWithId = task.withId(newId); // предполагаем наличие метода withId
            storage.put(newId, taskWithId);
            return copyOf(taskWithId);
        } else {
            Task copy = task.withId(task.getId());
            storage.put(task.getId(), copy);
            // Обязательно обновить атомик последним ID
            idGenerator.updateAndGet(current -> Math.max(current, task.getId() + 1));
            return copyOf(copy);
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        Task t = storage.get(id);
        return t != null ? Optional.of(copyOf(t)) : Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        return storage.values().stream()
                .map(this::copyOf)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public List<Task> findSubtasksByParentId(Long parentId) {
        return storage.values().stream()
                .filter(task -> parentId.equals(task.getParentId()))
                .map(this::copyOf)
                .collect(java.util.stream.Collectors.toList());
    }

    private Task copyOf(Task t) {
        return t.withId(t.getId());
    }
}
