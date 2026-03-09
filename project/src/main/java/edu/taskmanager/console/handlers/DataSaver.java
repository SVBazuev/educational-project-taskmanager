package edu.taskmanager.console.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.taskmanager.console.util.AppData;
import edu.taskmanager.repository.ProjectRepository;
import edu.taskmanager.repository.TagRepository;
import edu.taskmanager.repository.TaskRepository;
import edu.taskmanager.repository.UserRepository;

import java.io.File;
import java.io.IOException;

public class DataSaver {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    public DataSaver(TaskRepository taskRepository, ProjectRepository projectRepository,
                     UserRepository userRepository, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(String resourcePath) {
        AppData data = new AppData();
        data.setTasks(taskRepository.findAll());
        //data.setTags(tagRepository.findAll());
        //data.setProjects(projectRepository.findAll());
       // data.setUsers(userRepository.findAll());

        try{
            File outputFile = resolveOutputFile(resourcePath);
            if(outputFile.getParentFile() != null){
                outputFile.getParentFile().mkdirs();
            }
            objectMapper.writeValue(outputFile, data);
            System.out.println("Данные успешно сохранены в файл: " + resourcePath);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    private File resolveOutputFile(String resourcePath) {
        var url = getClass().getClassLoader().getResource(resourcePath);
        if (url != null){
            return new File(url.getFile());
        }
        return new File(resourcePath);
    }
}
