package edu.taskmanager.backend.strategy.notification;

import edu.taskmanager.backend.model.Task;

public class EmailNotificationStrategy implements NotificationStrategy {
    private final String email;

    public EmailNotificationStrategy(String email) {
        this.email = email;
    }

    @Override
    public void notify(Task task) {
        System.out.println("[EMAIL] Уведомление на " + email + " о задаче: " + task.getTitle());
    }
}
