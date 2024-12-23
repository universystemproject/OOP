import java.util.Vector;


public class Course {

    private String courseName;
    private String courseCode;
    private int credits;
    //*private Vector<Student> courseStudents;*//
    private String prof;


    public Course(String courseName, String courseID, Integer credits, String prof) {
        this.courseName = courseName;
        this.courseCode = courseID;
        this.credits = credits;
        this.prof = prof;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    public String getProf() {
        return prof;
    }
    public void setProf(String prof) {
        this.prof = prof;
    }

    @Override
    public String toString() {
        return String.format("Course: %s | Code: %s | Credits: %s | Professor: %s",
                courseName, courseCode, credits, prof);
    }

}