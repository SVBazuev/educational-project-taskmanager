package edu.taskmanager.frontend.console.decorator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.taskmanager.frontend.console.Command;

public record MultithreadingCommandDecorator(Command wrapped) implements Command {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            wrapped.execute(args);
            return;
        }

        System.out.println("[System] >>> ФОНОВЫЙ ЗАПУСК: " + wrapped.getClass().getSimpleName());
        executor.submit(() -> {
            try {
                wrapped.execute(args);
                System.out.println("\n[System] <<< ЗАВЕРШЕНО: " + wrapped.getClass().getSimpleName());
            } catch (Exception e) {
                System.err.println("\n[Error] Ошибка в фоне: " + e.getMessage());
            }
        });
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription() + " (выполняется асинхронно)";
    }
}
