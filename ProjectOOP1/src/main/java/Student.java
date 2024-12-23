import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Student {
    public List<Course> viewCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses ORDER BY course_name ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course(
                        rs.getString("course_name"),
                        rs.getString("course_code"),
                        rs.getInt("credits"),
                        rs.getString("professor_username")
                );
                courses.add(course);
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve courses.");
            e.printStackTrace();
        }

        return courses;
    }

    public boolean registerForCourse(String studentUsername, String courseNameOrCode, String professorUsername) {
        // First, verify that the course exists with the given name or code and professor username
        String findCourseQuery = "SELECT * FROM courses WHERE (course_name = ? OR course_code = ?) AND professor_username = ? LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement findCourseStmt = connection.prepareStatement(findCourseQuery)) {

            findCourseStmt.setString(1, courseNameOrCode.trim());
            findCourseStmt.setString(2, courseNameOrCode.trim());
            findCourseStmt.setString(3, professorUsername.trim());

            ResultSet rs = findCourseStmt.executeQuery();

            if (rs.next()) {
                String courseName = rs.getString("course_name");
                String courseCode = rs.getString("course_code");

                // Check if the student is already registered for this course
                if (isStudentRegistered(connection, studentUsername, courseName, courseCode, professorUsername)) {
                    System.out.println("You are already registered for this course.");
                    return false;
                }

                // Register the student for the course
                String registerQuery = "INSERT INTO student_courses (student_username, student_registered_course_name, registered_course_code, professor_username) VALUES (?, ?, ?, ?)";

                try (PreparedStatement registerStmt = connection.prepareStatement(registerQuery)) {
                    registerStmt.setString(1, studentUsername.trim());
                    registerStmt.setString(2, courseName.trim());
                    registerStmt.setString(3, courseCode.trim());
                    registerStmt.setString(4, professorUsername.trim());

                    int rowsInserted = registerStmt.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("Successfully registered for the course: " + courseName + " (" + courseCode + ")");
                        return true;
                    } else {
                        System.out.println("Failed to register for the course. Please try again.");
                        return false;
                    }
                }

            } else {
                System.out.println("No course found with the provided details.");
                return false;
            }

        } catch (Exception e) {
            System.out.println("An error occurred while registering for the course.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean isStudentRegistered(Connection connection, String studentUsername, String courseName, String courseCode, String professorUsername) {
        String checkRegistrationQuery = "SELECT 1 FROM student_courses WHERE student_username = ? AND student_registered_course_name = ? AND registered_course_code = ? AND professor_username = ? LIMIT 1";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkRegistrationQuery)) {
            checkStmt.setString(1, studentUsername.trim());
            checkStmt.setString(2, courseName.trim());
            checkStmt.setString(3, courseCode.trim());
            checkStmt.setString(4, professorUsername.trim());

            ResultSet rs = checkStmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            System.out.println("An error occurred while checking existing registrations.");
            e.printStackTrace();
            return false;
        }
    }

    public void viewMarks(String studentUsername) {
        Mark marksD = Mark.getInstance();
        List<StudentMark> marksList = marksD.getMarksForStudent(studentUsername);

        System.out.println("\n--- Your Marks ---");
        if (marksList.isEmpty()) {
            System.out.println("No marks available to display.");
            return;
        }

        for (StudentMark mark : marksList) {
            System.out.println(mark);
        }
    }

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
        String query = "SELECT b.title, b.author, b.code, bb.borrow_date FROM borrowed_books bb " + "JOIN books b ON bb.book_id = b.id " +
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

    public void viewTranscript(String studentUsername) {
        Mark marksService = Mark.getInstance();
        List<Transcript> transcriptList = marksService.getTranscriptForStudent(studentUsername);

        System.out.println("\n--- Your Transcript ---");
        if (transcriptList.isEmpty()) {
            System.out.println("No transcript records available.");
            return;
        }

        // Display header
        System.out.printf("%-12s | %-30s | %-7s | %-14s | %-6s | %-10s%n",
                "Course Code", "Course Name", "Credits", "Overall Points", "Grade", "GPA Points");
        System.out.println("-----------------------------------------------------------------------------------------------");

        // Display each transcript entry
        for (Transcript transcript : transcriptList) {
            System.out.printf("%-12s | %-30s | %-7d | %-14s | %-6s | %-10s%n",
                    transcript.getCourseCode(),
                    transcript.getCourseName(),
                    transcript.getCredits(),
                    transcript.getOverallPoints() != null ? String.format("%.2f", transcript.getOverallPoints()) : "N/A",
                    transcript.getGradeLetter(),
                    transcript.getGpaPoints() != null ? String.format("%.2f", transcript.getGpaPoints()) : "N/A");
        }

        // Optionally, calculate and display GPA
        double totalGpaPoints = 0.0;
        int totalCredits = 0;
        for (Transcript transcript : transcriptList) {
            if (transcript.getGpaPoints() != null) {
                totalGpaPoints += transcript.getGpaPoints() * transcript.getCredits();
                totalCredits += transcript.getCredits();
            }
        }

        if (totalCredits > 0) {
            double gpa = totalGpaPoints / totalCredits;
            System.out.printf("\nCumulative GPA: %.2f%n", gpa);
        } else {
            System.out.println("\nCumulative GPA: N/A");
        }
    }

}
