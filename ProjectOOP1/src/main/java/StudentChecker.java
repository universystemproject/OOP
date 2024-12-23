import java.util.List;


public class StudentChecker {
    private Teacher teacher;
    private Mark studentMarks;


    public StudentChecker(Teacher teacher) {
        this.teacher = teacher;
        this.studentMarks = new Mark();
    }


    public boolean checkStudent(String studentUsername, String teacherUsername, String courseName) {
        List<StudentRegisteredCourse> students = teacher.viewMyStudents(teacherUsername);
        boolean studentFound = false;

        for (StudentRegisteredCourse student : students) {
            if (student.getUsername() != null && student.getRegistered_course_name() != null &&
                    student.getUsername().trim().equalsIgnoreCase(studentUsername.trim()) &&
                    student.getRegistered_course_name().trim().equalsIgnoreCase(courseName.trim())) {
                studentFound = true;
                break;
            }
        }

        if (!studentFound) {
            System.out.println("Error: Student '" + studentUsername + "' is not associated with your courses for '" + courseName + "'.");
        }

        return studentFound;
    }


    public void assignMark(String studentUsername, String courseName, int markType, Double mark, String courseCode) {
        String markColumn = getMarkColumn(markType);
        if (markColumn == null) {
            System.out.println("Error: Invalid mark type.");
            return;
        }

        boolean exists = studentMarks.checkIfStudentExists(studentUsername, courseName);
        if (!exists) {
            boolean inserted = studentMarks.insertStudentToStudentMarks(studentUsername, courseName, courseCode);
            if (!inserted) {
                System.out.println("Error: Unable to insert student into student_marks table.");
                return;
            }
        }

        boolean updated = studentMarks.updateMark(studentUsername, courseName, markColumn, mark);
        if (!updated) {
            System.out.println("Error: Failed to assign mark.");
        }
    }


    private String getMarkColumn(int markType) {
        switch (markType) {
            case 1:
                return "first_attestation";
            case 2:
                return "second_attestation";
            case 3:
                return "final_exam";
            default:
                return null;
        }
    }
}
