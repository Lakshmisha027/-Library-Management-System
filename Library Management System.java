// Main.java
// Single-file Library Management System (Book, User, Library + interactive Main)
// Save as Main.java
// Compile: javac Main.java
// Run:     java Main

import java.util.*;

/**
 * Single-file Library Management System demonstrating OOP:
 * - Classes: Book, User, Library (non-public helper classes)
 * - Console menu: add/list/search/issue/return users & books
 * - Simple in-memory data store (no DB)
 *
 * Filename must be Main.java because public class is Main.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = new Library();

    public static void main(String[] args) {
        System.out.println("=== Mini Library Management System ===");
        library.loadSampleData();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": listAllBooks(); break;
                case "2": listAvailableBooks(); break;
                case "3": listAllUsers(); break;
                case "4": issueBookInteract(); break;
                case "5": returnBookInteract(); break;
                case "6": searchBooksInteract(); break;
                case "7": addBookInteract(); break;
                case "8": addUserInteract(); break;
                case "9": showBookDetailsInteract(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid choice. Try again."); break;
            }
        }
        System.out.println("Exiting. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. List all books");
        System.out.println("2. List available books");
        System.out.println("3. List all users");
        System.out.println("4. Issue a book");
        System.out.println("5. Return a book");
        System.out.println("6. Search books by title");
        System.out.println("7. Add a new book");
        System.out.println("8. Add a new user");
        System.out.println("9. Show book details (by ISBN)");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void listAllBooks() {
        List<Book> books = library.listAllBooks();
        System.out.println("\nAll books:");
        if (books.isEmpty()) System.out.println("  (No books)");
        for (Book b : books) System.out.println("  " + b);
    }

    private static void listAvailableBooks() {
        List<Book> books = library.listAvailableBooks();
        System.out.println("\nAvailable books:");
        if (books.isEmpty()) System.out.println("  (No available books)");
        for (Book b : books) System.out.println("  " + b);
    }

    private static void listAllUsers() {
        List<User> users = library.listAllUsers();
        System.out.println("\nUsers:");
        if (users.isEmpty()) System.out.println("  (No users)");
        for (User u : users) {
            System.out.println("  " + u);
            if (!u.getIssuedBookIsbns().isEmpty()) {
                System.out.println("    Issued books: " + u.getIssuedBookIsbns());
            }
        }
    }

    private static void issueBookInteract() {
        try {
            System.out.print("Enter ISBN to issue: ");
            String isbn = scanner.nextLine().trim();
            System.out.print("Enter User ID: ");
            int uid = Integer.parseInt(scanner.nextLine().trim());
            String res = library.issueBook(isbn, uid);
            System.out.println(res);
        } catch (NumberFormatException e) {
            System.out.println("Invalid user id. It must be an integer.");
        }
    }

    private static void returnBookInteract() {
        try {
            System.out.print("Enter ISBN to return: ");
            String isbn = scanner.nextLine().trim();
            System.out.print("Enter User ID: ");
            int uid = Integer.parseInt(scanner.nextLine().trim());
            String res = library.returnBook(isbn, uid);
            System.out.println(res);
        } catch (NumberFormatException e) {
            System.out.println("Invalid user id. It must be an integer.");
        }
    }

    private static void searchBooksInteract() {
        System.out.print("Enter title keyword: ");
        String q = scanner.nextLine().trim();
        List<Book> res = library.searchByTitle(q);
        System.out.println("Search results:");
        if (res.isEmpty()) System.out.println("  (No matches)");
        for (Book b : res) System.out.println("  " + b);
    }

    private static void addBookInteract() {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter author: ");
        String author = scanner.nextLine().trim();
        boolean ok = library.addBook(new Book(isbn, title, author));
        System.out.println(ok ? "Book added." : "Book with same ISBN already exists.");
    }

    private static void addUserInteract() {
        try {
            System.out.print("Enter user id (integer): ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();
            boolean ok = library.addUser(new User(id, name, email));
            System.out.println(ok ? "User added." : "User with same id already exists.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid user id. It must be an integer.");
        }
    }

    private static void showBookDetailsInteract() {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        Book b = library.getBookByIsbn(isbn);
        if (b == null) {
            System.out.println("Book not found.");
        } else {
            System.out.println(b);
            if (b.isIssued()) {
                User u = library.getUserById(b.getIssuedToUserId());
                System.out.println("  Issued to: " + (u != null ? u.getName() + " (ID=" + u.getUserId() + ")" : "User ID " + b.getIssuedToUserId()));
            }
        }
    }

    // ----------------------------
    // Non-public helper classes
    // ----------------------------

    // Book class
    static class Book {
        private final String isbn;
        private final String title;
        private final String author;
        private boolean isIssued;
        private Integer issuedToUserId; // null if not issued

        public Book(String isbn, String title, String author) {
            this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null").trim();
            this.title = Objects.requireNonNull(title, "Title cannot be null").trim();
            this.author = Objects.requireNonNull(author, "Author cannot be null").trim();
            this.isIssued = false;
            this.issuedToUserId = null;
        }

        // Getters
        public String getIsbn() { return isbn; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public boolean isIssued() { return isIssued; }
        public Integer getIssuedToUserId() { return issuedToUserId; }

        // Issue the book to a user (userId)
        public boolean issueTo(int userId) {
            if (isIssued) return false;
            isIssued = true;
            issuedToUserId = userId;
            return true;
        }

        // Return the book
        public boolean returnBook() {
            if (!isIssued) return false;
            isIssued = false;
            issuedToUserId = null;
            return true;
        }

        @Override
        public String toString() {
            return String.format("ISBN: %s | Title: %s | Author: %s | Issued: %s",
                    isbn, title, author, isIssued ? ("Yes (UserId=" + issuedToUserId + ")") : "No");
        }
    }

    // User class
    static class User {
        private final int userId;
        private final String name;
        private final String email;
        private final Set<String> issuedBookIsbns; // tracks which books user has

        public User(int userId, String name, String email) {
            this.userId = userId;
            this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
            this.email = Objects.requireNonNull(email, "Email cannot be null").trim();
            this.issuedBookIsbns = new HashSet<>();
        }

        public int getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public Set<String> getIssuedBookIsbns() { return Collections.unmodifiableSet(issuedBookIsbns); }

        public void addIssuedBook(String isbn) {
            issuedBookIsbns.add(isbn);
        }

        public void removeIssuedBook(String isbn) {
            issuedBookIsbns.remove(isbn);
        }

        @Override
        public String toString() {
            return String.format("UserId: %d | Name: %s | Email: %s | BooksIssued: %d",
                    userId, name, email, issuedBookIsbns.size());
        }
    }

    // Library class
    static class Library {
        private final Map<String, Book> booksByIsbn;
        private final Map<Integer, User> usersById;
        private final int maxBooksPerUser = 5; // simple policy

        public Library() {
            booksByIsbn = new HashMap<>();
            usersById = new HashMap<>();
        }

        // Add a book
        public boolean addBook(Book book) {
            String key = book.getIsbn();
            if (booksByIsbn.containsKey(key)) return false;
            booksByIsbn.put(key, book);
            return true;
        }

        // Add a user
        public boolean addUser(User user) {
            if (usersById.containsKey(user.getUserId())) return false;
            usersById.put(user.getUserId(), user);
            return true;
        }

        // Issue book to userId
        public String issueBook(String isbn, int userId) {
            Book book = booksByIsbn.get(isbn);
            User user = usersById.get(userId);
            if (book == null) return "Book not found.";
            if (user == null) return "User not found.";
            if (book.isIssued()) return "Book already issued.";
            if (user.getIssuedBookIsbns().size() >= maxBooksPerUser) return "User has reached max allowed issued books (" + maxBooksPerUser + ").";

            boolean ok = book.issueTo(userId);
            if (ok) {
                user.addIssuedBook(isbn);
                return "Book issued successfully.";
            } else {
                return "Failed to issue book.";
            }
        }

        // Return book
        public String returnBook(String isbn, int userId) {
            Book book = booksByIsbn.get(isbn);
            User user = usersById.get(userId);
            if (book == null) return "Book not found.";
            if (user == null) return "User not found.";
            if (!book.isIssued() || !Objects.equals(book.getIssuedToUserId(), userId)) {
                return "This book is not issued to this user.";
            }
            boolean ok = book.returnBook();
            if (ok) {
                user.removeIssuedBook(isbn);
                return "Book returned successfully.";
            } else {
                return "Failed to return book.";
            }
        }

        // Search books by title substring
        public List<Book> searchByTitle(String titleSub) {
            List<Book> results = new ArrayList<>();
            for (Book b : booksByIsbn.values()) {
                if (b.getTitle().toLowerCase().contains(titleSub.toLowerCase())) results.add(b);
            }
            results.sort(Comparator.comparing(Book::getTitle));
            return results;
        }

        // List available books
        public List<Book> listAvailableBooks() {
            List<Book> list = new ArrayList<>();
            for (Book b : booksByIsbn.values()) if (!b.isIssued()) list.add(b);
            list.sort(Comparator.comparing(Book::getTitle));
            return list;
        }

        // List all books
        public List<Book> listAllBooks() {
            List<Book> list = new ArrayList<>(booksByIsbn.values());
            list.sort(Comparator.comparing(Book::getTitle));
            return list;
        }

        // List users
        public List<User> listAllUsers() {
            List<User> list = new ArrayList<>(usersById.values());
            list.sort(Comparator.comparing(User::getName));
            return list;
        }

        // Utility getters for interactive details
        public Book getBookByIsbn(String isbn) {
            return booksByIsbn.get(isbn);
        }

        public User getUserById(Integer id) {
            return usersById.get(id);
        }

        // Utility to load sample data quickly
        public void loadSampleData() {
            addBook(new Book("978-0134685991", "Effective Java", "Joshua Bloch"));
            addBook(new Book("978-0596009205", "Head First Java", "Kathy Sierra & Bert Bates"));
            addBook(new Book("978-0201633610", "Design Patterns", "Gamma, Helm, Johnson, Vlissides"));
            addBook(new Book("978-0132350884", "Clean Code", "Robert C. Martin"));
            addBook(new Book("978-1617294945", "Java Concurrency in Practice", "Brian Goetz"));

            addUser(new User(1, "Alice", "alice@example.com"));
            addUser(new User(2, "Bob", "bob@example.com"));
            addUser(new User(3, "Charlie", "charlie@example.com"));
        }
    }
}
