package edu.taskmanager.backend.service;

import edu.taskmanager.backend.chain.FilterChain;
import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.*;

import java.util.List;
import java.util.Map;

public class ServisRepository {
    private Repository<Task, Long> taskRepo;
    private Repository<Project, Long> projectRepo;
    private Repository<Tag, Long> tagRepo;
    private Repository<User, Long> userRepo;

//Попытаться перестроить на Record
    public ServisRepository(){
        this.taskRepo= new TaskRepository();
        this.projectRepo= new ProjectRepository();
        this.tagRepo= new TagRepository();
        this.userRepo= new UserRepository();
    }

    public Repository taskRepo (){
        return  (Repository) this.taskRepo;
    }

    public Repository projectRepo (){
        return (Repository) this.projectRepo;
    }

    public Repository tagRepo (){
        return (Repository) this.tagRepo;
    }

    public Repository userRepo (){
        return (Repository) this.userRepo;
    }

    //ServisRepository.taskRepo.save()

    public List<Task> findBy (Map<String, String> criteria){

        FilterChain filterChain = new FilterChain();
        criteria.entrySet().stream()
                .forEach(filterChain::createChain);
        if (filterChain.isEmpty()) {
            System.out.println("Не задано ни одного корректного критерия фильтрации.");
            return null;
        }

        return findBy(filterChain);
    }

    public List<Task> findBy (FilterChain chain){
        List<Task> allTasks = taskRepo.findAll();
        return chain.apply(allTasks);
    }

    public List<Task> findAllTasks(){
        return taskRepo.findAll();
    }

}

