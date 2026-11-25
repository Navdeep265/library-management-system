package library;

import java.time.Clock;
import java.util.Scanner;
import java.util.logging.Logger;

public final class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        LendingService lending = new LendingService(Clock.systemUTC());
        lending.subscribe(new LoggingListener());
        Library library = new Library(new BookCatalog(), new PatronRegistry(), lending);
        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1 Add book | 2 Update book | 3 Remove book | 4 Search | 5 Add patron | 6 Update patron\n7 Checkout | 8 Return | 9 Available | 10 Borrowed | 11 History | 0 Exit");
                if (!input.hasNextLine()) return;
                String choice = input.nextLine().trim();
                try {
                    switch (choice) {
                        case "0" -> { return; }
                        case "1" -> { library.addBook(readBook(input)); System.out.println("Book added"); }
                        case "2" -> { library.updateBook(readBook(input)); System.out.println("Book updated"); }
                        case "3" -> { library.removeBook(ask(input,"ISBN")); System.out.println("Book removed"); }
                        case "4" -> System.out.println(library.search(ask(input,"Query"), SearchField.valueOf(ask(input,"TITLE/AUTHOR/ISBN").toUpperCase(java.util.Locale.ROOT))));
                        case "5" -> { library.addPatron(readPatron(input)); System.out.println("Patron added"); }
                        case "6" -> { library.updatePatron(readPatron(input)); System.out.println("Patron updated"); }
                        case "7" -> System.out.println(library.checkout(ask(input,"ISBN"), ask(input,"Patron ID")));
                        case "8" -> System.out.println(library.returnBook(ask(input,"ISBN"), ask(input,"Patron ID")));
                        case "9" -> System.out.println(library.availableBooks());
                        case "10" -> System.out.println(library.borrowedBooks());
                        case "11" -> System.out.println(library.borrowingHistory(ask(input,"Patron ID")));
                        default -> System.out.println("Invalid choice");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) { LOG.warning(e.getMessage()); System.out.println("Error: " + e.getMessage()); }
                catch (java.util.NoSuchElementException e) { return; }
            }
        }
    }
    private static String ask(Scanner input, String prompt) { System.out.print(prompt + ": "); return input.nextLine().trim(); }
    private static Book readBook(Scanner input) { return new Book(ask(input,"Title"), ask(input,"Author"), ask(input,"ISBN"), Integer.parseInt(ask(input,"Publication year"))); }
    private static Patron readPatron(Scanner input) { return new Patron(ask(input,"Patron ID"), ask(input,"Name"), ask(input,"Email")); }
}
