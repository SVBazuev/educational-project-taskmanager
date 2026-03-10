package edu.taskmanager.console.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.taskmanager.model.Project;
import edu.taskmanager.model.Tag;
import edu.taskmanager.model.Task;
import edu.taskmanager.model.User;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppData {
    private List<User> users;
    private List<Task> tasks;
    private List<Project> projects;
    private List<Tag> tags;

    public AppData() {

    }

    public List<User> getUsers() {
        return users;
    }

    public List<Project> getProjects(){
        return projects;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
