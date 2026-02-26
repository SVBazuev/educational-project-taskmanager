package edu.taskmanager.repository;


import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import edu.taskmanager.model.Task;
// import edu.taskmanager.repository.TaskRepository;


public class InMemoryTaskRepository implements TaskRepository{
    private final Map<Long, Task> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Task save(Task task) {
        // TODO: если у задачи нет id, сгенерировать новый.

        // TODO: сохранить в storage, вернуть задачу с установленным id.
        return null;
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
        // TODO: пройти по всем задачам и выбрать те, у которых parentId совпадает.
        // В модели Task пока нет поля parentId, нужно добавить или искать по-другому.
        // Для упрощения можно хранить подзадачи внутри родительской задачи и здесь возвращать их.
        return null;
    }
}
