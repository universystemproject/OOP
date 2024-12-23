public class Transcript {
    private String courseCode;
    private String courseName;
    private int credits;
    private Double overallPoints;
    private String gradeLetter;
    private Double gpaPoints;

    // Constructor
    public Transcript(String courseCode, String courseName, int credits, Double overallPoints, String gradeLetter, Double gpaPoints) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.overallPoints = overallPoints;
        this.gradeLetter = gradeLetter;
        this.gpaPoints = gpaPoints;
    }

    // Getters
    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public Double getOverallPoints() {
        return overallPoints;
    }

    public String getGradeLetter() {
        return gradeLetter;
    }

    public Double getGpaPoints() {
        return gpaPoints;
    }

    @Override
    public String toString() {
        return String.format("Course Code: %s | Course Name: %s | Credits: %d | Overall Points: %s | Grade: %s | GPA Points: %s",
                courseCode,
                courseName,
                credits,
                overallPoints != null ? String.format("%.2f", overallPoints) : "N/A",
                gradeLetter,
                gpaPoints != null ? String.format("%.2f", gpaPoints) : "N/A");
    }
}
