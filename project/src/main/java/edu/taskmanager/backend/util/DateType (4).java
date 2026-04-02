package edu.taskmanager.backend.util;

import java.time.LocalDateTime;
import java.util.function.Function;

import edu.taskmanager.backend.model.Task;

/**
 * Тип даты для фильтрации задач
 */
public enum DateType {
    /**
     * Фильтр по дате создания задачи
     */
    CREATION(Task::getCreatedAt, "startdate", "enddate"),
    
    /**
     * Фильтр по дате последнего обновления задачи
     */
    UPDATE(Task::getUpdatedAt, "upstartdate", "upenddate"),
    
    /**
     * Фильтр по сроку выполнения задачи
     */
    DUE_DATE(Task::getDueDate, "duestartdate", "dueenddate");
    
    private final Function<Task, LocalDateTime> dateExtractor;
    private final String startKey;
    private final String endKey;
    
    DateType(Function<Task, LocalDateTime> dateExtractor, String startKey, String endKey) {
        this.dateExtractor = dateExtractor;
        this.startKey = startKey;
        this.endKey = endKey;
    }
    
    /**
     * Извлекает дату из задачи в соответствии с типом фильтра
     * @param task задача для извлечения даты
     * @return дата задачи или null, если дата отсутствует
     */
    public LocalDateTime extract(Task task) {
        return dateExtractor.apply(task);
    }
    
    public String getStartKey() {
        return startKey;
    }
    
    public String getEndKey() {
        return endKey;
    }
    
    /**
     * Проверяет, содержится ли ключ в этом типе даты
     */
    public boolean containsKey(String key) {
        return key.equals(startKey) || key.equals(endKey);
    }
    
    /**
     * Получить DateType по ключу
     * @param key ключ параметра (startdate, enddate, duestartdate, etc.)
     * @return соответствующий DateType или null
     */
    public static DateType fromKey(String key) {
        for (DateType type : values()) {
            if (type.containsKey(key)) {
                return type;
            }
        }
        return null;
    }
    
    // /**
    //  * Получить тип даты по ключу начала
    
    // public static DateType fromStartKey(String key) {
    //     for (DateType type : values()) {
    //         if (type.getStartKey().equals(key)) {
    //             return type;
    //         }
    //     }
    //     return null;
    // }
    
    // /**
    //  * Получить тип даты по ключу окончания
    //  */
    // public static DateType fromEndKey(String key) {
    //     for (DateType type : values()) {
    //         if (type.getEndKey().equals(key)) {
    //             return type;
    //         }
    //     }
    //     return null;
    // }
    //  */
}