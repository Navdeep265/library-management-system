package library;

import java.util.List;
import java.util.logging.Logger;

/** Coordinates catalog, members and lending; intended for single-threaded use. */
public final class Library {
    private static final Logger LOG = Logger.getLogger(Library.class.getName());
    private final BookCatalog catalog;
    private final PatronRegistry patrons;
    private final LendingService lending;
    public Library(BookCatalog catalog, PatronRegistry patrons, LendingService lending) {
        this.catalog = catalog; this.patrons = patrons; this.lending = lending;
    }
    public void addBook(Book book) { catalog.add(book); }
    public void updateBook(Book book) { catalog.update(book); }
    public void removeBook(String isbn) {
        if (lending.isBorrowed(isbn)) { LOG.warning("Cannot remove borrowed book: " + isbn); throw new IllegalStateException("Book is borrowed"); }
        catalog.remove(isbn);
    }
    public List<Book> search(String query, SearchStrategy strategy) { return catalog.search(query, strategy); }
    public void addPatron(Patron patron) { patrons.add(patron); }
    public void updatePatron(Patron patron) { patrons.update(patron); }
    public Patron getPatron(String id) { return patrons.get(id); }
    public Loan checkout(String isbn, String patronId) {
        try { catalog.get(isbn); patrons.get(patronId); return lending.checkout(isbn, patronId); }
        catch (IllegalArgumentException | IllegalStateException e) { LOG.warning(e.getMessage()); throw e; }
    }
    public Loan returnBook(String isbn, String patronId) {
        try { return lending.returnBook(isbn, patronId); }
        catch (IllegalStateException e) { LOG.warning(e.getMessage()); throw e; }
    }
    public List<Loan> borrowingHistory(String id) { patrons.get(id); return lending.history(id); }
    public List<Book> availableBooks() { return catalog.all().stream().filter(b -> !lending.isBorrowed(b.getIsbn())).toList(); }
    public List<Book> borrowedBooks() { return catalog.all().stream().filter(b -> lending.isBorrowed(b.getIsbn())).toList(); }
}
