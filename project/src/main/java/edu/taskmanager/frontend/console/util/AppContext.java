package edu.taskmanager.frontend.console.util;

import edu.taskmanager.backend.model.User;

/**
 * Контекст текущей сессии. Хранит аутентифицированного пользователя.
 */
public class AppContext {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void clear() {
        this.currentUser = null;
    }
}
