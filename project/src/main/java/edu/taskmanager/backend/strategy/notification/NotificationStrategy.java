package edu.taskmanager.backend.strategy.notification;

import edu.taskmanager.backend.model.Task;

/**
 * Стратегия отправки уведомления о задаче.
 */
public interface NotificationStrategy {
    void notify(Task task);
}
