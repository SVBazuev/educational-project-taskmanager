package edu.taskmanager.repository;

import java.util.List;
import java.util.Optional;


import edu.taskmanager.model.Project;


public interface ProjectRepository {
    Project save(Project project);
    Optional<Project> findById(Long id);
    Optional<Project> findByName(String name);
    List<Project> findAll();
    void deleteById(Long id);
}
