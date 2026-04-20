package edu.taskmanager.frontend.console.handlers;


import java.util.*;


import edu.taskmanager.backend.chain.*;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.frontend.console.Command;
import static edu.taskmanager.frontend.console.parser.ArgumentParser.parseArguments;


public class FilterCommand implements Command {
    private List<Task> lastResult;
    private  final ServisRepository servisRepository;

    public FilterCommand(ServisRepository servisRepository) {
        this.servisRepository = servisRepository;
        this.lastResult = new ArrayList<>();
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("Использование: filter&ключ1=значение1&ключ2=значение2...");
            System.out.println("Доступные ключи: description, status, tag, project, user, priority, " +
                    "duestartdate, dueenddate, startdate, enddate, upstartdate, upenddate");
            return;
        }

        // Парсим аргументы в карту критериев
        Map<String, String> criteria = parseArguments(args);

        // Получаем все задачи и фильтруем
        lastResult = servisRepository.findBy(criteria);

        // Вывод
        if (lastResult.isEmpty()) {
            System.out.println("Задачи, удовлетворяющие условиям, не найдены.");
        } else {
            System.out.println("Найдено задач: " + lastResult.size());
            lastResult.forEach(System.out::println);
        }
    }

    public List<Task> getLastResult() {
        return lastResult != null ? new ArrayList<>(lastResult) : new ArrayList<>();
    }

    @Override
    public String getDescription() {

        return new StringBuilder()
            .append("filter&ключ=значение&... - фильтрация задач\n")
            .append("                   ")
            .append("Доступные поля:\n")
            .append("                       ")
            .append("description, dueDate, creator,\n")
            .append("                       ")
            .append("priority, status, project, tag, subtasks,\n")
            .append("                       ")
            .append("contractors, createdAt, updatedAt, parentId")
            .toString();
    }
}
