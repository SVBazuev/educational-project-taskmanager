package edu.taskmanager.backend.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.taskmanager.backend.model.Project;

public class ProjectRepository implements Repository<Project, Long> {
    private final Map<Long, Project> storage = new ConcurrentHashMap<>();
    private final Map<String, Long> nameToId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Project save(Project project) {
        if (project.getId() == null) {
            Long existingId = nameToId.get(project.getName());
            if (existingId != null) {
                return copyOf(storage.get(existingId));
            }
            long newId = idGenerator.getAndIncrement();
            Project projectWithId = project.withId(newId);
            storage.put(newId, projectWithId);
            nameToId.put(projectWithId.getName(), newId);
            return copyOf(projectWithId);
        } else {
            Project old = storage.get(project.getId());
            if (old != null && !old.getName().equals(project.getName())) {
                nameToId.remove(old.getName());
                nameToId.put(project.getName(), project.getId());
            } else if (old == null) {
                nameToId.put(project.getName(), project.getId());
            }
            Project copy = project.withId(project.getId());
            storage.put(project.getId(), copy);
            idGenerator.updateAndGet(current -> Math.max(current, project.getId() + 1));
            return copyOf(copy);
        }
    }

    @Override
    public Optional<Project> findById(Long id) {
        Project p = storage.get(id);
        return p != null ? Optional.of(copyOf(p)) : Optional.empty();
    }

//    @Override
//    public Optional<Project> findByName(String name) {
//        Long id = nameToId.get(name);
//        if (id == null) return Optional.empty();
//        Project p = storage.get(id);
//        return p != null ? Optional.of(copyOf(p)) : Optional.empty();
//    }

    @Override
    public List<Project> findAll() {
        return storage.values().stream()
                .map(this::copyOf)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Project project = storage.remove(id);
        if (project != null) {
            nameToId.remove(project.getName());
        }
    }

    private Project copyOf(Project p) {
        return p.withId(p.getId());
    }
}
