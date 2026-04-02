package edu.taskmanager.backend.chain;


import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;

import java.util.Map;


/**
 * Фильтр, пропускающий задачи, принадлежащие заданному пользователю.
 */
public class CreatorFilter implements TaskFilter, FilterFactory {
    //private final User requiredUser;
    private TaskFilter next;
    private Long id;
    private  String name;


    public CreatorFilter() {
    }

    public CreatorFilter( Long  id){
        this.id = id;
    }

    public CreatorFilter( String name){
        this.name = name;
    }

    @Override
    public boolean filter(Task task) {
        boolean belongsToUser = (
            task.getCreator() != null
            && task.getCreator().getId().equals(id)
                || task.getCreator() != null && task.getCreator().getUsername().equals(name)
        );

        if (!belongsToUser) { return false; }

        if (next != null) { return next.filter(task); }

        return true;
    }

    @Override
    public void setNext(TaskFilter next) {
        this.next = next;
    }

    @Override
    public TaskFilter create(Map.Entry<String, String> value) throws IllegalArgumentException {
        try {
            long userid = Long.parseLong(value.getValue());
            return new ProjectFilter(userid);//filter;
        } catch (NumberFormatException e) {
            return new ProjectFilter(value.getValue());
        }
    }
}
