package edu.taskmanager.backend.strategy.notification;

import edu.taskmanager.backend.model.Task;

public class PushNotificationStrategy implements NotificationStrategy {
    private final String deviceToken;

    public PushNotificationStrategy(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void notify(Task task) {
        System.out.println("[PUSH] Уведомление на устройство " + deviceToken + " о задаче: " + task.getTitle());
    }
}
