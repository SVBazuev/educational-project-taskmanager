package edu.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.taskmanager.console.util.LenientObjectIdResolver;

import java.util.Objects;

/**
 * Класс представляет пользователя системы (для авторизации).
 * Поля:
 * - id
 * - username
 * - passwordHash (в реальности хранить хеш)
 * - role (например, ADMIN, USER)
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = User.class, resolver = LenientObjectIdResolver.class)
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private Role role;

    /**
     * Роль пользователя в системе.
     */
    public enum Role {
        ADMIN,      // Администратор (полный доступ)
        USER,       // Обычный пользователь (доступ к своим задачам)
        GUEST       // Гость (только чтение)
    }

    /**
     * Конструктор по умолчанию.
     */
    public User() {
        this.username = "guest";
        this.passwordHash = "";
        this.role = Role.GUEST;
    }

    /**
     * Конструктор с основными полями.
     *
     * @param username     имя пользователя
     * @param passwordHash хеш пароля
     * @param role         роль
     */
    public User(String username, String passwordHash, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    /**
     * Конструктор со всеми полями.
     *
     * @param id           идентификатор
     * @param username     имя пользователя
     * @param passwordHash хеш пароля
     * @param role         роль
     */
    public User(Long id, String username, String passwordHash, Role role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }


    public User(User other) {
        this.username = other.username;
        this.passwordHash = other.passwordHash;
        this.role = other.role;
    }

    // Метод withId, использующий конструктор копирования
    public User withId(Long newId) {
        User copy = new User(this);
        copy.id = newId;
        return copy;
    }

    // --- Геттеры ---

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }

    // --- Cеттеры ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // --- equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("User{").append("id=")
            .append(id).append(", username='")
            .append(username).append('\'')
            .append(", role=").append(role)
            .append('}');

        return builder.toString();
    }
}
