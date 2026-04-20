package edu.taskmanager.frontend.console.decorator;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.repository.Repository;
import edu.taskmanager.backend.service.ServisRepository;
import edu.taskmanager.frontend.console.Command;
import edu.taskmanager.frontend.console.handlers.FilterCommand;
import edu.taskmanager.frontend.console.handlers.GenerateCommand;
import edu.taskmanager.frontend.console.handlers.SortingCommand;
import java.util.List;

public class ResultSavingDecorator implements Command {
    private final Command wrapped;
    private ServisRepository servisRepository;


    public ResultSavingDecorator(Command wrapped,ServisRepository servisRepository) {
        this.servisRepository=servisRepository;
        this.wrapped = wrapped;
    }

    @Override
    public void execute(List<String> args) {
        // Могу сделать что-то до
        wrapped.execute(args);

        if (wrapped instanceof SortingCommand command) {
            List<Task> sortedTasks = command.getLastResult();
            saveResult(sortedTasks);
        } else if (wrapped instanceof FilterCommand command){
            List<Task> sortedTasks = command.getLastResult();
            saveResult(sortedTasks);
        } else if (wrapped instanceof GenerateCommand command){
            List<Task> sortedTasks = command.getLastResult();
            saveResult(sortedTasks);
        }

    }

    private void saveResult(List<Task> tasks){

        for(Task task : tasks){
            servisRepository.taskRepo().save(new Task(task));
        }
        System.out.println("Результат добавлен в память" );

    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }
}
