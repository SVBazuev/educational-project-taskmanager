package edu.taskmanager.backend.repository;

import edu.taskmanager.backend.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTagRepositoryTest {

    private InMemoryTagRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTagRepository();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // save()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("save(): новый тег получает сгенерированный id")
    void save_newTag_idIsGenerated() {
        Tag saved = repository.save(new Tag("backend"));

        assertThat(saved.getId()).isNotNull().isPositive();
    }

    @Test
    @DisplayName("save(): новый тег — имя сохраняется без изменений")
    void save_newTag_nameIsPreserved() {
        Tag saved = repository.save(new Tag("backend"));

        assertThat(saved.getName()).isEqualTo("backend");
    }

    @Test
    @DisplayName("save(): два тега с разными именами получают разные id")
    void save_twoNewTags_getDistinctIds() {
        Tag t1 = repository.save(new Tag("frontend"));
        Tag t2 = repository.save(new Tag("backend"));

        assertThat(t1.getId()).isNotEqualTo(t2.getId());
    }

    @Test
    @DisplayName("save(): id генерируется монотонно возрастающим")
    void save_multipleNewTags_idsAreIncremental() {
        Tag t1 = repository.save(new Tag("alpha"));
        Tag t2 = repository.save(new Tag("beta"));
        Tag t3 = repository.save(new Tag("gamma"));

        assertThat(t2.getId()).isGreaterThan(t1.getId());
        assertThat(t3.getId()).isGreaterThan(t2.getId());
    }

    @Test
    @DisplayName("save(): тег с уже существующим именем — возвращает существующий (не дублирует)")
    void save_duplicateName_returnsExisting() {
        Tag original = repository.save(new Tag("urgent"));

        Tag duplicate = repository.save(new Tag("urgent"));

        assertThat(duplicate.getId()).isEqualTo(original.getId());
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("save(): мутация возвращённого объекта не меняет данные в репозитории")
    void save_mutatingReturnedTag_doesNotCorruptStorage() {
        Tag saved = repository.save(new Tag("original"));

        saved.setName("mutated");

        assertThat(repository.findByName("original")).isPresent();
        assertThat(repository.findByName("mutated")).isEmpty();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // findById()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findById(): существующий id — возвращает тег")
    void findById_existingId_returnsTag() {
        Tag saved = repository.save(new Tag("bugfix"));

        Optional<Tag> result = repository.findById(saved.getId());

        assertThat(result).isPresent()
                .get().extracting(Tag::getName).isEqualTo("bugfix");
    }

    @Test
    @DisplayName("findById(): несуществующий id — возвращает empty")
    void findById_unknownId_returnsEmpty() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    @DisplayName("findById(): после удаления — возвращает empty")
    void findById_afterDelete_returnsEmpty() {
        Tag saved = repository.save(new Tag("temp"));
        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("findById(): возвращает копию — мутация результата не меняет хранилище")
    void findById_mutatingResult_doesNotCorruptStorage() {
        Tag saved = repository.save(new Tag("stable"));

        repository.findById(saved.getId()).ifPresent(t -> t.setName("changed"));

        assertThat(repository.findById(saved.getId()))
                .isPresent()
                .get().extracting(Tag::getName).isEqualTo("stable");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // findByName()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findByName(): существующее имя — возвращает тег")
    void findByName_existingName_returnsTag() {
        repository.save(new Tag("devops"));

        Optional<Tag> result = repository.findByName("devops");

        assertThat(result).isPresent()
                .get().extracting(Tag::getName).isEqualTo("devops");
    }

    @Test
    @DisplayName("findByName(): несуществующее имя — возвращает empty")
    void findByName_unknownName_returnsEmpty() {
        assertThat(repository.findByName("nonexistent")).isEmpty();
    }

    @Test
    @DisplayName("findByName(): после удаления — возвращает empty")
    void findByName_afterDelete_returnsEmpty() {
        Tag saved = repository.save(new Tag("toRemove"));
        repository.deleteById(saved.getId());

        assertThat(repository.findByName("toRemove")).isEmpty();
    }

    @Test
    @DisplayName("findByName(): возвращает копию — мутация результата не меняет хранилище")
    void findByName_mutatingResult_doesNotCorruptStorage() {
        repository.save(new Tag("stable"));

        repository.findByName("stable").ifPresent(t -> t.setName("changed"));

        assertThat(repository.findByName("stable")).isPresent();
        assertThat(repository.findByName("changed")).isEmpty();
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
    @DisplayName("findAll(): возвращает все сохранённые теги")
    void findAll_multipleTags_returnsAll() {
        repository.save(new Tag("alpha"));
        repository.save(new Tag("beta"));
        repository.save(new Tag("gamma"));

        List<Tag> all = repository.findAll();

        assertThat(all).hasSize(3)
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("alpha", "beta", "gamma");
    }

    @Test
    @DisplayName("findAll(): после удаления одного тега — не содержит удалённый")
    void findAll_afterDelete_doesNotContainDeleted() {
        repository.save(new Tag("keep"));
        Tag toDelete = repository.save(new Tag("remove"));

        repository.deleteById(toDelete.getId());

        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(Tag::getName)
                .containsExactly("keep");
    }

    @Test
    @DisplayName("findAll(): возвращает копии — мутация элементов не меняет хранилище")
    void findAll_mutatingResults_doesNotCorruptStorage() {
        repository.save(new Tag("stable"));

        repository.findAll().forEach(t -> t.setName("changed"));

        assertThat(repository.findAll())
                .extracting(Tag::getName)
                .containsExactly("stable");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // deleteById()
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteById(): существующий id — тег удаляется из хранилища")
    void deleteById_existingId_tagRemoved() {
        Tag saved = repository.save(new Tag("toDelete"));

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("deleteById(): существующий id — имя удаляется из индекса")
    void deleteById_existingId_nameRemovedFromIndex() {
        Tag saved = repository.save(new Tag("indexed"));

        repository.deleteById(saved.getId());

        assertThat(repository.findByName("indexed")).isEmpty();
    }

    @Test
    @DisplayName("deleteById(): несуществующий id — не бросает исключение, хранилище не меняется")
    void deleteById_unknownId_noExceptionThrown() {
        repository.save(new Tag("safe"));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                () -> repository.deleteById(999L)
        );
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("deleteById(): после удаления можно сохранить тег с тем же именем")
    void deleteById_afterDelete_sameNameCanBeSavedAgain() {
        Tag first = repository.save(new Tag("reusable"));
        repository.deleteById(first.getId());

        Tag second = repository.save(new Tag("reusable"));

        assertThat(second.getId()).isNotNull();
        assertThat(repository.findByName("reusable")).isPresent();
    }
}