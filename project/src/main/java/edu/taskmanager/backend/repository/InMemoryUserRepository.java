package edu.taskmanager.backend.repository;


import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.taskmanager.backend.model.User;


public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> storage = new ConcurrentHashMap<>();
    private final Map<String, Long> usernameToId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            Long existingId = usernameToId.get(user.getUsername());
            if (existingId != null) {
                return copyOf(storage.get(existingId));
            }
            long newId = idGenerator.getAndIncrement();
            User userWithId = user.withId(newId);
            storage.put(newId, userWithId);
            usernameToId.put(userWithId.getUsername(), newId);
            return copyOf(userWithId);
        } else {
            User old = storage.get(user.getId());
            if (old != null && !old.getUsername().equals(user.getUsername())) {
                usernameToId.remove(old.getUsername());
                usernameToId.put(user.getUsername(), user.getId());
            } else if (old == null) {
                usernameToId.put(user.getUsername(), user.getId());
            }
            User copy = user.withId(user.getId());
            storage.put(user.getId(), copy);
            // Обновляем генератор, чтобы новые id не конфликтовали с импортированными
            idGenerator.updateAndGet(current -> Math.max(current, user.getId() + 1));
            return copyOf(copy);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        User u = storage.get(id);
        return u != null ? Optional.of(copyOf(u)) : Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Long id = usernameToId.get(username);
        if (id == null) return Optional.empty();
        User u = storage.get(id);
        return u != null ? Optional.of(copyOf(u)) : Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return storage.values().stream()
                .map(this::copyOf)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        User user = storage.remove(id);
        if (user != null) {
            usernameToId.remove(user.getUsername());
        }
    }

    private User copyOf(User u) {
        return u.withId(u.getId());
    }
}
