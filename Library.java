
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private final String FILE_NAME = "books.txt";

    public Library() {
        loadBooks();
    }

    public void addBook(Scanner sc) {
        try {
            System.out.print("Enter Book ID: ");
            int id = Integer.parseInt(sc.nextLine());

            if (findBookById(id) != null) {
                System.out.println("Book ID already exists.");
                return;
            }

            System.out.print("Enter Title: ");
            String title = sc.nextLine().trim();

            System.out.print("Enter Author: ");
            String author = sc.nextLine().trim();

            if (id <= 0 || title.isEmpty() || author.isEmpty()) {
                System.out.println("Invalid input. Please try again.");
                return;
            }

            books.add(new Book(id, title, author));
            saveBooks();

            System.out.println("Book added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Book ID must be a number.");
        }
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        for (Book book : books) {
            book.displayBook();
        }
    }

    public void searchBook(Scanner sc) {
        System.out.print("Enter Book ID or Title: ");
        String keyword = sc.nextLine().trim();

        boolean found = false;

        for (Book book : books) {
            if (String.valueOf(book.getId()).equalsIgnoreCase(keyword)
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
        try {
            System.out.print("Enter Book ID to remove: ");
            int id = Integer.parseInt(sc.nextLine());

            Book book = findBookById(id);

            if (book == null) {
                System.out.println("Book not found.");
                return;
            }

            books.remove(book);
            saveBooks();

            System.out.println("Book removed successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Book ID must be a number.");
        }
    }

    private Book findBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
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
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);

                if (data.length != 3) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0]);

                    if (id <= 0
                            || data[1].trim().isEmpty()
                            || data[2].trim().isEmpty()
                            || findBookById(id) != null) {
                        continue;
                    }

                    books.add(new Book(id, data[1], data[2]));

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid book record.");
                }
            }

        } catch (IOException e) {

        }
    }
}
