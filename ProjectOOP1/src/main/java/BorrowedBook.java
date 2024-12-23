import java.sql.Timestamp;

public class BorrowedBook {
    private String title;
    private String author;
    private String code; 
    private Timestamp borrowDate;
    private String username; 

    public BorrowedBook(String title, String author, String code, Timestamp borrowDate, String username) {
        if (title == null || author == null || code == null || borrowDate == null || username == null) {
            throw new IllegalArgumentException("Title, author, code, borrowDate, and username cannot be null.");
        }
        this.title = title;
        this.author = author;
        this.code = code;
        this.borrowDate = borrowDate;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCode() {
        return code;
    }

    public Timestamp getBorrowDate() {
        return borrowDate;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        BorrowedBook that = (BorrowedBook) obj;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }


    @Override
    public String toString() {
        return String.format("Title: %s | Author: %s | Code: %s | Borrowed On: %s | Borrowed By: %s",
                title, author, code, borrowDate != null ? borrowDate.toString() : "N/A", username);
    }
}
