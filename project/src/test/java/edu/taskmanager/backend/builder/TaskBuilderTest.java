package edu.taskmanager.backend.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.taskmanager.backend.model.Project;
import edu.taskmanager.backend.model.Tag;
import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;

class TaskBuilderTest {

    private TaskBuilder taskBuilder;
    private LocalDateTime testDueDate;
    private Project testProject;
    private Tag testTag1;
    private Tag testTag2;
    private Task testSubtask1;
    private Task testSubtask2;

    @BeforeEach
    void setUp() {
        taskBuilder = new TaskBuilder();
        testDueDate = LocalDateTime.now().plusDays(7);
        
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");
        
        testTag1 = new Tag();
        testTag1.setId(1L);
        testTag1.setName("urgent");
        
        testTag2 = new Tag();
        testTag2.setId(2L);
        testTag2.setName("work");
        
        testSubtask1 = new Task();
        testSubtask1.setId(100L);
        testSubtask1.setTitle("Subtask 1");
        
        testSubtask2 = new Task();
        testSubtask2.setId(101L);
        testSubtask2.setTitle("Subtask 2");
    }

    // ============== ТЕСТЫ ДЛЯ УСТАНОВКИ ПОЛЕЙ ==============

    @Test
    void setTitle_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.setTitle("Test Task");
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setDescription_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.setDescription("Test Description");
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setDueDate_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.setDueDate(testDueDate);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setPriority_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.setPriority(Priority.HIGH);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setStatus_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.setStatus(TaskStatus.IN_PROGRESS);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setProject_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.setProject(testProject);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setTags_ShouldReturnSameBuilder() {
        // Arrange
        List<Tag> tags = Arrays.asList(testTag1, testTag2);
        
        // Act
        TaskBuilder result = taskBuilder.setTags(tags);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void addTag_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.addTag(testTag1);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setSubtasks_ShouldReturnSameBuilder() {
        // Arrange
        List<Task> subtasks = Arrays.asList(testSubtask1, testSubtask2);
        
        // Act
        TaskBuilder result = taskBuilder.setSubtasks(subtasks);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void addSubtask_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.addSubtask(testSubtask1);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    @Test
    void setParentId_ShouldReturnSameBuilder() {
        // Act
        TaskBuilder result = taskBuilder.setParentId(5L);
        
        // Assert
        assertSame(taskBuilder, result);
    }

    // ============== ТЕСТЫ ДЛЯ ПРОВЕРКИ ЗНАЧЕНИЙ ПО УМОЛЧАНИЮ ==============
/*
    @Test
    void build_WithOnlyRequiredFields_ShouldSetDefaultValues() {
        // Arrange
        Task task = taskBuilder
            .setTitle("Test Task")
            .setDueDate(testDueDate)
            .build();
        
        // Assert
        assertEquals("Test Task", task.getTitle());
        assertEquals(testDueDate, task.getDueDate());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNull(task.getDescription());
        assertNull(task.getProject());
        assertTrue(task.getTags().isEmpty());
        assertTrue(task.getSubtasks().isEmpty());
        assertNull(task.getParentId());
        assertNotNull(task.getCreatedAt());
        assertNull(task.getUpdatedAt());
    }
*/
    // ============== ТЕСТЫ ДЛЯ ПРОВЕРКИ УСТАНОВЛЕННЫХ ЗНАЧЕНИЙ ==============

    @Test
    void build_WithAllFieldsSet_ShouldCreateTaskWithCorrectValues() {
        // Arrange
        List<Tag> tags = new ArrayList<>(Arrays.asList(testTag1, testTag2)); // ИСПРАВЛЕНО
        List<Task> subtasks = new ArrayList<>(Arrays.asList(testSubtask1, testSubtask2)); // ИСПРАВЛЕНО
        
        // Act
        Task task = taskBuilder
            .setTitle("Complete Task")
            .setDescription("This is a complete task description")
            .setDueDate(testDueDate)
            .setPriority(Priority.CRITICAL)
            .setStatus(TaskStatus.IN_PROGRESS)
            .setProject(testProject)
            .setTags(tags)
            .addTag(testTag1) // Добавляем еще один тег (дубликат не должен добавиться)
            .setSubtasks(subtasks)
            .addSubtask(testSubtask1) // Добавляем еще одну подзадачу (дубликат)
            .setParentId(42L)
            .build();
        
        // Assert
        assertEquals("Complete Task", task.getTitle());
        assertEquals("This is a complete task description", task.getDescription());
        assertEquals(testDueDate, task.getDueDate());
        assertEquals(Priority.CRITICAL, task.getPriority());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(testProject, task.getProject());
        assertEquals(2, task.getTags().size());
        assertTrue(task.getTags().contains(testTag1));
        assertTrue(task.getTags().contains(testTag2));
        assertEquals(2, task.getSubtasks().size());
        assertTrue(task.getSubtasks().contains(testSubtask1));
        assertTrue(task.getSubtasks().contains(testSubtask2));
        assertEquals(42L, task.getParentId());
        assertNotNull(task.getCreatedAt());
    }

    @Test
    void build_WithNullTagsList_ShouldSetEmptyTags() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with null tags")
            .setDueDate(testDueDate)
            .setTags(null)
            .build();
        
        // Assert
        assertNotNull(task.getTags());
        assertTrue(task.getTags().isEmpty());
    }

    @Test
    void build_WithNullSubtasksList_ShouldSetEmptySubtasks() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with null subtasks")
            .setDueDate(testDueDate)
            .setSubtasks(null)
            .build();
        
        // Assert
        assertNotNull(task.getSubtasks());
        assertTrue(task.getSubtasks().isEmpty());
    }

    // ============== ТЕСТЫ ДЛЯ МЕТОДОВ ADD ==============

    @Test
    void addTag_ShouldAddTagToCollection() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with tags")
            .setDueDate(testDueDate)
            .addTag(testTag1)
            .addTag(testTag2)
            .build();
        
        // Assert
        assertEquals(2, task.getTags().size());
        assertTrue(task.getTags().contains(testTag1));
        assertTrue(task.getTags().contains(testTag2));
    }

    @Test
    void addTag_WithNullTag_ShouldNotAdd() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with null tag")
            .setDueDate(testDueDate)
            .addTag(testTag1)
            .addTag(null)
            .build();
        
        // Assert
        assertEquals(1, task.getTags().size());
        assertTrue(task.getTags().contains(testTag1));
    }

    @Test
    void addTag_WithDuplicateTag_ShouldNotAddDuplicate() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with duplicate tag")
            .setDueDate(testDueDate)
            .addTag(testTag1)
            .addTag(testTag1) // Дубликат
            .build();
        
        // Assert
        assertEquals(1, task.getTags().size());
    }

    @Test
    void addSubtask_ShouldAddSubtaskToCollection() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with subtasks")
            .setDueDate(testDueDate)
            .addSubtask(testSubtask1)
            .addSubtask(testSubtask2)
            .build();
        
        // Assert
        assertEquals(2, task.getSubtasks().size());
        assertTrue(task.getSubtasks().contains(testSubtask1));
        assertTrue(task.getSubtasks().contains(testSubtask2));
    }

    @Test
    void addSubtask_WithNullSubtask_ShouldNotAdd() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with null subtask")
            .setDueDate(testDueDate)
            .addSubtask(testSubtask1)
            .addSubtask(null)
            .build();
        
        // Assert
        assertEquals(1, task.getSubtasks().size());
        assertTrue(task.getSubtasks().contains(testSubtask1));
    }

    @Test
    void addSubtask_WithDuplicateSubtask_ShouldNotAddDuplicate() {
        // Act
        Task task = taskBuilder
            .setTitle("Task with duplicate subtask")
            .setDueDate(testDueDate)
            .addSubtask(testSubtask1)
            .addSubtask(testSubtask1) // Дубликат
            .build();
        
        // Assert
        assertEquals(1, task.getSubtasks().size());
    }

    // ============== ТЕСТЫ ДЛЯ ПРОВЕРКИ ОБЯЗАТЕЛЬНЫХ ПОЛЕЙ ==============

    @Test
    void build_WithNullTitle_ShouldThrowException() {
        // Arrange
        taskBuilder.setDueDate(testDueDate);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> taskBuilder.build());
        
        assertTrue(exception.getMessage().contains("Название задачи"));
    }

    @Test
    void build_WithEmptyTitle_ShouldThrowException() {
        // Arrange
        taskBuilder
            .setTitle("   ")
            .setDueDate(testDueDate);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> taskBuilder.build());
        
        assertTrue(exception.getMessage().contains("Название задачи"));
    }

    @Test
    void build_WithNullDueDate_ShouldThrowException() {
        // Arrange
        taskBuilder.setTitle("Task without due date");
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> taskBuilder.build());
        
        assertTrue(exception.getMessage().contains("Срок выполнения"));
    }

    // ============== ТЕСТЫ ДЛЯ ГРАНИЧНЫХ СЛУЧАЕВ ==============

    @Test
    void build_WithVeryLongTitle_ShouldWork() {
        // Arrange
        String longTitle = "A".repeat(1000);
        
        // Act
        Task task = taskBuilder
            .setTitle(longTitle)
            .setDueDate(testDueDate)
            .build();
        
        // Assert
        assertEquals(longTitle, task.getTitle());
    }

    @Test
    void build_WithVeryLongDescription_ShouldWork() {
        // Arrange
        String longDescription = "B".repeat(10000);
        
        // Act
        Task task = taskBuilder
            .setTitle("Task with long description")
            .setDescription(longDescription)
            .setDueDate(testDueDate)
            .build();
        
        // Assert
        assertEquals(longDescription, task.getDescription());
    }

    @Test
    void build_WithPastDueDate_ShouldWork() {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.now().minusDays(5);
        
        // Act
        Task task = taskBuilder
            .setTitle("Past due task")
            .setDueDate(pastDate)
            .build();
        
        // Assert
        assertEquals(pastDate, task.getDueDate());
    }

    @Test
    void build_WithAllPriorityValues_ShouldWork() {
        // Проверяем все значения Priority
        for (Priority priority : Priority.values()) {
            Task task = taskBuilder
                .setTitle("Priority test: " + priority)
                .setDueDate(testDueDate)
                .setPriority(priority)
                .build();
            
            assertEquals(priority, task.getPriority());
        }
    }

    @Test
    void build_WithAllStatusValues_ShouldWork() {
        // Проверяем все значения TaskStatus
        for (TaskStatus status : TaskStatus.values()) {
            Task task = taskBuilder
                .setTitle("Status test: " + status)
                .setDueDate(testDueDate)
                .setStatus(status)
                .build();
            
            assertEquals(status, task.getStatus());
        }
    }

    // ============== ТЕСТЫ ДЛЯ ЦЕПОЧКИ ВЫЗОВОВ ==============

    @Test
    void builder_ShouldSupportMethodChaining() {
        // Act - строим сложную цепочку
        Task task = taskBuilder
            .setTitle("Chained Task")
            .setDescription("Built with chaining")
            .setDueDate(testDueDate)
            .setPriority(Priority.HIGH)
            .setStatus(TaskStatus.IN_PROGRESS)
            .setProject(testProject)
            .addTag(testTag1)
            .addTag(testTag2)
            .addSubtask(testSubtask1)
            .setParentId(99L)
            .build();
        
        // Assert
        assertNotNull(task);
        assertEquals("Chained Task", task.getTitle());
    }

    // ============== ТЕСТЫ ДЛЯ КОМБИНАЦИЙ set И add ==============

    @Test
    void setTags_And_AddTag_ShouldCombineCorrectly() {
        // Arrange
        List<Tag> initialTags = new ArrayList<>(); // ИСПРАВЛЕНО: создаем изменяемый список
        initialTags.add(testTag1);
        
        // Act
        Task task = taskBuilder
            .setTitle("Combined tags")
            .setDueDate(testDueDate)
            .setTags(initialTags)
            .addTag(testTag2)
            .build();
        
        // Assert
        assertEquals(2, task.getTags().size());
        assertTrue(task.getTags().contains(testTag1));
        assertTrue(task.getTags().contains(testTag2));
    }

    @Test
    void setTags_After_AddTag_ShouldReplaceAllTags() {
        // Act
        Task task = taskBuilder
            .setTitle("Replace tags")
            .setDueDate(testDueDate)
            .addTag(testTag1)
            .addTag(testTag2)
            .setTags(new ArrayList<>(Arrays.asList(testTag1))) // ИСПРАВЛЕНО: оборачиваем в ArrayList
            .build();
        
        // Assert
        assertEquals(1, task.getTags().size());
        assertTrue(task.getTags().contains(testTag1));
        assertFalse(task.getTags().contains(testTag2));
    }
}