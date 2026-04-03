package edu.taskmanager.backend.chain;

import java.util.Map;

public interface FilterFactory {

    public TaskFilter createFilter(Map.Entry<String, String> entry)
    throws IllegalArgumentException;

}
