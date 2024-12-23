public class StudentRegisteredCourse {
    private int id;
    private String username;
    private String password;
    private String registered_course_name;
    private String registered_course_code;
    private String registered_professor_username;

    public StudentRegisteredCourse(String username, String registered_course_name, String registered_course_code, String registered_professor_username) {
        this.username = username;
        this.registered_course_name = registered_course_name;
        this.registered_course_code = registered_course_code;
        this.registered_professor_username = registered_professor_username;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRegistered_course_name() {
        return registered_course_name;
    }
    public void setRegistered_course_name(String registered_course_name) {
        this.registered_course_name = registered_course_name;
    }
    public String getRegistered_course_code() {
        return registered_course_code;
    }
    public void setRegistered_course_code(String registered_course_code) {
        this.registered_course_code = registered_course_code;
    }
    public String getRegistered_professor_username() {
        return registered_professor_username;
    }
    public void setRegistered_professor_username(String registered_professor_username) {
        this.registered_professor_username = registered_professor_username;
    }

    @Override
    public String toString() {
        return String.format("Student: %s | Course: %s | Course Code: %s | Professor: %s",
                username, registered_course_name, registered_course_code, registered_professor_username);
    }
}
