import java.util.Vector;


public class Course {

    private String courseName;
    private String courseID;
    private int credits;
    private Vector<Student> courseStudents;
    private Employee prof;


    public Course(String courseName, String courseID) { 
        this.courseName = courseName;
        this.courseID = courseID;
    }

    public String getCourseName() { 
        return courseName;
    }

    public void setCourseName(String courseName) { 
        this.courseName = courseName;
    }

    public String getCourseID() { 
        return courseID;
    }

    public void setCourseID(String courseID) { 
        this.courseID = courseID;
    }
}
