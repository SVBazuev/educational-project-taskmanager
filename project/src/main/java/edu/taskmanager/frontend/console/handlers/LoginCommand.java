package edu.taskmanager.frontend.console.handlers;

import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.repository.UserRepository;
import edu.taskmanager.frontend.console.Command;
import edu.taskmanager.frontend.console.util.AppContext;

import java.util.List;
import java.util.Optional;

public class LoginCommand implements Command {
    private final UserRepository userRepository;
    private final AppContext appContext;

    public LoginCommand(UserRepository userRepository, AppContext appContext) {
        this.userRepository = userRepository;
        this.appContext = appContext;
    }

    @Override
    public void execute(List<String> args) {
        if (args.size() < 2) {
            System.out.println("Использование: login <username> <password>");
            return;
        }
        String username = args.get(0);
        String password = args.get(1);

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            System.out.println("Пользователь не найден.");
            return;
        }
        User user = userOpt.get();
        // Временное сравнение паролей в открытом виде (для демо)
        if (!password.equals(user.getPasswordHash())) {
            System.out.println("Неверный пароль.");
            return;
        }
        appContext.setCurrentUser(user);
        System.out.println("Успешный вход. Добро пожаловать, " + user.getUsername() + "!");
    }

    @Override
    public String getDescription() {
        return "login <username> <password> - вход в систему";
    }
}
