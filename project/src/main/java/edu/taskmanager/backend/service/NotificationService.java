package edu.taskmanager.backend.service;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.strategy.notification.NotificationStrategy;

/**
 * Сервис для отправки уведомлений о задачах.
 * Использует стратегию уведомлений, которую можно сменить динамически.
 */
public class NotificationService {
    private NotificationStrategy notificationStrategy;

    public void setNotificationStrategy(NotificationStrategy strategy) {
        this.notificationStrategy = strategy;
    }

    public void notifyAboutTask(Task task) {
        if (notificationStrategy != null) {
            notificationStrategy.notify(task);
        } else {
            System.out.println("NotificationService: стратегия не задана, уведомление не отправлено.");
        }
    }
}
