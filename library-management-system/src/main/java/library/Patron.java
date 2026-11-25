package library;

public final class Patron {
    private final String id;
    private final String name;
    private final String email;

    public Patron(String id, String name, String email) {
        this.id = Book.requireText(id);
        this.name = Book.requireText(name);
        this.email = Book.requireText(email);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
