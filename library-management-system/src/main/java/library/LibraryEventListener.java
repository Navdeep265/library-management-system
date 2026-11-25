package library;

/** Observer contract for lending events. */
public interface LibraryEventListener {
    void onEvent(String event, Loan loan);
}
