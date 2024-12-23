import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Teacher {

    public List<Course> viewMyCourses(String teacherName) {
        List<Course> courses = new ArrayList<>();
        String selectQuery = "SELECT * FROM courses WHERE professor_username = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectQuery)){
            pstmt.setString(1, teacherName);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Course course = new Course(
                        rs.getString("course_name"),
                        rs.getString("course_code"),
                        rs.getInt("credits"),
                        rs.getString("professor_username")
                );
                courses.add(course);
            }
        }
        catch(Exception e){
            System.out.println("Failed to view My Courses");
            e.printStackTrace();
        }
        return courses;
    }

    public List<StudentRegisteredCourse> viewMyStudents(String teacherName) {
        List<StudentRegisteredCourse> students = new ArrayList<>();
        String selectQuery = "SELECT * FROM student_courses WHERE professor_username = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(selectQuery)){
            pstmt.setString(1, teacherName);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                StudentRegisteredCourse sregc = new StudentRegisteredCourse(
                        rs.getString("student_username"),
                        rs.getString("student_registered_course_name"),
                        rs.getString("registered_course_code"),
                        rs.getString("professor_username")
                );
                students.add(sregc);
            }
        }
        catch(Exception e){
            System.out.println("Failed to view My Students");
            e.printStackTrace();
        }
        return students;
    }


}
