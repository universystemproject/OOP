
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class Admin {


    public void addUser(List<User> users) {
        String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                for (User user : users) {
                    pstmt.setString(1, user.getUsername());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getRole());
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                connection.commit();
                System.out.println("Users added successfully.");
            } catch (Exception e) {
                connection.rollback();
                System.out.println("Error adding users. Transaction rolled back.");
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Failed to add users.");
            e.printStackTrace();
        }
    }


    public void removeUser(User user) {
        String deleteQuery = "DELETE FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {

            pstmt.setString(1, user.getUsername());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User " + user.getUsername() + " removed successfully.");
            } else {
                System.out.println("User " + user.getUsername() + " not found.");
            }

        } catch (Exception e) {
            System.out.println("Failed to remove user.");
            e.printStackTrace();
        }
    }


    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM users";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                users.add(user);
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve users.");
            e.printStackTrace();
        }

        return users;
    }
}
