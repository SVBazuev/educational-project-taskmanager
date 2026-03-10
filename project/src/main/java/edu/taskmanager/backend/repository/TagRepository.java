package edu.taskmanager.backend.repository;

import java.util.List;
import java.util.Optional;

import edu.taskmanager.backend.model.Tag;

public interface TagRepository {
    Tag save(Tag tag);
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);
    List<Tag> findAll();
    void deleteById(Long id);
}
