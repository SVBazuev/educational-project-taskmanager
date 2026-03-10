package edu.taskmanager.adapter.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.taskmanager.console.util.AppData;
import edu.taskmanager.model.Task;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTaskExporter implements TaskExporter {
    private final ObjectMapper objectMapper;
    private final String outputFilePath;

    public JsonTaskExporter(String outputFilePath){
        this.outputFilePath = outputFilePath;
        this.objectMapper = new ObjectMapper();

        // Для работы с LocalDateTime
        this.objectMapper.registerModule(new JavaTimeModule());

        // А сериализация в формате YYYY-MM-DDTHH:mm:ss будет работать по умолчанию
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Форматирование
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String export(List<Task> tasks){
        Map<String, List<Task>> wrapper = new HashMap<>();
        wrapper.put("tasks", tasks);

        try {
            String json = objectMapper.writeValueAsString(wrapper);

            // Сохранение в файл
            File outputFile = new File(outputFilePath);
            // Создаём родительские директории, если их нет
            if(outputFile.getParentFile() != null){
                outputFile.getParentFile().mkdirs();
            }
            objectMapper.writeValue(outputFile, wrapper);

            System.out.println("[JsonTaskExporter] Задачи успешно экспортированы в JSON файл: " + outputFilePath);
            return json;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте задач в JSON: " + e.getMessage(), e);
        }
    }

    public void exportAppData(AppData data) {
        try {
            File outputFile = new File(outputFilePath);
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            objectMapper.writeValue(outputFile, data);
            System.out.println("[JsonTaskExporter] Данные успешно сохранены в файл: " + outputFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении данных: " + e.getMessage(), e);
        }
    }
}


