package edu.taskmanager.frontend.console.decorator;


import java.util.List;

import edu.taskmanager.frontend.console.Command;


public class LoggingCommandDecorator implements Command {
    private final Command wrapped;

    public LoggingCommandDecorator(Command wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void execute(List<String> args) {
        System.out.println(
            "[LOG] Выполняется команда: "
            + wrapped.getClass().getSimpleName()
        );
        wrapped.execute(args);
        System.out.println(
            "[LOG] Команда завершена: "
            + wrapped.getClass().getSimpleName()
        );
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }
}
