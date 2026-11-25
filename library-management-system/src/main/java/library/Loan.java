package library;

import java.time.Instant;

/** Immutable borrowing record; a return creates a completed version. */
public record Loan(String isbn, String patronId, Instant checkoutTime, Instant returnTime) {
    public boolean isActive() { return returnTime == null; }
    public Loan returnedAt(Instant time) {
        if (!isActive() || time.isBefore(checkoutTime)) throw new IllegalStateException("Invalid return");
        return new Loan(isbn, patronId, checkoutTime, time);
    }
}
