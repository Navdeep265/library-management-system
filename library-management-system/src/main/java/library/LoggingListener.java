package library;

import java.util.logging.Logger;

public final class LoggingListener implements LibraryEventListener {
    private static final Logger LOG = Logger.getLogger(LoggingListener.class.getName());
    @Override public void onEvent(String event, Loan loan) {
        LOG.info(() -> event + ": ISBN=" + loan.isbn() + ", patron=" + loan.patronId());
    }
}
