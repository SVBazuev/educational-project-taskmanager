package edu.taskmanager.console.handlers;


import java.util.List;


import edu.taskmanager.console.Command;


public class ExitCommand implements Command {
    private final Runnable exitAction;
    private final DataSaver dataSaver;
    private final String saveFilePath;

    public ExitCommand(Runnable exitAction, DataSaver dataSaver, String saveFilePath) {
        this.exitAction = exitAction;
        this.dataSaver = dataSaver;
        this.saveFilePath = saveFilePath;
    }

    @Override
    public void execute(List<String> args) {
        System.out.println("Сохранение данных перед выходом...");
        dataSaver.save(saveFilePath);
        System.out.println("Выход из программы.");
        exitAction.run();
    }

    @Override
    public String getDescription() {
        return "exit - выход из программы";
    }
}
