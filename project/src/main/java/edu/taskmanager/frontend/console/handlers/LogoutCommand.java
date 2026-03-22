package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.frontend.console.Command;
import edu.taskmanager.frontend.console.util.AppContext;

import java.util.List;

public class LogoutCommand implements Command {
    private final AppContext appContext;

    public LogoutCommand(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void execute(List<String> args) {
        if (appContext.getCurrentUser() == null) {
            System.out.println("Вы не вошли в систему.");
            return;
        }
        appContext.clear();
        System.out.println("Вы вышли из системы.");
    }

    @Override
    public String getDescription() {
        return "logout - выход из системы";
    }
}
