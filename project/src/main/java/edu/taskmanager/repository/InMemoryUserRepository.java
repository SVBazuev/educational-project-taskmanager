package edu.taskmanager.repository;


import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import edu.taskmanager.model.User;


public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> storage = new ConcurrentHashMap<>();
    private final Map<String, Long> usernameToId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            Long existingId = usernameToId.get(user.getUsername());
            if (existingId != null) {
                return storage.get(existingId);
            }
            long newId = idGenerator.getAndIncrement();
            User userWithId = user.withId(newId);
            storage.put(newId, userWithId);
            usernameToId.put(userWithId.getUsername(), newId);
            return userWithId;
        } else {
            User old = storage.get(user.getId());
            if (old != null && !old.getUsername().equals(user.getUsername())) {
                usernameToId.remove(old.getUsername());
                usernameToId.put(user.getUsername(), user.getId());
            }
            storage.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Long id = usernameToId.get(username);
        return id != null ? Optional.ofNullable(storage.get(id)) : Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        User user = storage.remove(id);
        if (user != null) {
            usernameToId.remove(user.getUsername());
        }
    }
}
