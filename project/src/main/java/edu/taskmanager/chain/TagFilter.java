package edu.taskmanager.chain;

import edu.taskmanager.model.Tag;
import edu.taskmanager.model.Task;

import java.util.List;
import java.util.stream.Collectors;

public class TagFilter implements TaskFilter {
    private Tag tag = null;

    public void setTag(Tag newTag){
        this.tag = newTag;
    }

    @Override
    public List<Task> apply(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getTags() != null && task.getTags().contains(tag.getName()))
                .collect(Collectors.toList());
    }
}
