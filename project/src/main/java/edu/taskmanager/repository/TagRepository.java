package edu.taskmanager.repository;

import edu.taskmanager.model.Tag;
import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);
    List<Tag> findAll();
    void deleteById(Long id);
}
