package edu.taskmanager.model;

import java.util.Objects;

/**
 * Класс тега для категоризации задач.
 * Поля:
 * - id
 * - name
 */
public class Tag {
    private Long id;
    private String name;

    /**
     * Конструктор с названием.
     *
     * @param name название тега
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Конструктор со всеми полями.
     *
     * @param id   идентификатор
     * @param name название
     */
    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag withId(Long newId) {
        Tag tagWithId = new Tag(newId, this.name);
        return tagWithId;
    }
    // --- Геттеры ---

    public Long getId() { return id; }
    public String getName() { return name; }

    // --- Cеттеры ---

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }


    // --- equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Tag{")
            .append("id=").append(id)
            .append(", name='").append(name)
            .append('\'').append('}');

        return builder.toString();
    }
}
