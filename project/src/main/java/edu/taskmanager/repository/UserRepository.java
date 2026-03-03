package edu.taskmanager.repository;


import java.util.List;
import java.util.Optional;


import edu.taskmanager.model.User;


public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void deleteById(Long id);
}
