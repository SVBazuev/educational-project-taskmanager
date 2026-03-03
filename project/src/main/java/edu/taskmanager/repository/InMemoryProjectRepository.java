package edu.taskmanager.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.taskmanager.model.Project;

public class InMemoryProjectRepository implements ProjectRepository {
    private final Map<Long, Project> storage = new ConcurrentHashMap<>();
    private final Map<String, Long> nameToId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Project save(Project project) {
        if (project.getId() == null) {
            Long existingId = nameToId.get(project.getName());
            if (existingId != null) {
                return storage.get(existingId);
            }
            long newId = idGenerator.getAndIncrement();
            Project projectWithId = project.withId(newId);
            storage.put(newId, projectWithId);
            nameToId.put(projectWithId.getName(), newId);
            return projectWithId;
        } else {
            Project old = storage.get(project.getId());
            if (old != null && !old.getName().equals(project.getName())) {
                nameToId.remove(old.getName());
                nameToId.put(project.getName(), project.getId());
            }
            storage.put(project.getId(), project);
            return project;
        }
    }

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Project> findByName(String name) {
        Long id = nameToId.get(name);
        return id != null ? Optional.ofNullable(storage.get(id)) : Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        Project project = storage.remove(id);
        if (project != null) {
            nameToId.remove(project.getName());
        }
    }
}
