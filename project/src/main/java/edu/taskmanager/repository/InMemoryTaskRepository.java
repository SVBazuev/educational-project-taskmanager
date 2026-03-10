package edu.taskmanager.repository;


import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import edu.taskmanager.model.Task;


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
            return taskWithId;
        } else {
            // Задача с существующим id — обновляем (перезаписываем)
            storage.put(task.getId(), task);
            return task;
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public List<Task> findSubtasksByParentId(Long parentId) {
        return storage.values().stream()
                .filter(task -> parentId.equals(task.getParentId()))
                .collect(java.util.stream.Collectors.toList());
    }
}
