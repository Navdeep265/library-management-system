package library;

import java.util.*;
import java.util.logging.Logger;

public final class BookCatalog {
    private static final Logger LOG = Logger.getLogger(BookCatalog.class.getName());
    private final Map<String, Book> books = new LinkedHashMap<>();
    public void add(Book book) {
        if (books.putIfAbsent(book.getIsbn(), book) != null) throw new IllegalArgumentException("Duplicate ISBN");
        LOG.info(() -> "Book added: " + book.getIsbn());
    }
    public Book get(String isbn) {
        Book book = books.get(isbn);
        if (book == null) throw new IllegalArgumentException("Unknown ISBN: " + isbn);
        return book;
    }
    public void update(Book book) {
        get(book.getIsbn());
        books.put(book.getIsbn(), book);
        LOG.info(() -> "Book updated: " + book.getIsbn());
    }
    // Package-private: Library controls removal to protect active loans.
    void remove(String isbn) {
        get(isbn);
        books.remove(isbn);
        LOG.info(() -> "Book removed: " + isbn);
    }
    public List<Book> all() { return List.copyOf(books.values()); }
    public List<Book> search(String query, SearchStrategy strategy) {
        String text = Book.requireText(query);
        return books.values().stream().filter(b -> strategy.matches(b, text)).toList();
    }
}
