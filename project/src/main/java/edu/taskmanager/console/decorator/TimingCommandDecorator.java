package edu.taskmanager.console.decorator;


import java.util.List;


import edu.taskmanager.console.Command;


public class TimingCommandDecorator implements Command {
    private final Command wrapped;

    public TimingCommandDecorator(Command wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void execute(List<String> args) {
        long start = System.nanoTime();
        wrapped.execute(args);
        long end = System.nanoTime();
        double ms = (end - start) / 1_000_000.0;
        System.out.printf("[TIMING] Команда выполнена за %.2f мс%n", ms);
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }
}
