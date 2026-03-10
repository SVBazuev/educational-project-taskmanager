package edu.taskmanager.frontend.console.util;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

public class LenientObjectIdResolver extends SimpleObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {
        if(_items == null || !_items.containsKey(id)) {
            super.bindItem(id, pojo);
        }

        // Если есть - игнор без исключений
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new LenientObjectIdResolver();
    }
}
