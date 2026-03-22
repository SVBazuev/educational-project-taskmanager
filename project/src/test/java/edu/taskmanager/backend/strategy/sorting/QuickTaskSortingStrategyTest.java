package edu.taskmanager.backend.strategy.sorting;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuickTaskSortingStrategyTest {

@Spy
private QuickTaskSortingStrategy strategy;

private Task taskLow;
private Task taskMedium;
private Task taskHigh;
private Task taskCritical;

@BeforeEach
void setUp() {
taskLow = createTask(1L, "Low task", Priority.LOW);
taskMedium = createTask(2L, "Medium task", Priority.MEDIUM);
taskHigh = createTask(3L, "High task", Priority.HIGH);
taskCritical = createTask(4L, "Critical task", Priority.CRITICAL);
}

@Test
@DisplayName("sort(): возвращает список, отсортированный по убыванию приоритета")
void sort_returnsSortedByPriorityDescending() {
List<Task> input = List.of(taskLow, taskCritical, taskMedium, taskHigh);

List<Task> result = strategy.sort(input, Comparator.comparing(Task::getPriority).reversed());

assertThat(result)
.extracting(Task::getPriority)
.containsExactly(
Priority.CRITICAL, Priority.HIGH, Priority.MEDIUM, Priority.LOW
);
}

@Test
@DisplayName("sort(): не изменяет исходный список")
void sort_doesNotMutateOriginalList() {
List<Task> original = new ArrayList<>(List.of(taskLow, taskCritical, taskMedium));
List<Task> copy = new ArrayList<>(original);

strategy.sort(original, Comparator.comparing(Task::getPriority));

assertThat(original).isEqualTo(copy);
}

@Test
@DisplayName("sort(): возвращает пустой список при пустом входе")
void sort_emptyList_returnsEmpty() {
List<Task> result = strategy.sort(new ArrayList<>(), Comparator.comparing(Task::getPriority));

assertThat(result).isEmpty();
}

@Test
@DisplayName("sort(): возвращает список из одного элемента без изменений")
void sort_singleElement_returnsSameElement() {
List<Task> result = strategy.sort(List.of(taskHigh), Comparator.comparing(Task::getPriority));

assertThat(result)
.hasSize(1)
.first()
.extracting(Task::getPriority)
.isEqualTo(Priority.HIGH);
}

@Test
@DisplayName("sort(): задачи с одинаковым приоритетом все присутствуют в результате")
void sort_equalPriorities_allTasksPresent() {
Task t1 = createTask(10L, "A", Priority.MEDIUM);
Task t2 = createTask(11L, "B", Priority.MEDIUM);
Task t3 = createTask(12L, "C", Priority.MEDIUM);
List<Task> input = List.of(t1, t2, t3);

List<Task> result = strategy.sort(
input, Comparator.comparing(Task::getPriority)
);

assertThat(result)
.hasSize(3)
.extracting(Task::getPriority)
.containsOnly(Priority.MEDIUM);
}

@Test
@DisplayName("sort(): список, уже отсортированный по убыванию, остаётся верным")
void sort_alreadySortedDescending_remainsCorrect() {
List<Task> input = List.of(taskCritical, taskHigh, taskMedium, taskLow);

List<Task> result = strategy.sort(
input, Comparator.comparing(Task::getPriority).reversed()
);

assertThat(result)
.extracting(Task::getPriority)
.containsExactly(
Priority.CRITICAL, Priority.HIGH, Priority.MEDIUM, Priority.LOW
);
}

@Test
@DisplayName("sort(): список в обратном порядке (возрастание) сортируется правильно")
void sort_reversedInput_sortedCorrectly() {
List<Task> input = List.of(taskLow, taskMedium, taskHigh, taskCritical);

List<Task> result = strategy.sort(input, Comparator.comparing(Task::getPriority).reversed());

assertThat(result)
.extracting(Task::getPriority)
.containsExactly(
Priority.CRITICAL, Priority.HIGH, Priority.MEDIUM, Priority.LOW
);
}

@Test
@DisplayName("sort(): результат не содержит null-элементов")
void sort_resultContainsNoNulls() {
List<Task> input = List.of(taskLow, taskHigh, taskCritical, taskMedium);

List<Task> result = strategy.sort(
input, Comparator.comparing(Task::getPriority)
);

assertThat(result).doesNotContainNull();
}

@Test
@DisplayName("sort(): Mockito проверяет, что метод sort() был вызван ровно один раз")
void sort_verifyMethodInvokedOnce() {
List<Task> input = List.of(taskLow, taskHigh);
Comparator<Task> comparator = Comparator.comparing(Task::getPriority);

strategy.sort(input, comparator);

verify(strategy, times(1)).sort(input, comparator);
}

@Test
@DisplayName("sort(): сортировка по убыванию приоритета через компаратор")
void sort_withDescendingComparator_sortedCorrectly() {
List<Task> input = List.of(taskLow, taskCritical, taskMedium, taskHigh);
Comparator<Task> comparator = Comparator
.comparing(Task::getPriority).reversed();

List<Task> result = strategy.sort(input, comparator);

assertThat(result)
.extracting(Task::getPriority)
.containsExactly(
Priority.CRITICAL, Priority.HIGH, Priority.MEDIUM, Priority.LOW
);
}

@Test
@DisplayName("sort(): сортировка по возрастанию приоритета через компаратор")
void sort_withAscendingComparator_sortedCorrectly() {
List<Task> input = List.of(taskCritical, taskLow, taskHigh, taskMedium);
Comparator<Task> comparator = Comparator.comparing(Task::getPriority);

List<Task> result = strategy.sort(input, comparator);

assertThat(result)
.extracting(Task::getPriority)
.containsExactly(
Priority.LOW, Priority.MEDIUM, Priority.HIGH, Priority.CRITICAL
);
}

@Test
@DisplayName("sort(): сортировка по title через компаратор")
void sort_withTitleComparator_sortedAlphabetically() {
Task tA = createTask(20L, "Apple", Priority.LOW);
Task tB = createTask(21L, "Banana", Priority.LOW);
Task tC = createTask(22L, "Cherry", Priority.LOW);
List<Task> input = List.of(tC, tA, tB);
Comparator<Task> comparator = Comparator.comparing(Task::getTitle);

List<Task> result = strategy.sort(input, comparator);

assertThat(result)
.extracting(Task::getTitle)
.containsExactly("Apple", "Banana", "Cherry");
}

@Test
@DisplayName("sort(): пустой список возвращается без изменений")
void sort_emptyList_returnsEmpty2() {
List<Task> result = strategy.sort(new ArrayList<>(), Comparator.comparing(Task::getPriority));

assertThat(result).isEmpty();
}

@Test
@DisplayName("sort(): список из одного элемента возвращается без изменений")
void sort_singleElement_returnsSingleElement2() {
List<Task> result = strategy.sort(List.of(taskMedium), Comparator.comparing(Task::getPriority));

assertThat(result)
.hasSize(1)
.first()
.extracting(Task::getPriority)
.isEqualTo(Priority.MEDIUM);
}

@Test
@DisplayName("sort(): исходный список не изменяется после сортировки")
void sort_doesNotMutateOriginalList2() {
List<Task> original = new ArrayList<>(List.of(taskCritical, taskLow, taskMedium));
List<Task> copy = new ArrayList<>(original);

strategy.sort(original, Comparator.comparing(Task::getPriority));

assertThat(original).isEqualTo(copy);
}

@Test
@DisplayName("sort(): возвращаемый список содержит те же объекты, что и исходный")
void sort_resultContainsSameTaskObjects() {
List<Task> input = List.of(taskLow, taskHigh, taskCritical);

List<Task> result = strategy.sort(
input, Comparator.comparing(Task::getPriority)
);

assertThat(result).containsExactlyInAnyOrder(
taskLow, taskHigh, taskCritical
);
}

@Test
@DisplayName("sort(): большой список сортируется корректно")
void sort_largeList_sortedCorrectly() {
Priority[] priorities = Priority.values(); // LOW, MEDIUM, HIGH, CRITICAL
List<Task> input = new ArrayList<>();
for (int i = 0; i < 100; i++) {
input.add(
createTask(
(long) i, "Task " + i, priorities[i % priorities.length]
)
);
}

Comparator<Task> comparator = Comparator
.comparing(Task::getPriority).reversed();
List<Task> result = strategy.sort(input, comparator);

assertThat(result).hasSize(100);
for (int i = 0; i < result.size() - 1; i++) {
assertThat(result.get(i).getPriority().ordinal())
.isGreaterThanOrEqualTo(result.get(i + 1).getPriority().ordinal());
}
}

private Task createTask(Long id, String title, Priority priority) {
Task task = new Task(
title, LocalDateTime.now(), null, priority, TaskStatus.TODO
);
task.setId(id);
return task;
}
}
