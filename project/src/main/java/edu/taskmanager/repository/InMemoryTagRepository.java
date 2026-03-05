package edu.taskmanager.repository;


import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import edu.taskmanager.model.Tag;


public class InMemoryTagRepository implements TagRepository {
    private final Map<Long, Tag> storage = new ConcurrentHashMap<>();
    private final Map<String, Long> nameToId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Tag save(Tag tag) {
        // Проверим по nameToId, есть ли уже такое имя
        Long existingId = nameToId.get(tag.getName());
        if (existingId != null) {
            // Возвращаем существующий тег (не создаём новый)
            return storage.get(existingId);
        }
        // Создаём новый
        long newId = idGenerator.getAndIncrement();
        Tag tagWithId = tag.withId(newId);
        storage.put(newId, tagWithId);
        nameToId.put(tagWithId.getName(), newId);
        
        return tagWithId;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Long id = nameToId.get(name);
        if (id != null) {
            return Optional.ofNullable(storage.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        Tag tag = storage.remove(id);
        if (tag != null) {
            nameToId.remove(tag.getName());
        }
    }
}
