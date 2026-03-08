package edu.taskmanager.console.parser;


import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class ArgumentParser {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime parseDate(String value) {
        try {
            return LocalDateTime.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Можно добавить другие методы парсинга
}
