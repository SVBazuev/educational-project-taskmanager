package edu.taskmanager;

import edu.taskmanager.builder.ProjectBuilder;
import edu.taskmanager.builder.TaskBuilder;
import edu.taskmanager.model.Project;
import edu.taskmanager.model.Task;
import edu.taskmanager.util.Priority;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        //Создание задач с помощью TaskBuilder
        Task task = new TaskBuilder()
                .setTitle("Важная задача")
                .setDescription("Описание задачи")
                .setDueDate(LocalDateTime.now().plusDays(7))
                .setPriority(Priority.HIGH)
                .setStatus("IN_PROGRESS")
                .setProject("Проект А")
                .setTags(Arrays.asList("работа", "срочно"))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .build();

        System.out.println(task.toStringBuilder());

        ////Создание задач с помощью ProjectBuilder
        Project task1 = new Project();
        task1.setId(2L);
        task1.setName("Задача 1");

        Project task2 = new Project();
        task2.setId(3L);
        task2.setName("Задача 2");
        task2.setDescription("Описание задачи 2");

        Project project = new ProjectBuilder()
                .setId(1L)
                .setName("Мой проект")
                .setDescription("Описание моего проекта")
                .addTask(task1)
                .addTask(task2)
                .build();

        System.out.println(project);


        //System.out.println("Hello world!");
    }
}