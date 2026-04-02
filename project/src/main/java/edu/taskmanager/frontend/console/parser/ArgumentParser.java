package edu.taskmanager.frontend.console.parser;


import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class ArgumentParser {
    public static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Optional<LocalDateTime> parseDate(String value) {
        try {
            return Optional.of(LocalDateTime.parse(value, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static Map<String, String> parseArguments(List<String> args) {
        Map<String, String> criteria = new HashMap<>();
        for (String arg : args) {
            String[] kv = arg.split("=", 2);
            if (kv.length == 2) {
                criteria.put(kv[0].toLowerCase(), kv[1]);
            } else {
                System.out.println("Пропущен некорректный аргумент: " + arg);
            }
        }
        return criteria;
    }

    public static Set<String> parseArgumentsList(List<String> args) {
        Set<String> criteria = new HashSet<>();
        for (String arg : args) {
            String[] keys = arg.split("&");
            for (String key : keys) {
                if(key.contains("id")){
                    System.out.println("Id будет пропущен");
                    continue;
                }
                if (!key.trim().isEmpty()) {
                    criteria.add(key.trim().toLowerCase());
                } else {
                    System.out.println("Пропущен пустой аргумент: " + arg);
                }
            }
        }
        return criteria;
    }

    // Можно добавить другие методы парсинга
}
