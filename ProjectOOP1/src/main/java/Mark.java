import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Mark {
    private static Mark instance;

    public boolean checkIfStudentExists(String studentUsername, String courseName) {
        String checkQuery = "SELECT 1 FROM student_marks WHERE student_username = ? AND course = ? LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(checkQuery)) {

            pstmt.setString(1, studentUsername.trim());
            pstmt.setString(2, courseName.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Database error occurred while checking student in student_marks.");
            e.printStackTrace();
            return false;
        }
    }


    public boolean insertStudentToStudentMarks(String studentUsername, String courseName, String courseCode) {
        String insertQuery = "INSERT INTO student_marks (student_username, course, course_code) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {

            pstmt.setString(1, studentUsername.trim());
            pstmt.setString(2, courseName.trim());
            pstmt.setString(3, courseCode.trim());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Info: Inserted student '" + studentUsername + "' into student_marks for course '" + courseName + "'.");
                return true;
            } else {
                System.out.println("Warning: No rows inserted for student '" + studentUsername + "' in course '" + courseName + "'.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error: Failed to insert student '" + studentUsername + "' into student_marks for course '" + courseName + "'.");
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateMark(String studentUsername, String courseName, String markType, Double mark) {
        String updateQuery = "UPDATE student_marks SET " + markType + " = ? WHERE student_username = ? AND course = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {

            pstmt.setDouble(1, mark);
            pstmt.setString(2, studentUsername.trim());
            pstmt.setString(3, courseName.trim());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Info: Updated " + markType.replace("_", " ") + " for student '" + studentUsername + "' in course '" + courseName + "'.");
                return true;
            } else {
                System.out.println("Warning: No rows updated for student '" + studentUsername + "' in course '" + courseName + "'.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error: Failed to update " + markType.replace("_", " ") + " for student '" + studentUsername + "' in course '" + courseName + "'.");
            e.printStackTrace();
            return false;
        }
    }
    public static synchronized Mark getInstance() {
        if (instance == null) {
            instance = new Mark();
        }
        return instance;
    }
    public List<StudentMark> getMarksForStudent(String studentUsername) {
        List<StudentMark> marksList = new ArrayList<>();
        String query = "SELECT sm.course, c.course_name, c.course_code, sm.first_attestation, sm.second_attestation, sm.final_exam " +
                "FROM student_marks sm " +
                "JOIN courses c ON sm.course_code = c.course_code " +
                "WHERE sm.student_username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, studentUsername.trim());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String courseName = rs.getString("course_name");
                String courseCode = rs.getString("course_code");
                Double firstAttestation = rs.getDouble("first_attestation");
                if (rs.wasNull()) firstAttestation = null;
                Double secondAttestation = rs.getDouble("second_attestation");
                if (rs.wasNull()) secondAttestation = null;
                Double finalExam = rs.getDouble("final_exam");
                if (rs.wasNull()) finalExam = null;

                StudentMark mark = new StudentMark(courseName, courseCode, firstAttestation, secondAttestation, finalExam);
                marksList.add(mark);
            }

            System.out.println("Retrieved marks for student " + studentUsername);

        } catch (SQLException e) {
            System.out.println("Failed to retrieve marks for student " + studentUsername + " Error: " + e.getMessage());
            e.printStackTrace();
        }

        return marksList;
    }

    public List<Transcript> getTranscriptForStudent(String studentUsername) {
        List<Transcript> transcriptList = new ArrayList<>();
        String query = "SELECT sm.course_code, c.course_name, c.credits, sm.first_attestation, sm.second_attestation, sm.final_exam " +
                "FROM student_marks sm " +
                "JOIN courses c ON sm.course_code = c.course_code " +
                "WHERE sm.student_username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, studentUsername.trim());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                String courseName = rs.getString("course_name");
                int credits = rs.getInt("credits");
                Double firstAttestation = rs.getDouble("first_attestation");
                if (rs.wasNull()) firstAttestation = null;
                Double secondAttestation = rs.getDouble("second_attestation");
                if (rs.wasNull()) secondAttestation = null;
                Double finalExam = rs.getDouble("final_exam");
                if (rs.wasNull()) finalExam = null;

                Double overallPoints = calculateOverallPoints(firstAttestation, secondAttestation, finalExam);
                String gradeLetter = determineGradeLetter(overallPoints);
                Double gpaPoints = mapGradeToGPA(gradeLetter);

                Transcript entry = new Transcript(courseCode, courseName, credits, overallPoints, gradeLetter, gpaPoints);
                transcriptList.add(entry);
            }

            System.out.println("Retrieved transcript for student '" + studentUsername + "'.");

        } catch (SQLException e) {
            System.out.println("Failed to retrieve transcript for student '" + studentUsername + "'. Error: " + e.getMessage());
            e.printStackTrace();
        }

        return transcriptList;
    }


    private Double calculateOverallPoints(Double firstAttestation, Double secondAttestation, Double finalExam) {
        if (firstAttestation == null || secondAttestation == null || finalExam == null) {
            return null; 
        }
        double weightFirst = 0.20;
        double weightSecond = 0.30;
        double weightFinal = 0.50;

        return (firstAttestation * weightFirst) + (secondAttestation * weightSecond) + (finalExam * weightFinal);
    }


    private String determineGradeLetter(Double overallPoints) {
        if (overallPoints == null) {
            return "N/A"; 
        }

        if (overallPoints >= 95.0) return "A";
        if (overallPoints >= 90.0) return "A-";
        if (overallPoints >= 87.0) return "B+";
        if (overallPoints >= 83.0) return "B";
        if (overallPoints >= 80.0) return "B-";
        if (overallPoints >= 77.0) return "C+";
        if (overallPoints >= 73.0) return "C";
        if (overallPoints >= 70.0) return "C-";
        if (overallPoints >= 67.0) return "D+";
        if (overallPoints >= 63.0) return "D";
        return "F";
    }

    
    private Double mapGradeToGPA(String gradeLetter) {
        switch (gradeLetter) {
            case "A": return 4.0;
            case "A-": return 3.67;
            case "B+": return 3.33;
            case "B": return 3.0;
            case "B-": return 2.67;
            case "C+": return 2.33;
            case "C": return 2.0;
            case "C-": return 1.67;
            case "D+": return 1.33;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return null; 
        }
    }
}
