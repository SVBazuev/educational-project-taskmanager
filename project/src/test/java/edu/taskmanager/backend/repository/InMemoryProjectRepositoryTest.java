//package edu.taskmanager.backend.repository;
//
//import edu.taskmanager.backend.model.Project;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class InMemoryProjectRepositoryTest {
//
//    private InMemoryProjectRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        repository = new InMemoryProjectRepository();
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // save()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("save(): новый проект (id == null) получает сгенерированный id")
//    void save_newProject_idIsGenerated() {
//        Project project = new Project("Backend");
//
//        Project saved = repository.save(project);
//
//        assertThat(saved.getId()).isNotNull().isPositive();
//    }
//
//    @Test
//    @DisplayName("save(): новый проект — имя сохраняется без изменений")
//    void save_newProject_nameIsPreserved() {
//        Project project = new Project("Backend");
//
//        Project saved = repository.save(project);
//
//        assertThat(saved.getName()).isEqualTo("Backend");
//    }
//
//    @Test
//    @DisplayName("save(): два новых проекта получают разные id")
//    void save_twoNewProjects_getDistinctIds() {
//        Project p1 = repository.save(new Project("Alpha"));
//        Project p2 = repository.save(new Project("Beta"));
//
//        assertThat(p1.getId()).isNotEqualTo(p2.getId());
//    }
//
//    @Test
//    @DisplayName("save(): новый проект с уже существующим именем — возвращает существующий (id не меняется)")
//    void save_newProjectWithDuplicateName_returnsExisting() {
//        Project original = repository.save(new Project("Shared"));
//
//        Project duplicate = repository.save(new Project("Shared"));
//
//        assertThat(duplicate.getId()).isEqualTo(original.getId());
//        assertThat(repository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("save(): обновление проекта с тем же именем — данные перезаписываются")
//    void save_existingProjectSameName_updatesData() {
//        Project saved = repository.save(new Project("Backend"));
//        saved.setDescription("Описание бэкенда");
//
//        Project updated = repository.save(saved);
//
//        assertThat(updated.getDescription()).isEqualTo("Описание бэкенда");
//        assertThat(repository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("save(): обновление проекта с изменением имени — старое имя удаляется из индекса")
//    void save_existingProjectRenamedOldName_notFoundByOldName() {
//        Project saved = repository.save(new Project("OldName"));
//        saved.setName("NewName");
//
//        repository.save(saved);
//
//        assertThat(repository.findByName("OldName")).isEmpty();
//    }
//
//    @Test
//    @DisplayName("save(): обновление проекта с изменением имени — проект находится по новому имени")
//    void save_existingProjectRenamed_foundByNewName() {
//        Project saved = repository.save(new Project("OldName"));
//        saved.setName("NewName");
//
//        repository.save(saved);
//
//        assertThat(repository.findByName("NewName")).isPresent()
//                .get().extracting(Project::getId).isEqualTo(saved.getId());
//    }
//
//    @Test
//    @DisplayName("save(): мутация возвращённого объекта не ломает индекс имён")
//    void save_mutatingReturnedObject_doesNotCorruptNameIndex() {
//        Project saved = repository.save(new Project("Original"));
//
//        // Мутируем возвращённый объект напрямую, без повторного save()
//        saved.setName("Mutated");
//
//        // Индекс не должен был измениться — старое имя всё ещё в репозитории
//        assertThat(repository.findByName("Original")).isPresent();
//        assertThat(repository.findByName("Mutated")).isEmpty();
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // findById()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("findById(): существующий id — возвращает проект")
//    void findById_existingId_returnsProject() {
//        Project saved = repository.save(new Project("Frontend"));
//
//        Optional<Project> result = repository.findById(saved.getId());
//
//        assertThat(result).isPresent()
//                .get().extracting(Project::getName).isEqualTo("Frontend");
//    }
//
//    @Test
//    @DisplayName("findById(): несуществующий id — возвращает empty")
//    void findById_unknownId_returnsEmpty() {
//        Optional<Project> result = repository.findById(999L);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("findById(): после удаления — возвращает empty")
//    void findById_afterDelete_returnsEmpty() {
//        Project saved = repository.save(new Project("ToDelete"));
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findById(saved.getId())).isEmpty();
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // findByName()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("findByName(): существующее имя — возвращает проект")
//    void findByName_existingName_returnsProject() {
//        repository.save(new Project("DevOps"));
//
//        Optional<Project> result = repository.findByName("DevOps");
//
//        assertThat(result).isPresent()
//                .get().extracting(Project::getName).isEqualTo("DevOps");
//    }
//
//    @Test
//    @DisplayName("findByName(): несуществующее имя — возвращает empty")
//    void findByName_unknownName_returnsEmpty() {
//        Optional<Project> result = repository.findByName("NonExistent");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("findByName(): после удаления проекта — возвращает empty")
//    void findByName_afterDelete_returnsEmpty() {
//        Project saved = repository.save(new Project("Temp"));
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findByName("Temp")).isEmpty();
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // findAll()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("findAll(): пустой репозиторий — возвращает пустой список")
//    void findAll_emptyRepository_returnsEmptyList() {
//        assertThat(repository.findAll()).isEmpty();
//    }
//
//    @Test
//    @DisplayName("findAll(): возвращает все сохранённые проекты")
//    void findAll_multipleProjects_returnsAll() {
//        repository.save(new Project("Alpha"));
//        repository.save(new Project("Beta"));
//        repository.save(new Project("Gamma"));
//
//        List<Project> all = repository.findAll();
//
//        assertThat(all).hasSize(3)
//                .extracting(Project::getName)
//                .containsExactlyInAnyOrder("Alpha", "Beta", "Gamma");
//    }
//
//    @Test
//    @DisplayName("findAll(): после удаления одного проекта — возвращает оставшиеся")
//    void findAll_afterDelete_doesNotContainDeleted() {
//        repository.save(new Project("Keep"));
//        Project toDelete = repository.save(new Project("Remove"));
//
//        repository.deleteById(toDelete.getId());
//
//        assertThat(repository.findAll())
//                .hasSize(1)
//                .extracting(Project::getName)
//                .containsExactly("Keep");
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // deleteById()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("deleteById(): существующий id — проект удаляется из хранилища")
//    void deleteById_existingId_projectRemoved() {
//        Project saved = repository.save(new Project("ToRemove"));
//
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findById(saved.getId())).isEmpty();
//        assertThat(repository.findAll()).isEmpty();
//    }
//
//    @Test
//    @DisplayName("deleteById(): существующий id — имя удаляется из индекса имён")
//    void deleteById_existingId_nameRemovedFromIndex() {
//        Project saved = repository.save(new Project("IndexedName"));
//
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findByName("IndexedName")).isEmpty();
//    }
//
//    @Test
//    @DisplayName("deleteById(): несуществующий id — не бросает исключение")
//    void deleteById_unknownId_noExceptionThrown() {
//        repository.save(new Project("Safe"));
//
//        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
//                () -> repository.deleteById(999L)
//        );
//        assertThat(repository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("deleteById(): после удаления можно сохранить проект с тем же именем")
//    void deleteById_afterDelete_sameNameCanBeSavedAgain() {
//        Project first = repository.save(new Project("Reusable"));
//        repository.deleteById(first.getId());
//
//        Project second = repository.save(new Project("Reusable"));
//
//        assertThat(second.getId()).isNotNull();
//        assertThat(repository.findByName("Reusable")).isPresent();
//    }
//}
//
