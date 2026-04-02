package edu.taskmanager.backend.repository;


import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.taskmanager.backend.model.Tag;


public class InMemoryTagRepository implements TagRepository {
    private final Map<Long, Tag> storage = new ConcurrentHashMap<>();
    private final Map<String, Long> nameToId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Tag save(Tag tag) {
        if (tag.getId() != null) {
            // Если тег пришёл с уже известным id
            Tag old = storage.get(tag.getId());
            if (old != null) {
                nameToId.remove(old.getName());
            }
            Tag copy = tag.withId(tag.getId());
            storage.put(tag.getId(), copy);
            nameToId.put(copy.getName(), tag.getId());
            // Обновляем генератор, чтобы новые id не конфликтовали с импортированными
            idGenerator.updateAndGet(current -> Math.max(current, tag.getId() + 1));
            return copyOf(copy);
        }
        // Проверим по nameToId, есть ли уже такое имя
        Long existingId = nameToId.get(tag.getName());
        if (existingId != null) {
            // Возвращаем существующий тег (не создаём новый)
            return copyOf(storage.get(existingId));
        }
        // Создаём новый
        long newId = idGenerator.getAndIncrement();
        Tag tagWithId = tag.withId(newId);
        storage.put(newId, tagWithId);
        nameToId.put(tagWithId.getName(), newId);

        return copyOf(tagWithId);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Tag t = storage.get(id);
        return t != null ? Optional.of(copyOf(t)) : Optional.empty();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Long id = nameToId.get(name);
        if (id == null) return Optional.empty();
        Tag t = storage.get(id);
        return t != null ? Optional.of(copyOf(t)) : Optional.empty();
    }

    @Override
    public List<Tag> findAll() {
        return storage.values().stream()
                .map(this::copyOf)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Tag tag = storage.remove(id);
        if (tag != null) {
            nameToId.remove(tag.getName());
        }
    }

    private Tag copyOf(Tag t) {
        return t.withId(t.getId());
    }
}
