public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String role;

    // TODO: конструкторы, геттеры, сеттеры.

    public User() {
    }

    public User(Long id, String username, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}