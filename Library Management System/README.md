# Mini Library Management System (Java, OOP)

## Overview
A simple Java console-based Library Management System demonstrating OOP principles:
- Classes: `Book`, `User`, `Library`, `Main`
- Features: add books/users, issue & return books, search, list available books
- Example policies: max 5 books per user (easy to change)

## How to run
1. Clone or download repository.
2. Open terminal in project folder.
3. Compile: `javac *.java`
4. Run: `java Main`

## Files
- `Book.java` — Book model, issue/return logic.
- `User.java` — User model, tracks issued book ISBNs.
- `Library.java` — Library operations (add/search/issue/return).
- `Main.java` — Console UI (menu-driven).

## Notes
- This is a simple in-memory system (no DB). To persist data, add file or DB I/O.
- Policies (like max issued books) are easy to change in `Library.java`.

## Interview prep
See `INTERVIEW.md` (or refer to the README) for common OOP interview questions and answers.
