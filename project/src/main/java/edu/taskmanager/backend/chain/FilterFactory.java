package edu.taskmanager.backend.chain;

import java.util.Map;
import java.util.Set;

public interface FilterFactory {

    public TaskFilter createFilter(Map.Entry<String, String> value) throws IllegalArgumentException;

}
