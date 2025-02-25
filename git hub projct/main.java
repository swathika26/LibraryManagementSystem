import java.sql.*;
import java.util.Scanner;

public class main.java {
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBook(scanner);
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    borrowBook(scanner);
                    break;
                case 4:
                    returnBook(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addBook(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO books (title, author, available) VALUES (?, ?, 1)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewBooks() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {
            System.out.println("\nBooks Available:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("title") + " by " + rs.getString("author") + " (" + (rs.getBoolean("available") ? "Available" : "Borrowed") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void borrowBook(Scanner scanner) {
        System.out.print("Enter book ID to borrow: ");
        int bookId = scanner.nextInt();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE books SET available = 0 WHERE id = ? AND available = 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book borrowed successfully!");
            } else {
                System.out.println("Book not available!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void returnBook(Scanner scanner) {
        System.out.print("Enter book ID to return: ");
        int bookId = scanner.nextInt();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE books SET available = 1 WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            System.out.println("Book returned successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
