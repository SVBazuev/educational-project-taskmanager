package edu.taskmanager.backend.repository;

import edu.taskmanager.backend.model.Task;
import edu.taskmanager.backend.util.Priority;
import edu.taskmanager.backend.util.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // save()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("save(): новая задача (id == null) получает сгенерированный id")
    void save_newTask_idIsGenerated() {
        Task saved = repository.save(task("First"));

        assertThat(saved.getId()).isNotNull().isPositive();
    }

    @Test
    @DisplayName("save(): новая задача — title сохраняется без изменений")
    void save_newTask_titleIsPreserved() {
        Task saved = repository.save(task("My Task"));

        assertThat(saved.getTitle()).isEqualTo("My Task");
    }

    @Test
    @DisplayName("save(): две новые задачи получают разные id")
    void save_twoNewTasks_getDistinctIds() {
        Task t1 = repository.save(task("First"));
        Task t2 = repository.save(task("Second"));

        assertThat(t1.getId()).isNotEqualTo(t2.getId());
    }

    @Test
    @DisplayName("save(): id генерируется монотонно возрастающим")
    void save_multipleNewTasks_idsAreIncremental() {
        Task t1 = repository.save(task("T1"));
        Task t2 = repository.save(task("T2"));
        Task t3 = repository.save(task("T3"));

        assertThat(t2.getId()).isGreaterThan(t1.getId());
        assertThat(t3.getId()).isGreaterThan(t2.getId());
    }

    @Test
    @DisplayName("save(): задача с существующим id — перезаписывает данные в хранилище")
    void save_existingTask_updatesStoredData() {
        Task saved = repository.save(task("Original"));
        saved.setTitle("Updated");

        Task updated = repository.save(saved);

        assertThat(updated.getTitle()).isEqualTo("Updated");
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("save(): задача с существующим id — id не меняется")
    void save_existingTask_idIsUnchanged() {
        Task saved = repository.save(task("Task"));
        Long originalId = saved.getId();
        saved.setTitle("Modified");

        Task updated = repository.save(saved);

        assertThat(updated.getId()).isEqualTo(originalId);
    }

    @Test
    @DisplayName("save(): мутация возвращённого объекта не изменяет данные в хранилище")
    void save_mutatingReturnedTask_doesNotCorruptStorage() {
        Task saved = repository.save(task("Stable"));

        saved.setTitle("Mutated");

        assertThat(repository.findById(saved.getId()))
                .isPresent()
                .get().extracting(Task::getTitle).isEqualTo("Stable");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // findById()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findById(): существующий id — возвращает задачу")
    void findById_existingId_returnsTask() {
        Task saved = repository.save(task("Find Me"));

        Optional<Task> result = repository.findById(saved.getId());

        assertThat(result).isPresent()
                .get().extracting(Task::getTitle).isEqualTo("Find Me");
    }

    @Test
    @DisplayName("findById(): несуществующий id — возвращает empty")
    void findById_unknownId_returnsEmpty() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    @DisplayName("findById(): после удаления — возвращает empty")
    void findById_afterDelete_returnsEmpty() {
        Task saved = repository.save(task("Temp"));
        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("findById(): возвращает копию — мутация результата не меняет хранилище")
    void findById_mutatingResult_doesNotCorruptStorage() {
        Task saved = repository.save(task("Stable"));

        repository.findById(saved.getId()).ifPresent(t -> t.setTitle("Changed"));

        assertThat(repository.findById(saved.getId()))
                .isPresent()
                .get().extracting(Task::getTitle).isEqualTo("Stable");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // findAll()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll(): пустой репозиторий — возвращает пустой список")
    void findAll_emptyRepository_returnsEmptyList() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("findAll(): возвращает все сохранённые задачи")
    void findAll_multipleTasks_returnsAll() {
        repository.save(task("T1"));
        repository.save(task("T2"));
        repository.save(task("T3"));

        assertThat(repository.findAll())
                .hasSize(3)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("T1", "T2", "T3");
    }

    @Test
    @DisplayName("findAll(): после удаления одной задачи — не содержит удалённую")
    void findAll_afterDelete_doesNotContainDeleted() {
        repository.save(task("Keep"));
        Task toDelete = repository.save(task("Remove"));

        repository.deleteById(toDelete.getId());

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Task::getTitle)
                .containsExactly("Keep");
    }

    @Test
    @DisplayName("findAll(): возвращает копии — мутация элементов не меняет хранилище")
    void findAll_mutatingResults_doesNotCorruptStorage() {
        repository.save(task("Stable"));

        repository.findAll().forEach(t -> t.setTitle("Changed"));

        assertThat(repository.findAll())
                .extracting(Task::getTitle)
                .containsExactly("Stable");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // deleteById()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteById(): существующий id — задача удаляется")
    void deleteById_existingId_taskRemoved() {
        Task saved = repository.save(task("ToDelete"));

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("deleteById(): несуществующий id — не бросает исключение, хранилище не меняется")
    void deleteById_unknownId_noExceptionThrown() {
        repository.save(task("Safe"));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                () -> repository.deleteById(999L)
        );
        assertThat(repository.findAll()).hasSize(1);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // findSubtasksByParentId()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findSubtasksByParentId(): возвращает только подзадачи с нужным parentId")
    void findSubtasksByParentId_returnsOnlyMatchingSubtasks() {
        Task parent1 = repository.save(task("Parent 1"));
        Task parent2 = repository.save(task("Parent 2"));

        Task sub1 = taskWithParent("Sub 1-1", parent1.getId());
        Task sub2 = taskWithParent("Sub 1-2", parent1.getId());
        Task sub3 = taskWithParent("Sub 2-1", parent2.getId());
        repository.save(sub1);
        repository.save(sub2);
        repository.save(sub3);

        List<Task> result = repository.findSubtasksByParentId(parent1.getId());

        assertThat(result)
                .hasSize(2)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Sub 1-1", "Sub 1-2");
    }

    @Test
    @DisplayName("findSubtasksByParentId(): у родителя нет подзадач — возвращает пустой список")
    void findSubtasksByParentId_noSubtasks_returnsEmpty() {
        Task parent = repository.save(task("Lonely Parent"));

        assertThat(repository.findSubtasksByParentId(parent.getId())).isEmpty();
    }

    @Test
    @DisplayName("findSubtasksByParentId(): несуществующий parentId — возвращает пустой список")
    void findSubtasksByParentId_unknownParentId_returnsEmpty() {
        repository.save(task("Task"));

        assertThat(repository.findSubtasksByParentId(999L)).isEmpty();
    }

    @Test
    @DisplayName("findSubtasksByParentId(): задачи без parentId не попадают в результат")
    void findSubtasksByParentId_tasksWithoutParent_notIncluded() {
        Task parent = repository.save(task("Parent"));
        repository.save(task("Top-level task")); // parentId == null

        Task sub = taskWithParent("Sub", parent.getId());
        repository.save(sub);

        List<Task> result = repository.findSubtasksByParentId(parent.getId());

        assertThat(result)
                .hasSize(1)
                .extracting(Task::getTitle)
                .containsExactly("Sub");
    }

    @Test
    @DisplayName("findSubtasksByParentId(): после удаления подзадачи — не возвращает её")
    void findSubtasksByParentId_afterDeleteSubtask_notReturned() {
        Task parent = repository.save(task("Parent"));
        Task sub = repository.save(taskWithParent("Sub", parent.getId()));

        repository.deleteById(sub.getId());

        assertThat(repository.findSubtasksByParentId(parent.getId())).isEmpty();
    }

    @Test
    @DisplayName("findSubtasksByParentId(): возвращает копии — мутация результата не меняет хранилище")
    void findSubtasksByParentId_mutatingResults_doesNotCorruptStorage() {
        Task parent = repository.save(task("Parent"));
        repository.save(taskWithParent("Sub", parent.getId()));

        repository.findSubtasksByParentId(parent.getId())
                .forEach(t -> t.setTitle("Changed"));

        assertThat(repository.findSubtasksByParentId(parent.getId()))
                .extracting(Task::getTitle)
                .containsExactly("Sub");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Вспомогательные методы
    // ──────────────────────────────────────────────────────────────────────────

    private Task task(String title) {
        return new Task(title, LocalDateTime.now(), null, Priority.MEDIUM, TaskStatus.TODO);
    }

    private Task taskWithParent(String title, Long parentId) {
        Task t = task(title);
        t.setParentId(parentId);
        return t;
    }
}

