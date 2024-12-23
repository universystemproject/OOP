
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Librarian {

    /**
        some books
     */
    public void addBooks(List<Book> books) {
        String insertQuery = "INSERT INTO books (title, author, code) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                for (Book book : books) {
                    pstmt.setString(1, book.getTitle());
                    pstmt.setString(2, book.getAuthor());
                    pstmt.setString(3, book.getCode());
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                connection.commit();
                System.out.println("Books added successfully.");
            } catch (Exception e) {
                connection.rollback();
                System.out.println("Error adding books. Transaction rolled back.");
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Failed to add books.");
            e.printStackTrace();
        }
    }

    /**
        some books
     */
    public void removeBook(String code) {
        String deleteQuery = "DELETE FROM books WHERE code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {

            pstmt.setString(1, code);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book with Code " + code + " removed successfully.");
            } else {
                System.out.println("Book with Code " + code + " not found.");
            }

        } catch (Exception e) {
            System.out.println("Failed to remove book.");
            e.printStackTrace();
        }
    }

    /**
     list of books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String selectQuery = "SELECT * FROM books";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("code"),
                        rs.getBoolean("available")
                );
                books.add(book);
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve books.");
            e.printStackTrace();
        }

        return books;
    }

    /**
        update availability
     */
    public void updateBookAvailability(String code, boolean available) {
        String updateQuery = "UPDATE books SET available = ? WHERE code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {

            pstmt.setBoolean(1, available);
            pstmt.setString(2, code);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book availability updated successfully.");
            } else {
                System.out.println("Book with Code " + code + " not found.");
            }

        } catch (Exception e) {
            System.out.println("Failed to update book availability.");
            e.printStackTrace();
        }
    }

    /**
        method to search books by author or title
     */
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String searchQuery = "SELECT * FROM books WHERE title ILIKE ? OR author ILIKE ? OR code ILIKE ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("code"),
                        rs.getBoolean("available")
                );
                books.add(book);
            }

        } catch (Exception e) {
            System.out.println("Failed to search books.");
            e.printStackTrace();
        }

        return books;
    }
    public List<BorrowedBook> viewBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.title, b.author, b.code, bb.borrow_date, u.username FROM borrowed_books bb JOIN books b ON bb.book_id = b.id JOIN users u ON bb.user_id = u.id WHERE bb.return_date IS NULL";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BorrowedBook borrowedBook = new BorrowedBook(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("code"),
                        rs.getTimestamp("borrow_date"),
                        rs.getString("username")
                );
                borrowedBooks.add(borrowedBook);
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve borrowed books.");
            e.printStackTrace();
        }

        return borrowedBooks;
    }
}
