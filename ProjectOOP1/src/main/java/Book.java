
public class Book {
    private int id;
    private String title;
    private String author;
    private String code;
    private boolean available;

    // for new books
    public Book(String title, String author, String code) {
        this.title = title;
        this.author = author;
        this.code = code;
        this.available = true; // Default to available
    }

    // for existing books
    public Book(int id, String title, String author, String code, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.code = code;
        this.available = available;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if(author == null || author.trim().isEmpty()){
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        this.author = author;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code == null || code.trim().isEmpty()){
            throw new IllegalArgumentException("Code cannot be null or empty.");
        }
        this.code = code;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Title: %s | Author: %s | Code: %s | Available: %s",
                id, title, author, code, available ? "Yes" : "No");
    }
}
