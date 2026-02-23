import java.util.Objects;

public class Tag {
    private Long id;
    private String name;

    // Конструктор по умолчанию
    public Tag() {
    }

    // Конструктор с двумя параметрами
    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Конструктор с одним параметром (name)
    public Tag(String name) {
        this.name = name;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    // Переопределение toString для удобного вывода
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}