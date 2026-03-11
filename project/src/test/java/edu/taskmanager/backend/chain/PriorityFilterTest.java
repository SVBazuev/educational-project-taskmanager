package edu.taskmanager.backend.chain;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;

class PriorityFilterTest {

    private PriorityFilter priorityFilter;
    private Task highPriorityTask;
    private Task mediumPriorityTask;
    private Task lowPriorityTask;
    private Task criticalPriorityTask;

    @BeforeEach
    void setUp() {
        // Создаем задачи с разными приоритетами
        highPriorityTask = createTask("High Priority Task", Priority.HIGH);
        mediumPriorityTask = createTask("Medium Priority Task", Priority.MEDIUM);
        lowPriorityTask = createTask("Low Priority Task", Priority.LOW);
        criticalPriorityTask = createTask("Critical Priority Task", Priority.CRITICAL);
    }

    private Task createTask(String title, Priority priority) {
        Task task = new Task(title, LocalDateTime.now(), null, priority, TaskStatus.TODO);
        task.setId((long) (Math.random() * 1000));
        return task;
    }

    @Test
    void filter_ShouldReturnTrue_WhenTaskHasRequiredPriority() {
        // Arrange
        priorityFilter = new PriorityFilter(Priority.HIGH);
        
        // Act & Assert
        assertTrue(priorityFilter.filter(highPriorityTask));
    }

    @Test
    void filter_ShouldReturnFalse_WhenTaskHasDifferentPriority() {
        // Arrange
        priorityFilter = new PriorityFilter(Priority.HIGH);
        
        // Act & Assert
        assertFalse(priorityFilter.filter(mediumPriorityTask));
        assertFalse(priorityFilter.filter(lowPriorityTask));
        assertFalse(priorityFilter.filter(criticalPriorityTask));
    }

    @Test
    void filter_ShouldReturnTrue_WhenTaskHasRequiredPriorityAndNextFilterPasses() {
        // Arrange
        priorityFilter = new PriorityFilter(Priority.HIGH);
        
        // Создаем заглушку следующего фильтра, который всегда возвращает true
        TaskFilter nextFilter = new TaskFilter() {
            @Override
            public boolean filter(Task task) {
                return true;
            }
            
            @Override
            public void setNext(TaskFilter next) {
                // Не требуется для теста
            }
        };
        
        priorityFilter.setNext(nextFilter);
        
        // Act & Assert
        assertTrue(priorityFilter.filter(highPriorityTask));
    }

    @Test
    void filter_ShouldReturnFalse_WhenTaskHasRequiredPriorityButNextFilterFails() {
        // Arrange
        priorityFilter = new PriorityFilter(Priority.HIGH);
        
        // Создаем заглушку следующего фильтра, который всегда возвращает false
        TaskFilter nextFilter = new TaskFilter() {
            @Override
            public boolean filter(Task task) {
                return false;
            }
            
            @Override
            public void setNext(TaskFilter next) {
                // Не требуется для теста
            }
        };
        
        priorityFilter.setNext(nextFilter);
        
        // Act & Assert
        assertFalse(priorityFilter.filter(highPriorityTask));
    }

    @Test
    void filter_ShouldReturnFalse_WhenTaskHasDifferentPriorityEvenIfNextFilterPasses() {
        // Arrange
        priorityFilter = new PriorityFilter(Priority.HIGH);
        
        TaskFilter nextFilter = new TaskFilter() {
            @Override
            public boolean filter(Task task) {
                return true; // Следующий фильтр пропустит, но до него не дойдет
            }
            
            @Override
            public void setNext(TaskFilter next) {}
        };
        
        priorityFilter.setNext(nextFilter);
        
        // Act & Assert
        assertFalse(priorityFilter.filter(mediumPriorityTask)); // Должно быть false, так как приоритет не совпадает
    }

    @Test
    void filter_ShouldWorkWithAllPriorityValues() {
        // Проверяем все возможные значения приоритета
        testPriorityValue(Priority.CRITICAL, criticalPriorityTask, highPriorityTask);
        testPriorityValue(Priority.HIGH, highPriorityTask, mediumPriorityTask);
        testPriorityValue(Priority.MEDIUM, mediumPriorityTask, lowPriorityTask);
        testPriorityValue(Priority.LOW, lowPriorityTask, criticalPriorityTask);
    }
    
    private void testPriorityValue(Priority targetPriority, Task matchingTask, Task nonMatchingTask) {
        PriorityFilter filter = new PriorityFilter(targetPriority);
        
        assertTrue(filter.filter(matchingTask), 
            "Должно быть true для задачи с приоритетом " + targetPriority);
        assertFalse(filter.filter(nonMatchingTask), 
            "Должно быть false для задачи с другим приоритетом");
    }

    @Test
    void filter_ShouldHandleNullTask() {
        // Arrange
        priorityFilter = new PriorityFilter(Priority.HIGH);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            priorityFilter.filter(null);
        });
    }

    
    @Test
    void filter_WithChainOfFilters_AllMustPass() {
        // Arrange
        PriorityFilter highFilter = new PriorityFilter(Priority.HIGH);
        PriorityFilter mediumFilter = new PriorityFilter(Priority.MEDIUM);
        
        // Строим цепочку: high -> medium
        highFilter.setNext(mediumFilter);
        
        // Act & Assert
        // Задача с HIGH приоритетом не пройдет через mediumFilter
        assertFalse(highFilter.filter(highPriorityTask));
        
        // Задача с MEDIUM приоритетом не пройдет через highFilter
        assertFalse(highFilter.filter(mediumPriorityTask));
        
        // Задача с CRITICAL приоритетом не пройдет через highFilter
        assertFalse(highFilter.filter(criticalPriorityTask));
    }

    @Test
    void filter_WithChainOfFilters_AllPassWhenConditionsMet() {
        // Arrange
        PriorityFilter highFilter = new PriorityFilter(Priority.HIGH);
        PriorityFilter mediumFilter = new PriorityFilter(Priority.MEDIUM);
        
        // Такая цепочка логически не имеет смысла (задача не может иметь два приоритета),
        // но проверяем работу самого механизма
        highFilter.setNext(mediumFilter);
        
        // Создаем задачу, которая удовлетворяет всем фильтрам в цепочке
        Task customTask = new Task() {
            @Override
            public Priority getPriority() {
                // Этот метод будет вызываться дважды
                return Priority.HIGH; // Первый фильтр пройдет
                // Второй фильтр ожидает MEDIUM, но не получит его
            }
        };
        
        // Для реальной проверки создадим цепочку с разными типами фильтров
        // Создадим заглушку, которая всегда true
        TaskFilter alwaysTrueFilter = new TaskFilter() {
            @Override
            public boolean filter(Task task) {
                return true;
            }
            
            @Override
            public void setNext(TaskFilter next) {}
        };
        
        highFilter.setNext(alwaysTrueFilter);
        
        // Act & Assert
        assertTrue(highFilter.filter(highPriorityTask));
    }
}