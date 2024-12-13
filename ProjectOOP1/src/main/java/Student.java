import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Student {


    public void borrowBook(User user, String code) {
        String checkAvailabilityQuery = "SELECT * FROM books WHERE code = ?";
        String borrowBookQuery = "INSERT INTO borrowed_books (book_id, user_id) VALUES (?, ?)";
        String updateBookAvailabilityQuery = "UPDATE books SET available = FALSE WHERE code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery)) {

            checkStmt.setString(1, code);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                boolean isAvailable = rs.getBoolean("available");
                int bookId = rs.getInt("id");

                if (isAvailable) {
                    try (PreparedStatement borrowStmt = connection.prepareStatement(borrowBookQuery);
                         PreparedStatement updateStmt = connection.prepareStatement(updateBookAvailabilityQuery)) {

                        connection.setAutoCommit(false); 

                        borrowStmt.setInt(1, bookId);
                        borrowStmt.setInt(2, user.getId());
                        borrowStmt.executeUpdate();

                        updateStmt.setString(1, code);
                        updateStmt.executeUpdate();

                        connection.commit();
                        System.out.println("Book borrowed successfully.");
                    } catch (Exception e) {
                        connection.rollback();
                        System.out.println("Error borrowing book. Transaction rolled back.");
                        e.printStackTrace();
                    } finally {
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Book is currently not available.");
                }
            } else {
                System.out.println("Book with code " + code + " not found.");
            }

        } catch (Exception e) {
            System.out.println("Failed to borrow book.");
            e.printStackTrace();
        }
    }


    public void returnBook(User user, String code) {
        String findBorrowedBookQuery = "SELECT bb.id FROM borrowed_books bb " +
                "JOIN books b ON bb.book_id = b.id " +
                "WHERE b.code = ? AND bb.user_id = ? AND bb.return_date IS NULL";
        String updateBorrowedBookQuery = "UPDATE borrowed_books SET return_date = CURRENT_TIMESTAMP WHERE id = ?";
        String updateBookAvailabilityQuery = "UPDATE books SET available = TRUE WHERE code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement findStmt = connection.prepareStatement(findBorrowedBookQuery)) {

            findStmt.setString(1, code);
            findStmt.setInt(2, user.getId());
            ResultSet rs = findStmt.executeQuery();

            if (rs.next()) {
                int borrowedBookId = rs.getInt("id");

                try (PreparedStatement updateBorrowStmt = connection.prepareStatement(updateBorrowedBookQuery);
                     PreparedStatement updateBookStmt = connection.prepareStatement(updateBookAvailabilityQuery)) {

                    connection.setAutoCommit(false); 

                    updateBorrowStmt.setInt(1, borrowedBookId);
                    updateBorrowStmt.executeUpdate();

                    updateBookStmt.setString(1, code);
                    updateBookStmt.executeUpdate();

                    connection.commit();
                    System.out.println("Book returned successfully.");
                } catch (Exception e) {
                    connection.rollback();
                    System.out.println("Error returning book. Transaction rolled back.");
                    e.printStackTrace();
                } finally {
                    connection.setAutoCommit(true);
                }
            } else {
                System.out.println("No borrowed record found for code " + code + ".");
            }

        } catch (Exception e) {
            System.out.println("Failed to return book.");
            e.printStackTrace();
        }
    }


    public List<BorrowedBook> getBorrowedBooks(User user) {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.title, b.author, b.code, bb.borrow_date FROM borrowed_books bb " +
                "JOIN books b ON bb.book_id = b.id " +
                "WHERE bb.user_id = ? AND bb.return_date IS NULL";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowedBook borrowedBook = new BorrowedBook(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("code"),
                        rs.getTimestamp("borrow_date"),
                        user.getUsername() 
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
