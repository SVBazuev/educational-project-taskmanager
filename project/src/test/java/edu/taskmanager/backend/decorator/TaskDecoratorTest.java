package edu.taskmanager.backend.decorator;

import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.model.User;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Да, хоть это и абстрактный класс, его можно протестировать, создав конкретную реализацию для тестов.
 * Проверяем, что декоратор корректно оборачивает объект Task и делегирует все методы к wrappedTask.
 */

class TaskDecoratorTest {

    /** Конкретная реализация абстрактного декоратора для тестирования */
    static class ConcreteTaskDecorator extends TaskDecorator {
        ConcreteTaskDecorator(Task task) {
            super(task);
        }
    }

    private Task task;
    private TaskDecorator decorator;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(42L);
        task.setTitle("Test task");
        task.setDescription("Some description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(Priority.HIGH);

        LocalDateTime now = LocalDateTime.of(2026, 3, 11, 12, 0);
        task.setCreatedAt(now);
        task.setDueDate(now.plusDays(3));

        User user = new User();
        user.setId(1L);
        task.setCreator(user);

        Project project = new Project();
        project.setId(10L);
        task.setProject(project);

        decorator = new ConcreteTaskDecorator(task);
    }

    // --- Конструктор ---

    @Test
    void constructor_nullTask_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new ConcreteTaskDecorator(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task cannot be null");
    }

    @Test
    void constructor_validTask_wrapsSuccessfully() {
        assertThat(decorator.getWrappedTask()).isSameAs(task);
    }

    // --- Геттеры делегируют к wrappedTask ---

    @Test
    void getId() {
        assertThat(decorator.getId()).isEqualTo(42L);
    }

    @Test
    void getTitle() {
        assertThat(decorator.getTitle()).isEqualTo("Test task");
    }

    @Test
    void getDescription() {
        assertThat(decorator.getDescription()).isEqualTo("Some description");
    }

    @Test
    void getStatus() {
        assertThat(decorator.getStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void getPriority() {
        assertThat(decorator.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void getCreatedAt() {
        assertThat(decorator.getCreatedAt()).isEqualTo(task.getCreatedAt());
    }

    @Test
    void getDueDate() {
        assertThat(decorator.getDueDate()).isEqualTo(task.getDueDate());
    }

    @Test
    void getCreator() {
        assertThat(decorator.getCreator()).isSameAs(task.getCreator());
    }

    @Test
    void getProject() {
        assertThat(decorator.getProject()).isSameAs(task.getProject());
    }

    @Test
    void getWrappedTask() {
        assertThat(decorator.getWrappedTask()).isSameAs(task);
    }

    // --- Сеттеры делегируют к wrappedTask ---

    @Test
    void setId() {
        decorator.setId(99L);
        assertThat(task.getId()).isEqualTo(99L);
    }

    @Test
    void setTitle() {
        decorator.setTitle("New title");
        assertThat(task.getTitle()).isEqualTo("New title");
    }

    @Test
    void setDescription() {
        decorator.setDescription("New description");
        assertThat(task.getDescription()).isEqualTo("New description");
    }

    @Test
    void setStatus() {
        decorator.setStatus(TaskStatus.IN_PROGRESS);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void setPriority() {
        decorator.setPriority(Priority.LOW);
        assertThat(task.getPriority()).isEqualTo(Priority.LOW);
    }

    @Test
    void setDueDate() {
        LocalDateTime newDate = LocalDateTime.of(2026, 6, 1, 0, 0);
        decorator.setDueDate(newDate);
        assertThat(task.getDueDate()).isEqualTo(newDate);
    }

    @Test
    void setCreator() {
        User newUser = new User();
        newUser.setId(99L);
        decorator.setCreator(newUser);
        assertThat(task.getCreator()).isSameAs(newUser);
    }

    @Test
    void setProjectId() {
        Project newProject = new Project();
        newProject.setId(55L);
        decorator.setProjectId(newProject);
        assertThat(task.getProject()).isSameAs(newProject);
    }
}