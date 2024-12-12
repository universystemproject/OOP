public class User {

    private int id;
    private String username;
    private String role;
    private String password;

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.password = password;
    }
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
