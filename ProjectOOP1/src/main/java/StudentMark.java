public class StudentMark {
    private String courseName;
    private String courseCode;
    private Double firstAttestation;
    private Double secondAttestation;
    private Double finalExam;

    public StudentMark(String courseName, String courseCode, Double firstAttestation, Double secondAttestation, Double finalExam) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Double getFirstAttestation() {
        return firstAttestation;
    }

    public Double getSecondAttestation() {
        return secondAttestation;
    }

    public Double getFinalExam() {
        return finalExam;
    }

    @Override
    public String toString() {
        return String.format("Course: %s | Code: %s | First Attestation: %s | Second Attestation: %s | Final Grade: %s",
                courseName,courseCode,firstAttestation != null ? firstAttestation : "N/A",secondAttestation != null ? secondAttestation : "N/A",finalExam != null ? finalExam : "N/A");
    }
}
