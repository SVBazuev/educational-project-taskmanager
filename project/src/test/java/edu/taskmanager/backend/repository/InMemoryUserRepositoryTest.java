//package edu.taskmanager.backend.repository;
//
//import edu.taskmanager.backend.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
//class InMemoryUserRepositoryTest {
//
//    private InMemoryUserRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        repository = new InMemoryUserRepository();
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // save()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("save(): новый пользователь (id == null) получает сгенерированный id")
//    void save_newUser_idIsGenerated() {
//        User saved = repository.save(user("alice"));
//
//        assertThat(saved.getId()).isNotNull().isPositive();
//    }
//
//    @Test
//    @DisplayName("save(): новый пользователь — username сохраняется без изменений")
//    void save_newUser_usernameIsPreserved() {
//        User saved = repository.save(user("alice"));
//
//        assertThat(saved.getUsername()).isEqualTo("alice");
//    }
//
//    @Test
//    @DisplayName("save(): два новых пользователя получают разные id")
//    void save_twoNewUsers_getDistinctIds() {
//        User u1 = repository.save(user("alice"));
//        User u2 = repository.save(user("bob"));
//
//        assertThat(u1.getId()).isNotEqualTo(u2.getId());
//    }
//
//    @Test
//    @DisplayName("save(): id генерируется монотонно возрастающим")
//    void save_multipleNewUsers_idsAreIncremental() {
//        User u1 = repository.save(user("u1"));
//        User u2 = repository.save(user("u2"));
//        User u3 = repository.save(user("u3"));
//
//        assertThat(u2.getId()).isGreaterThan(u1.getId());
//        assertThat(u3.getId()).isGreaterThan(u2.getId());
//    }
//
//    @Test
//    @DisplayName("save(): новый пользователь с уже существующим username — возвращает существующего (не дублирует)")
//    void save_duplicateUsername_returnsExisting() {
//        User original = repository.save(user("alice"));
//
//        User duplicate = repository.save(user("alice"));
//
//        assertThat(duplicate.getId()).isEqualTo(original.getId());
//        assertThat(repository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("save(): обновление пользователя с тем же username — данные перезаписываются")
//    void save_existingUserSameUsername_updatesData() {
//        User saved = repository.save(user("alice"));
//        saved.setPasswordHash("newHash");
//
//        User updated = repository.save(saved);
//
//        assertThat(updated.getPasswordHash()).isEqualTo("newHash");
//        assertThat(repository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("save(): обновление пользователя с изменением username — старый username удаляется из индекса")
//    void save_existingUserRenamed_oldUsernameNotFoundByIndex() {
//        User saved = repository.save(user("alice"));
//        saved.setUsername("alice-renamed");
//
//        repository.save(saved);
//
//        assertThat(repository.findByUsername("alice")).isEmpty();
//    }
//
//    @Test
//    @DisplayName("save(): обновление пользователя с изменением username — пользователь находится по новому username")
//    void save_existingUserRenamed_foundByNewUsername() {
//        User saved = repository.save(user("alice"));
//        saved.setUsername("alice-renamed");
//
//        repository.save(saved);
//
//        assertThat(repository.findByUsername("alice-renamed"))
//                .isPresent()
//                .get().extracting(User::getId).isEqualTo(saved.getId());
//    }
//
//    @Test
//    @DisplayName("save(): мутация возвращённого объекта не ломает индекс username")
//    void save_mutatingReturnedUser_doesNotCorruptUsernameIndex() {
//        User saved = repository.save(user("alice"));
//
//        saved.setUsername("mutated");
//
//        assertThat(repository.findByUsername("alice")).isPresent();
//        assertThat(repository.findByUsername("mutated")).isEmpty();
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // findById()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("findById(): существующий id — возвращает пользователя")
//    void findById_existingId_returnsUser() {
//        User saved = repository.save(user("alice"));
//
//        Optional<User> result = repository.findById(saved.getId());
//
//        assertThat(result).isPresent()
//                .get().extracting(User::getUsername).isEqualTo("alice");
//    }
//
//    @Test
//    @DisplayName("findById(): несуществующий id — возвращает empty")
//    void findById_unknownId_returnsEmpty() {
//        assertThat(repository.findById(999L)).isEmpty();
//    }
//
//    @Test
//    @DisplayName("findById(): после удаления — возвращает empty")
//    void findById_afterDelete_returnsEmpty() {
//        User saved = repository.save(user("temp"));
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findById(saved.getId())).isEmpty();
//    }
//
//    @Test
//    @DisplayName("findById(): возвращает копию — мутация результата не меняет хранилище")
//    void findById_mutatingResult_doesNotCorruptStorage() {
//        User saved = repository.save(user("alice"));
//
//        repository.findById(saved.getId()).ifPresent(u -> u.setUsername("changed"));
//
//        assertThat(repository.findById(saved.getId()))
//                .isPresent()
//                .get().extracting(User::getUsername).isEqualTo("alice");
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // findByUsername()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("findByUsername(): существующий username — возвращает пользователя")
//    void findByUsername_existingUsername_returnsUser() {
//        repository.save(user("bob"));
//
//        Optional<User> result = repository.findByUsername("bob");
//
//        assertThat(result).isPresent()
//                .get().extracting(User::getUsername).isEqualTo("bob");
//    }
//
//    @Test
//    @DisplayName("findByUsername(): несуществующий username — возвращает empty")
//    void findByUsername_unknownUsername_returnsEmpty() {
//        assertThat(repository.findByUsername("ghost")).isEmpty();
//    }
//
//    @Test
//    @DisplayName("findByUsername(): после удаления — возвращает empty")
//    void findByUsername_afterDelete_returnsEmpty() {
//        User saved = repository.save(user("temp"));
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findByUsername("temp")).isEmpty();
//    }
//
//    @Test
//    @DisplayName("findByUsername(): возвращает копию — мутация результата не меняет хранилище")
//    void findByUsername_mutatingResult_doesNotCorruptStorage() {
//        repository.save(user("alice"));
//
//        repository.findByUsername("alice").ifPresent(u -> u.setUsername("changed"));
//
//        assertThat(repository.findByUsername("alice")).isPresent();
//        assertThat(repository.findByUsername("changed")).isEmpty();
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
//    @DisplayName("findAll(): возвращает всех сохранённых пользователей")
//    void findAll_multipleUsers_returnsAll() {
//        repository.save(user("alice"));
//        repository.save(user("bob"));
//        repository.save(user("charlie"));
//
//        List<User> all = repository.findAll();
//
//        assertThat(all).hasSize(3)
//                .extracting(User::getUsername)
//                .containsExactlyInAnyOrder("alice", "bob", "charlie");
//    }
//
//    @Test
//    @DisplayName("findAll(): после удаления одного пользователя — не содержит удалённого")
//    void findAll_afterDelete_doesNotContainDeleted() {
//        repository.save(user("keep"));
//        User toDelete = repository.save(user("remove"));
//
//        repository.deleteById(toDelete.getId());
//
//        assertThat(repository.findAll())
//                .hasSize(1)
//                .extracting(User::getUsername)
//                .containsExactly("keep");
//    }
//
//    @Test
//    @DisplayName("findAll(): возвращает копии — мутация элементов не меняет хранилище")
//    void findAll_mutatingResults_doesNotCorruptStorage() {
//        repository.save(user("alice"));
//
//        repository.findAll().forEach(u -> u.setUsername("changed"));
//
//        assertThat(repository.findAll())
//                .extracting(User::getUsername)
//                .containsExactly("alice");
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // deleteById()
//    // ──────────────────────────────────────────────────────────────────────────
//
//    @Test
//    @DisplayName("deleteById(): существующий id — пользователь удаляется из хранилища")
//    void deleteById_existingId_userRemoved() {
//        User saved = repository.save(user("toDelete"));
//
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findById(saved.getId())).isEmpty();
//        assertThat(repository.findAll()).isEmpty();
//    }
//
//    @Test
//    @DisplayName("deleteById(): существующий id — username удаляется из индекса")
//    void deleteById_existingId_usernameRemovedFromIndex() {
//        User saved = repository.save(user("indexed"));
//
//        repository.deleteById(saved.getId());
//
//        assertThat(repository.findByUsername("indexed")).isEmpty();
//    }
//
//    @Test
//    @DisplayName("deleteById(): несуществующий id — не бросает исключение, хранилище не меняется")
//    void deleteById_unknownId_noExceptionThrown() {
//        repository.save(user("safe"));
//
//        assertDoesNotThrow(() -> repository.deleteById(999L));
//        assertThat(repository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("deleteById(): после удаления можно сохранить пользователя с тем же username")
//    void deleteById_afterDelete_sameUsernameCanBeSavedAgain() {
//        User first = repository.save(user("reusable"));
//        repository.deleteById(first.getId());
//
//        User second = repository.save(user("reusable"));
//
//        assertThat(second.getId()).isNotNull();
//        assertThat(repository.findByUsername("reusable")).isPresent();
//    }
//
//    // ──────────────────────────────────────────────────────────────────────────
//    // Вспомогательные методы
//    // ──────────────────────────────────────────────────────────────────────────
//
//    private User user(String username) {
//        return new User(username, "hash", User.Role.USER);
//    }
//}