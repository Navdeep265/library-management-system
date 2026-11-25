package library;

/** A single inventory item, identified by ISBN. */
public final class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private final int publicationYear;

    public Book(String title, String author, String isbn, int publicationYear) {
        this.title = requireText(title);
        this.author = requireText(author);
        this.isbn = requireText(isbn);
        if (publicationYear <= 0) throw new IllegalArgumentException("Invalid publication year");
        this.publicationYear = publicationYear;
    }
    static String requireText(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Text must not be blank");
        return value.trim();
    }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getPublicationYear() { return publicationYear; }
    @Override public String toString() { return title + " by " + author + " (" + isbn + ")"; }
}
