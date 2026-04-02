package edu.taskmanager.backend.chain;

import java.util.Map;
import java.util.Set;

public interface FilterFactory {

    public TaskFilter create(Map.Entry<String, String> value) throws IllegalArgumentException;

}
