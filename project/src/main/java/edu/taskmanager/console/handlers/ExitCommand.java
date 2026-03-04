package edu.taskmanager.console.handlers;


import java.util.List;


import edu.taskmanager.console.Command;


public class ExitCommand implements Command {
    private final Runnable exitAction;

    public ExitCommand(Runnable exitAction) {
        this.exitAction = exitAction;
    }

    @Override
    public void execute(List<String> args) {
        System.out.println("Выход из программы.");
        exitAction.run();
    }

    @Override
    public String getDescription() {
        return "exit - выход из программы";
    }
}
