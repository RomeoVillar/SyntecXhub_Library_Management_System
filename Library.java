
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private final String FILE_NAME = "books.txt";

    public Library() {
        loadBooks();
    }
    
    public void addBook(Scanner sc) {
        System.out.print("Enter Book ID: ");
        String id = sc.nextLine().trim();

        if (!id.matches("\\d+")) {
            System.out.println("Invalid ID. Use numbers only.");
            return;
        }

        if (findBookById(id) != null) {
            System.out.println("Book ID already exists.");
            return;
        }

        System.out.print("Enter Title: ");
        String title = sc.nextLine().trim();

        System.out.print("Enter Author: ");
        String author = sc.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Title and author cannot be empty.");
            return;
        }

        books.add(new Book(id, title, author));
        saveBooks();

        System.out.println("Book added successfully!");
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println("\n===== ALL BOOKS =====");

        for (Book book : books) {
            book.displayBook();
        }
    }
    
    public void searchBook(Scanner sc) {
        System.out.print("Enter Book ID or Title: ");
        String keyword = sc.nextLine().trim();

        boolean found = false;

        for (Book book : books) {
            if (book.getId().equals(keyword)
                    || book.getTitle().equalsIgnoreCase(keyword)) {
                book.displayBook();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Book not found.");
        }
    }
    
    public void removeBook(Scanner sc) {
        System.out.print("Enter Book ID to remove: ");
        String id = sc.nextLine().trim();

        Book book = findBookById(id);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        books.remove(book);
        saveBooks();

        System.out.println("Book removed successfully!");
    }
    
    private Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }

        return null;
    }
    
    private void saveBooks() {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Book book : books) {
                writer.write(
                        book.getId() + "|"
                                + book.getTitle() + "|"
                                + book.getAuthor()
                );

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println(
                    "Error saving books: " + e.getMessage()
            );
        }
    }

    // Load saved books when the program starts
    private void loadBooks() {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);

                if (data.length != 3) {
                    continue;
                }

                String id = data[0].trim();
                String title = data[1].trim();
                String author = data[2].trim();

                if (!id.matches("\\d+")
                        || title.isEmpty()
                        || author.isEmpty()
                        || findBookById(id) != null) {
                    continue;
                }

                books.add(new Book(id, title, author));
            }

        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            System.out.println(
                    "Error loading books: " + e.getMessage()
            );
        }
    }
}
