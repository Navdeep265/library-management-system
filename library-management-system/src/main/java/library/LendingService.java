package library;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.logging.*;

public final class LendingService {
    private static final Logger LOG = Logger.getLogger(LendingService.class.getName());
    private final Map<String, Loan> active = new HashMap<>();
    private final Map<String, List<Loan>> completed = new HashMap<>();
    private final List<LibraryEventListener> listeners = new ArrayList<>();
    private final Clock clock;
    public LendingService(Clock clock) { this.clock = Objects.requireNonNull(clock); }
    public void subscribe(LibraryEventListener listener) { listeners.add(Objects.requireNonNull(listener)); }
    public boolean isBorrowed(String isbn) { return active.containsKey(isbn); }
    Loan checkout(String isbn, String patronId) {
        if (isBorrowed(isbn)) throw new IllegalStateException("Book already borrowed");
        Loan loan = new Loan(isbn, patronId, Instant.now(clock), null);
        active.put(isbn, loan);
        publish("CHECKOUT", loan);
        return loan;
    }
    Loan returnBook(String isbn, String patronId) {
        Loan loan = active.get(isbn);
        if (loan == null || !loan.patronId().equals(patronId)) throw new IllegalStateException("No matching active loan");
        Loan returned = loan.returnedAt(Instant.now(clock));
        active.remove(isbn);
        completed.computeIfAbsent(patronId, key -> new ArrayList<>()).add(returned);
        publish("RETURN", returned);
        return returned;
    }
    public List<Loan> history(String patronId) {
        List<Loan> result = new ArrayList<>(completed.getOrDefault(patronId, List.of()));
        active.values().stream().filter(l -> l.patronId().equals(patronId)).forEach(result::add);
        result.sort(Comparator.comparing(Loan::checkoutTime).thenComparing(Loan::isbn));
        return List.copyOf(result);
    }
    private void publish(String event, Loan loan) {
        for (LibraryEventListener listener : List.copyOf(listeners)) {
            try { listener.onEvent(event, loan); }
            catch (RuntimeException e) { LOG.log(Level.WARNING, "Listener failed", e); }
        }
    }
}
